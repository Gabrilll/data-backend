package com.judicature.databackend.po;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NodeTest {

    @Test
    public void testNode() throws Exception{
        Node node = new Node();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("test");
        Property property = new Property();
        ArrayList<Property> properties = new ArrayList<>();
        property.setKey("name");
        property.setValue("ljb");
        properties.add(property);

        node.setProperties(properties);
        node.setIdentity(1L);
        node.setLabels(labels);
        node.getProperties();
        node.getLabels();
        node.getIdentity();
    }
}
