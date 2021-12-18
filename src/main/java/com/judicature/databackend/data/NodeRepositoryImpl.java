package com.judicature.databackend.data;

import com.judicature.databackend.po.Node;
import com.judicature.databackend.po.Property;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class NodeRepositoryImpl implements NodeOperations {
    Driver driver;

    @Autowired
    public NodeRepositoryImpl(Driver driver) {
        this.driver = driver;
    }


    @Override
    public long addNode(Node node) {
        String labelList = toLabelStr(node.getLabels());

        String properties = toPropStr(node.getProperties());
        long res = -1;
        String query = "CALL apoc.create.node(" + labelList + "," + properties +
                "            ) YIELD node return id(node)";
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            res = transaction.run(query).single().get(0).asLong();
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return res;
    }

    @Override
    public int updateNode(Node node) {
        int res = -1;
        String properties = toPropStr(node.getProperties());
        String query = "MATCH (n) WHERE id(n) = " + node.getIdentity() + " SET n+=\n" + properties;
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            transaction.run(query);
            transaction.commit();
            res = 1;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return res;
    }

    @Override
    public Node getNodeById(long identity) {
        String query = "MATCH (n) WHERE id(n)=" + identity + " RETURN id(n) as identity,labels(n) as labels,properties(n) as properties\n";
        return getNodeByQuery(query);
    }

    @Override
    public List<Node> getAllNodes() {
        String query = "MATCH (n) RETURN id(n) as identity,labels(n) as labels,properties(n) as properties";
        return queryToNodeList(query);
    }

    @Override
    public List<Node> getSubNodes(Long identity) {
        String query = "MATCH (n) WHERE id(n) = " + identity + " CALL apoc.path.subgraphNodes(n,{relationshipFilter: '<|>',minLevel: 0,maxLevel: -1})\n" +
                "        YIELD node RETURN id(node) as identity,labels(node) as labels,properties(node) as properties LIMIT 30\n";
        return queryToNodeList(query);
    }

    @Override
    public Node getNodeByName(String name) {
        String query = "MATCH (n) WHERE n.name=\"" + name + "\" RETURN id(n) as identity,labels(n) as labels,properties(n) as properties\n";
        return getNodeByQuery(query);
    }

    @Override
    public List<Node> getNodeByLabel(String label) {
        String query = "MATCH (n:" + label + ") RETURN id(n) as identity,labels(n) as labels,properties(n) as properties LIMIT 50\n";
        return queryToNodeList(query);
    }

    @Override
    public Node getNodeByStartAndRe(String startName, String reName) {
        String query = "MATCH (n1)-[r]->(n) WHERE n1.name=\"" + startName + "\" AND r.name=\""+reName+"\" RETURN id(n) as identity,labels(n) as labels,properties(n) as properties\n";
        return getNodeByQuery(query);
    }

    @Override
    public List<Node> getNodesByP(String startName, String reName) {
        String query = "MATCH (n1)-[r]->(n) WHERE n1.name=\"" + startName + "\" AND r.name=\""+reName+"\" RETURN id(n) as identity,labels(n) as labels,properties(n) as properties\n";
        return queryToNodeList(query);
    }

    @Override
    public Node getNodeByUUID(String uuid) {
        String query="MATCH (n) WHERE n.UUID=\""+uuid+"\" RETURN id(n) as identity,labels(n) as labels,properties(n) as properties LIMIT 1";
        return getNodeByQuery(query);
    }



    private Node getNodeByQuery(String query) {
        Node node = new Node();
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            node = transaction.run(query).list(r -> new Node(r.get("identity").asLong(), r.get("labels").asList(Value::asString), r.get("properties").asMap().entrySet().stream().map(v -> new Property(v.getKey(), v.getValue().toString())).collect(Collectors.toList()))).get(0);
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return node;
    }

    private List<Node> queryToNodeList(String query) {
        List<Node> nodes = new ArrayList<>();
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            nodes = transaction.run(query).list(r -> new Node(r.get("identity").asLong(), r.get("labels").asList(Value::asString), r.get("properties").asMap().entrySet().stream().map(v -> new Property(v.getKey(), v.getValue().toString())).collect(Collectors.toList())));
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return nodes;
    }

    private String toLabelStr(List<String> labels) {
        StringBuilder labelList = new StringBuilder("[");
        if (labels != null) {
            for (int i = 0; i < labels.size(); i++) {
                labelList.append("\"").append(labels.get(i)).append("\"");
                if (i < labels.size() - 1) {
                    labelList.append(",");
                }
            }
        }
        labelList.append("]");
        return labelList.toString();
    }

    private String toPropStr(List<Property> properties) {
        StringBuilder propStr = new StringBuilder("{");
        if (properties != null) {
            for (int i = 0; i < properties.size(); i++) {
                Property cur = properties.get(i);
                propStr.append(cur.getKey()).append(":\"").append(cur.getValue()).append("\"");
                if (i < properties.size() - 1) {
                    propStr.append(",");
                }
            }
        }
        propStr.append("}");
        return propStr.toString();
    }
}
