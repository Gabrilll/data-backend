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
public class DependencyVOTest {

    DependencyVO dependencyVO = new DependencyVO();
    @BeforeEach
    public void setup(){
        dependencyVO.setDependencyNodes(null);
    }
    @Test
    void getDependencyNodes() {
        dependencyVO.getDependencyNodes();
    }

    @Test
    void setDependencyNodes() {
        List<DependencyNode> dependencyVOList = new ArrayList<>();
        dependencyVO.setDependencyNodes(dependencyVOList);
    }

    @Test
    void testToString() {
        dependencyVO.toString();
    }
}