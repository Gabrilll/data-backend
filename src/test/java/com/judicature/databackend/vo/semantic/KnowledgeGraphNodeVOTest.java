package com.judicature.databackend.vo.semantic;

import com.judicature.databackend.model.PolysemantNamedEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KnowledgeGraphNodeVOTest {
    KnowledgeGraphNodeVO k = new KnowledgeGraphNodeVO();

    @BeforeEach
    public void setup(){
        k.setName(null);
        k.setId(1);
        k.setAlpha(1);
        k.setSize(1.0);
        k.setShape(null);
        k.setPolysemantNamedEntities(null);
        k.setColor(null);
    }

    @Test
    void getId() {
        k.getId();
    }

    @Test
    void setId() {
        k.setId(0);
    }

    @Test
    void getName() {
        k.getName();
    }

    @Test
    void setName() {
        k.setName("name");
    }

    @Test
    void getShape() {
        k.getShape();
    }

    @Test
    void setShape() {
        k.setShape("name");
    }

    @Test
    void getColor() {
        k.getColor();
    }

    @Test
    void setColor() {
        k.setColor("red");
    }

    @Test
    void getSize() {
        k.getSize();
    }

    @Test
    void setSize() {
        k.setSize(1.0);
    }

    @Test
    void getAlpha() {
        k.getAlpha();
    }

    @Test
    void setAlpha() {
        k.setAlpha(1);
    }

    @Test
    void getPolysemantNamedEntities() {
        k.getPolysemantNamedEntities();
    }

    @Test
    void setPolysemantNamedEntities() {
        List<PolysemantNamedEntity> list = new ArrayList<>();
        k.setPolysemantNamedEntities(list);
    }

    @Test
    void testToString() {
        k.toString();
    }
}