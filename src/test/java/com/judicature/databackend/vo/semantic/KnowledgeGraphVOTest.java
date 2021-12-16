package com.judicature.databackend.vo.semantic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KnowledgeGraphVOTest {

    KnowledgeGraphVO k = new KnowledgeGraphVO();
    @BeforeEach
    public void setup(){
        k.setKnowledgeGraphStatements(null);
    }

    @Test
    void getKnowledgeGraphStatements() {
        k.getKnowledgeGraphStatements();
    }

    @Test
    void setKnowledgeGraphStatements() {
        List<KnowledgeGraphStatementVO> list = new ArrayList<>();
        k.setKnowledgeGraphStatements(list);
    }

    @Test
    void testToString() {
        k.toString();
    }
}