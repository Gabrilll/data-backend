package com.judicature.databackend.data;

import com.judicature.databackend.po.Node;
import com.judicature.databackend.po.Property;
import com.judicature.databackend.util.VO2PO;
import com.judicature.databackend.vo.NodeVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Rollback
@Transactional(transactionManager = "transactionManager")

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeRepositoryTest {
    @Autowired
    NodeRepository nodeRepository;

    @Test
    public void testGetNodeById() {
        Node node = nodeRepository.getNodeById(25008L);
        NodeVO nodeVO = VO2PO.toNodeVO(node);
        System.out.println(nodeVO.getProperties().get("key"));
        Assert.assertEquals((long) node.getIdentity(), 25008);
    }

    @Test
    public void testGetNodeByUUID() {
        Node node = new Node();
        node.setLabels(List.of("hh", "12"));
        node.setProperties(List.of(new Property("UUID", "2")));
        long id = nodeRepository.addNode(node);
        Node res = nodeRepository.getNodeByUUID("2");
        Assert.assertEquals(id, (long) res.getIdentity());
    }

    @Test
    public void testAddNode() {
        Node node = new Node();
        node.setLabels(List.of("test", "guess"));
        node.setProperties(List.of(new Property("key", "value"), new Property("hh", "12")));
        long res = nodeRepository.addNode(node);
        log.info(String.valueOf(res));
    }

    @Test
    public void testDeleteNode() {
        nodeRepository.deleteNode(25001L);
        log.info("true");
    }

    @Test
    public void testUpdateNode() {
        Node node = new Node(25008L, List.of("test1", "guess1"), List.of(new Property("k", "v"), new Property("hh", "1")));
        int res = nodeRepository.updateNode(node);
        log.info(String.valueOf(res));
    }

    @Test
    public void testGetAllNodes() {
        List<Node> nodes = nodeRepository.getAllNodes();
        log.info(String.valueOf(nodes.size()));
    }

    @Test
    public void testGetSubNodes() {
        List<Node> nodes = nodeRepository.getSubNodes(814L);
        log.info(String.valueOf(nodes.size()));
    }

    @Test
    public void testGetNodeByName() {
        Node node = nodeRepository.getNodeByName("诊断");
        log.info(String.valueOf(node.getIdentity()));
    }

    @Test
    public void testGetNodeByLabel() {
        List<Node> nodes = nodeRepository.getNodeByLabel("裁判文书");
        log.info(String.valueOf(nodes.size()));
    }

    @Test
    public void testGetNodeByStartAndRe() {
        Node node = nodeRepository.getNodeByStartAndRe("诊断", "就是");
        log.info(String.valueOf(node.getIdentity()));
    }

    @Test
    public void testGetNodeByP() {
        List<Node> nodes = nodeRepository.getNodesByP("诊断", "就是");
        log.info(String.valueOf(nodes.size()));
    }

    @Test
    public void testGetKeyNodes() {
        String key = "上诉";
        List<Long> nodes = nodeRepository.getKeyNodes(key);
        log.info(String.valueOf(nodes.size()));
    }

    @Test
    public void testGetKeyNodesByEdge() {
        String key = "上诉";
        List<Long> nodes = nodeRepository.getKeyNodesByEdge(key);
        log.info(String.valueOf(nodes.size()));
    }

    @Test
    public void getDistanceBetweenNodes() {
        long res = nodeRepository.getDistanceBetweenNodes(1193L, 1196L);
        log.info(String.valueOf(res));
    }

    @Test
    public void testGetDistance() {
        List<Double> res = nodeRepository.getDistanceBetweenNodes(1993L, List.of(1996L));
        log.info(String.valueOf(res));
    }



}
