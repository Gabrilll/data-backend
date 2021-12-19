package com.judicature.databackend.mongodb;

import com.judicature.databackend.po.Document;
import org.apache.catalina.LifecycleState;

import java.util.List;

public interface DocumentOperations {
    List<Document> findFirst5Docs();
    List<Document> findDocumentByName(List<String> name);
}
