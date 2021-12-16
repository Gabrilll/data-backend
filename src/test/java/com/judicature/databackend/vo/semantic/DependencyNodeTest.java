package com.judicature.databackend.vo.semantic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DependencyNodeTest {

    DependencyNode dependencyNode = new DependencyNode();

    @BeforeEach
    public void setup(){
        dependencyNode.setArg(null);
        dependencyNode.setSemparent(0);
        dependencyNode.setSemrelate(null);
        dependencyNode.setSemparent(0);
        dependencyNode.setParent(0);
        dependencyNode.setNe(null);
        dependencyNode.setId(0);
        dependencyNode.setCont(null);
    }
    @Test
    void getId() {
        dependencyNode.getId();
    }

    @Test
    void setId() {
        dependencyNode.setId(1);
    }

    @Test
    void getCont() {
        dependencyNode.getCont();
    }

    @Test
    void setCont() {
        dependencyNode.setCont("1");
    }

    @Test
    void getPos() {
        dependencyNode.getPos();
    }

    @Test
    void setPos() {
        dependencyNode.setPos("1");
    }

    @Test
    void getNe() {
        dependencyNode.getNe();
    }

    @Test
    void setNe() {
        dependencyNode.setNe("1");
    }

    @Test
    void getParent() {
        dependencyNode.getParent();
    }

    @Test
    void setParent() {
        dependencyNode.setParent(1);
    }

    @Test
    void getRelate() {
        dependencyNode.getRelate();
    }

    @Test
    void setRelate() {
        dependencyNode.setRelate("1");
    }

    @Test
    void getSemparent() {
        dependencyNode.getSemparent();
    }

    @Test
    void setSemparent() {
        dependencyNode.setSemparent(1);
    }

    @Test
    void getSemrelate() {
        dependencyNode.getSemrelate();
    }

    @Test
    void setSemrelate() {
        dependencyNode.setSemrelate("1");
    }

    @Test
    void getArg() {
        dependencyNode.getArg();
    }

    @Test
    void setArg() {
        dependencyNode.setArg(null);
    }
}