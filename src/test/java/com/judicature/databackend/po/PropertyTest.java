package com.judicature.databackend.po;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PropertyTest {
    @Test
    public void testProperty() throws Exception{
        Property property = new Property();
        property.setKey("name");
        property.setValue("ljb");
        property.getKey();
        property.getValue();
    }
}
