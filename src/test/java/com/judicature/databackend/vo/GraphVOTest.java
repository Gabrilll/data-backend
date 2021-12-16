package com.judicature.databackend.vo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GraphVOTest {
    @Test
    public void TestGraphVO() throws Exception{
        GraphVO graphVO = new GraphVO();
        NodeVO nodeVO = new NodeVO();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("hhh");
        nodeVO.setLabels(labels);
        ArrayList<NodeVO> nodeVOS = new ArrayList<>();
        nodeVOS.add(nodeVO);

        RelationVO relationVO = new RelationVO();
        relationVO.setIdentity(1L);
        relationVO.setStart(2L);
        relationVO.setEnd(3L);
        ArrayList<RelationVO> relationVOS = new ArrayList<>();
        relationVOS.add(relationVO);


        graphVO.setNodes(nodeVOS);
        graphVO.setEdges(relationVOS);
        graphVO.getEdges();
        graphVO.getNodes();
    }
}
