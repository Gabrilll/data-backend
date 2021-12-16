package com.judicature.databackend.vo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PropertyVOTest {
    @Test
    public void TestPropertyVO() throws Exception{
        PropertyVO propertyVO = new PropertyVO();
        propertyVO.setKey("name");
        propertyVO.setValue("eee");
        propertyVO.getKey();
        propertyVO.getValue();
    }
}
