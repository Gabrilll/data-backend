package com.judicature.databackend.mongodb;

import com.judicature.databackend.po.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<Document, String> ,DocumentOperations{
    Document findDistinctByName(String name);



}
