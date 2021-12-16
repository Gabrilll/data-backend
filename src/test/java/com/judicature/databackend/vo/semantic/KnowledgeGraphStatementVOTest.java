package com.judicature.databackend.vo.semantic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KnowledgeGraphStatementVOTest {

    KnowledgeGraphStatementVO k = new KnowledgeGraphStatementVO();
    @BeforeEach
    public void setup(){
        k.setObject(null);
        k.setPredicate(null);
        k.setSubject(null);
    }

    @Test
    void getSubject() {
        k.getSubject();
    }

    @Test
    void setSubject() {
        KnowledgeGraphNodeVO kGS = new KnowledgeGraphNodeVO();
        k.setSubject(kGS);
    }

    @Test
    void getPredicate() {
        k.getPredicate();
    }

    @Test
    void setPredicate() {
        KnowledgeGraphNodeVO kGS = new KnowledgeGraphNodeVO();
        k.setPredicate(kGS);
    }

    @Test
    void getObject() {
        k.getObject();
    }

    @Test
    void setObject() {
        KnowledgeGraphNodeVO kGS = new KnowledgeGraphNodeVO();
        k.setObject(kGS);
    }

    @Test
    void testToString() {
        k.toString();
    }
}