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
public class GraphRepositoryImpl implements GraphOperations {

    Driver driver;

    @Autowired
    public GraphRepositoryImpl(Driver driver) {
        this.driver = driver;
    }

    @Override
    public List<Node> filterByNodeLabels(List<String> labels) {
        List<Node> res = new ArrayList<>();
        int len = labels.size();
        StringBuilder labelList = new StringBuilder("[");
        for (int i = 0; i < len; i++) {
            labelList.append("\"").append(labels.get(i)).append("\"");
            if (i < len - 1) {
                labelList.append(",");
            }
        }
        labelList.append("]");
        String query = "MATCH (n) WITH n AS n,labels(n) AS labels WHERE ALL(l IN labels WHERE l IN " + labelList +
                "        ) RETURN id(n) as identity,labels(n) as labels,properties(n) as properties";
        try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
            res = transaction.run(query).list(r -> new Node(r.get("identity").asLong(), r.get("labels").asList(Value::asString), r.get("properties").asMap().entrySet().stream().map(v -> new Property(v.getKey(), v.getValue().toString())).collect(Collectors.toList())));
            transaction.commit();
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return res;
    }
}
