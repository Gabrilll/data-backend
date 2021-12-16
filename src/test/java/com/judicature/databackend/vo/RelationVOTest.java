package com.judicature.databackend.vo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RelationVOTest {
    @Test
    public void TestRelationVO() throws Exception{
        RelationVO relationVO = new RelationVO();
        Map<String,String> properties = new HashMap<>();
        properties.put("name","hhh");
        relationVO.setIdentity(1L);
        relationVO.setStart(2L);
        relationVO.setEnd(3L);
        relationVO.setType("hhh");
        relationVO.setProperties(properties);

        relationVO.getProperties();
        relationVO.getEnd();
        relationVO.getStart();
        relationVO.getType();
        relationVO.getIdentity();
    }
}
