package com.judicature.databackend.data;

import com.judicature.databackend.po.Property;
import com.judicature.databackend.po.Relation;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class RelationRepositoryImpl implements RelationOperations {

    Driver driver;

    @Autowired
    public RelationRepositoryImpl(Driver driver) {
        this.driver = driver;
    }

    @Override
    public Long addRelation(Relation relation) {
        String query = "";
        if (relation.getType() != null) {
            query = "MATCH (x),(y) WHERE id(x)=" + relation.getStart() + " AND id(y)=" + relation.getEnd() + " AND NOT (x)-[:`" + relation.getType() + "`]->(y) CREATE (x)-[r:`" + relation.getType() + "`]->(y) return id(r)\n";
        } else {
            query = "MATCH (x),(y) WHERE id(x)=" + relation.getStart() + " AND id(y)=" + relation.getEnd() + " CREATE (x)-[r:re]->(y) return id(r)\n";
        }

        long res = -1;
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            res = transaction.run(query).single().get(0).asLong();
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return res;
    }

    @Override
    public Relation updateRelation(Relation relation) {
        String query = "MATCH (x)-[r]->(y)\n" +
                "        WHERE id(r)=" + relation.getIdentity() + " AND id(x)=" + relation.getStart() + " AND id(y)=" + relation.getEnd() + "\n" +
                "        CREATE (x)-[r2:" + relation.getType() + "]->(y) SET r2=r WITH r2,r,x,y DELETE r\n" +
                "        RETURN id(r2) as identity,id(x) as start,id(y) as end,type(r2) as type,properties(r2) as properties\n";

        return getRelationByQuery(query);
    }

    @Override
    public Relation getRelationById(long identity) {
        String query = "MATCH (n)-[r]->(m) WHERE id(r)=" + identity + "\n" +
                "        RETURN id(r) as identity,id(n) as start,id(m) as end,type(r) as type,properties(r) as properties\n";
        return getRelationByQuery(query);
    }

    @Override
    public List<Relation> getAllRe() {
        String query = "MATCH (a)-[r]->(b) RETURN id(r) as identity,id(a) as start,id(b) as end,type(r) as type,properties(r) as properties\n";
        return getRelationListByQuery(query);
    }

    @Override
    public List<Relation> getSubRe(List<Long> identity) {
        String query = "MATCH (a)-[r]->(b)\n" +
                "        WHERE id(a) in " + identity + " and id(b) in " + identity + "\n" +
                "        RETURN id(r) as identity,id(a) as start,id(b) as end,type(r) as type,properties(r) as properties";
        return getRelationListByQuery(query);
    }

    @Override
    public long getRelationByTriplet(Long startId, long endId, String type) {
        String query = "MATCH (x)-[r]->(y) WHERE id(x)="+startId+" and id(y)="+endId+" and r.name=\"" + type + "\"" +
                "        RETURN id(r)";
        long res = 0;
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            res = transaction.run(query).single().get(0).asLong();
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return res;
    }

    private List<Relation> getRelationListByQuery(String query) {
        List<Relation> relations = new ArrayList<>();
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            relations = transaction.run(query).list(r -> new Relation(r.get("identity").asLong(), r.get("start").asLong(), r.get("end").asLong(), r.get("type").asString(), r.get("properties").asMap().entrySet().stream().map(v -> new Property(v.getKey(), v.getValue().toString())).collect(Collectors.toList())));
            transaction.commit();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return relations;
    }

    private Relation getRelationByQuery(String query) {
        Relation res = new Relation();
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            res = transaction.run(query).list(r -> new Relation(r.get("identity").asLong(), r.get("start").asLong(), r.get("end").asLong(), r.get("type").asString(), r.get("properties").asMap().entrySet().stream().map(v -> new Property(v.getKey(), v.getValue().toString())).collect(Collectors.toList()))).get(0);
            transaction.commit();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return res;
    }
}
