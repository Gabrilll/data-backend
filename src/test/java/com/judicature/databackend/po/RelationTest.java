package com.judicature.databackend.po;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RelationTest {
    @Test
    public void TestRelation() throws Exception{
        Relation relation = new Relation();
        ArrayList<Property> properties = new ArrayList<>();
        Property property = new Property();
        property.setKey("name");
        property.setValue("hhh");
        properties.add(property);
        relation.setIdentity(1L);
        relation.setStart(2L);
        relation.setEnd(3L);
        relation.setType("hhh");
        relation.setProperties(properties);

        relation.getEnd();
        relation.getStart();
        relation.getIdentity();
        relation.getProperties();
        relation.getType();
    }
}
