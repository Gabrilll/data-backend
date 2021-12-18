package com.judicature.databackend.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.Map;

@Data
@org.springframework.data.mongodb.core.mapping.Document(collection = "docs")
public class Document {
    @Id
    private String id;
    private String name;
    private Map<String, Double> keywords;
    private String text;
}
