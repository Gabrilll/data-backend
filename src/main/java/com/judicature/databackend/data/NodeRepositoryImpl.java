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

import java.util.*;
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
        String query = "MATCH (n:" + label + ") RETURN id(n) as identity,labels(n) as labels,properties(n) as properties\n";
        return queryToNodeList(query);
    }

    @Override
    public Node getNodeByStartAndRe(String startName, String reName) {
        String query = "MATCH (n1)-[r]->(n) WHERE n1.name=\"" + startName + "\" AND r.name=\"" + reName + "\" RETURN id(n) as identity,labels(n) as labels,properties(n) as properties\n";
        return getNodeByQuery(query);
    }

    @Override
    public List<Node> getNodesByP(String startName, String reName) {
        String query = "MATCH (n1)-[r]->(n) WHERE n1.name=\"" + startName + "\" AND r.name=\"" + reName + "\" RETURN id(n) as identity,labels(n) as labels,properties(n) as properties\n";
        return queryToNodeList(query);
    }

    @Override
    public Node getNodeByUUID(String uuid) {
        String query = "MATCH (n) WHERE n.UUID=\"" + uuid + "\" RETURN id(n) as identity,labels(n) as labels,properties(n) as properties LIMIT 1";
        return getNodeByQuery(query);
    }

    @Override
    public List<Long> getKeyNodes(String key) {
        String query = "MATCH (n) WHERE '" + key + "' IN n.name OR '" + key + "' IN labels(n) OR '" + key + "' in keys(n) OR '" + key + "' IN apoc.map.mget(properties(n), keys(n)) RETURN id(n) as id LIMIT 10";
        return getIdsByKey(query);
    }

    @Override
    public List<Long> getKeyNodesByEdge(String key) {
        String query = "MATCH (m:裁判文书)-[r]-(n:裁判文书)\n" +
                " WHERE '" + key + "' IN r.name OR type(r) contains '" + key + "'" +
                " OR '" + key + "' in keys(r) OR '" + key + "' IN apoc.map.mget(properties(r), keys(r)) RETURN id(m) as id LIMIT 10";
        return getIdsByKey(query);
    }

    @Override
    public Long getDistanceBetweenNodes(Long id1, Long id2) {
        String query = "MATCH (a),(b)  WHERE id(a)=" + id1 + " AND id(b)=" + id2 + " WITH length(shortestPath((a)-[*]-(b))) AS l RETURN CASE WHEN l>0 THEN l ELSE 0 END";
        long res = 0L;
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            res = transaction.run(query).single().get(0).asLong();
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return res;
    }

    @Override
    public List<Double> getDistanceBetweenNodes(Long id1, List<Long> id2) {
        String query = "UNWIND" + id2 + " AS x Match (a),(b)  WHERE id(a)=x AND id(b)=" + id1 + " WITH length(shortestPath((a)-[*]-(b))) AS l ,a WITH a, CASE WHEN l>0 THEN l ELSE 2147483647 END AS dis RETURN a.weight/dis as weight";
        List<Double> res = new ArrayList<>();
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            res = transaction.run(query).list(r -> r.get("weight").asDouble());
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return res;
    }

    @Override
    public List<Long> getKeyNodes(Map<String, Double> weights) {
        List<String> key = new ArrayList<>(weights.keySet());
        String keyList = toLabelStr(key);
        String query = "call{UNWIND " + keyList + " AS k  MATCH (n) WHERE k IN n.name OR k IN labels(n) OR k in keys(n) OR k IN apoc.map.mget(properties(n), keys(n)) RETURN id(n) as id ,k LIMIT 5" +
                " UNION UNWIND " + keyList + " AS k MATCH (m:裁判文书)-[r]-(n:裁判文书)" +
                " WHERE k IN r.name OR type(r) contains k " +
                " OR k in keys(r) OR k IN apoc.map.mget(properties(r), keys(r)) RETURN k,id(m) as id LIMIT 5} WITH id,k,apoc.map.fromLists(" + keyList + "," + weights.values() + ")" +
                "AS w MATCH (n) where id(n) = id SET n += {weight:w[k]}" +
                " RETURN id";
        Set<Long> res = new HashSet<>();
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            transaction.run(query).list(r -> {
                res.add(r.get("id").asLong());
                return null;
            });
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return new ArrayList<>(res);
    }

    @Override
    public List<String> getNearestDocs(String name) {
        String query = "MATCH (n) where n.name=\"" + name + "\" with n MATCH (m:裁判文书) WHERE id(m)<>id(n) WITH length(shortestPath((n)-[*]-(m)))>0 as l,m.name as name WITH case when l>0 then l else 2147483647 end as l,name return name order by l limit 10";
        List<String> nodes = new ArrayList<>();
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            nodes = transaction.run(query).list(r -> r.get("name").asString());
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return nodes;
    }

    @Override
    public List<Node> getDocByLabel(String label) {
        label = "\"" + label + "\"";
        String query = "MATCH (n:裁判文书)-[r]-() WHERE " + label + " IN n.name OR " + label + " IN labels(n) OR " + label + " IN keys(n) OR " + label + " IN apoc.map.mget(properties(n), keys(n)) OR r.name CONTAINS "+label+" OR  type(r) CONTAINS "+label+" OR " + label + " IN keys(r) OR " + label + " IN apoc.map.mget(properties(r), keys(r)) RETURN id(n) as identity,labels(n) as labels,properties(n) as properties";
        return queryToNodeList(query);
    }


    private List<Long> getIdsByKey(String query) {
        List<Long> nodes = new ArrayList<>();
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            nodes = transaction.run(query).list(r -> r.get("id").asLong());
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return nodes;
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
