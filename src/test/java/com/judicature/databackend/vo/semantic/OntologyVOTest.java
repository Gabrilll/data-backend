package com.judicature.databackend.vo.semantic;

import com.judicature.databackend.vo.NodeVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OntologyVOTest {
    OntologyVO ontologyVO = new OntologyVO();
    @BeforeEach
    public void setup(){
        ontologyVO.setNodeVO(null);
        ontologyVO.setIndividual(null);
    }

    @Test
    void getIndividual() {
        ontologyVO.getIndividual();
    }

    @Test
    void setIndividual() {
        ontologyVO.setIndividual(null);
    }

    @Test
    void getNodeVO() {
        ontologyVO.getNodeVO();
    }

    @Test
    void setNodeVO() {
        NodeVO nodeVO = new NodeVO();
        ontologyVO.setNodeVO(nodeVO);
    }
}