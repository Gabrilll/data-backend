package com.judicature.databackend.mongodb;

import com.judicature.databackend.po.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentRepositoryImpl implements DocumentOperations {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Document> findFirst5Docs() {
        Query query = new Query();
        query.limit(5);
        return mongoTemplate.find(query, Document.class);
    }

    @Override
    public List<Document> findDocumentByName(List<String> name) {
        Criteria criteria=Criteria.where("name").in(name);
        return mongoTemplate.find(Query.query(criteria),Document.class);
    }
}
