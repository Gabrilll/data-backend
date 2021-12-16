package com.judicature.databackend.vo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NodeVOTest {

    @Test
    public void testNodeVO() throws Exception{
        NodeVO nodeVO = new NodeVO();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("hhh");
        Map<String,String> properties = new HashMap<>();
        properties.put("name","eee");
        nodeVO.setIdentity(1L);
        nodeVO.setLabels(labels);
        nodeVO.setProperties(properties);

        nodeVO.getLabels();
        nodeVO.getIdentity();
        nodeVO.getProperties();
    }
}
