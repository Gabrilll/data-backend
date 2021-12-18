package com.judicature.databackend.data;

import com.judicature.databackend.mongodb.DocumentRepository;
import com.judicature.databackend.po.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Rollback
@Transactional(transactionManager = "transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DocumentRepositoryTest {

    @Autowired
    DocumentRepository documentRepository;

    @Test
    public void testFindFirst5(){
        List<Document> documents=documentRepository.findFirst5Docs();
        log.info(String.valueOf(documents.size()));
    }
}
