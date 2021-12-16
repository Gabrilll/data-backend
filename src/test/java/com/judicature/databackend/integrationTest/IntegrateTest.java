package com.judicature.databackend.integrationTest;

import com.judicature.databackend.controller.GraphController;
import com.judicature.databackend.controller.NodeController;
import com.judicature.databackend.controller.RelationController;
import com.judicature.databackend.vo.NodeVO;
import com.judicature.databackend.vo.RelationVO;
import com.judicature.databackend.vo.ResponseVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class IntegrateTest {
    @Autowired
    GraphController graphController;
    @Autowired
    NodeController nodeController;
    @Autowired
    RelationController relationController;

    @Test
    //测试展示图谱
    public void testShowGraph() throws Exception{
        ResponseVO responseVO = graphController.getGraph();
        Assert.assertEquals(true,responseVO.getSuccess());
    }

    @Test
    //测试添加节点后查看图谱
    public void testAddNodeAndShow() throws Exception{
        NodeVO nodeVO = new NodeVO();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("hhh");
        nodeVO.setLabels(labels);
        Map<String,String> map = new HashMap<>();
        map.put("name","sds");
        nodeVO.setProperties(map);
        ResponseVO responseVO = nodeController.addNode(nodeVO,0);
        Assert.assertEquals(true,responseVO.getSuccess());
    }

    @Test
    //测试添加节点后删除节点
    public void testAddAndDeleteNode() throws Exception{
        NodeVO nodeVO = new NodeVO();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("hhh");
        nodeVO.setLabels(labels);
        Map<String,String> map = new HashMap<>();
        map.put("name","sds");
        nodeVO.setProperties(map);
        ResponseVO responseVO = nodeController.addNode(nodeVO,0);
        Assert.assertEquals(true,responseVO.getSuccess());

        Object id = responseVO.getContent();
        ResponseVO responseVO1 = nodeController.deleteNode(Long.valueOf(String.valueOf(id)),0);
        Assert.assertEquals(true,responseVO1.getSuccess());
    }

    @Test
    //测试更新节点
    public void testUpdateNode() throws Exception{
        NodeVO nodeVO = new NodeVO();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("sda");
        nodeVO.setLabels(labels);
        nodeController.updateNode(nodeVO,0);
    }

    @Test
    //测试直接删除节点
    public void testDeleteNode() throws Exception{
        nodeController.deleteNode(1L,0);
    }

    @Test
    //测试添加节点，再在新添节点间增加关系后删除
    public void testAddAndDeleteRelation() throws Exception{
        NodeVO nodeVO1 = new NodeVO();
        ArrayList<String> labels1 = new ArrayList<>();
        labels1.add("hhh");
        nodeVO1.setLabels(labels1);
        Map<String,String> map1 = new HashMap<>();
        map1.put("name","sds");
        nodeVO1.setProperties(map1);
        ResponseVO responseVO1 = nodeController.addNode(nodeVO1,0);

        NodeVO nodeVO2 = new NodeVO();
        ArrayList<String> labels2 = new ArrayList<>();
        labels2.add("hhh");
        nodeVO2.setLabels(labels2);
        Map<String,String> map2 = new HashMap<>();
        map2.put("name","sds");
        nodeVO2.setProperties(map2);
        ResponseVO responseVO2 = nodeController.addNode(nodeVO2,0);

        Long start = Long.valueOf(String.valueOf(responseVO1.getContent()));
        Long end = Long.valueOf(String.valueOf(responseVO2.getContent()));

        RelationVO relationVO = new RelationVO();
        relationVO.setStart(start);
        relationVO.setEnd(end);
        relationVO.setType("sds");

        ResponseVO responseVO3 = relationController.addRelation(relationVO,0);

        Long id = Long.valueOf(String.valueOf(responseVO3.getContent()));
        ResponseVO responseVO4 = relationController.deleteRelation(id,0);
        Assert.assertEquals(true,responseVO3.getSuccess());
        Assert.assertEquals(true,responseVO4.getSuccess());
    }

    @Test
    //测试添加节点后，添加关系再修改关系
    public void testUpdateRe() throws Exception{
        NodeVO nodeVO1 = new NodeVO();
        ArrayList<String> labels1 = new ArrayList<>();
        labels1.add("hhh");
        nodeVO1.setLabels(labels1);
        Map<String,String> map1 = new HashMap<>();
        map1.put("name","sds");
        nodeVO1.setProperties(map1);
        ResponseVO responseVO1 = nodeController.addNode(nodeVO1,0);

        NodeVO nodeVO2 = new NodeVO();
        ArrayList<String> labels2 = new ArrayList<>();
        labels2.add("hhh");
        nodeVO2.setLabels(labels2);
        Map<String,String> map2 = new HashMap<>();
        map2.put("name","sds");
        nodeVO2.setProperties(map2);
        ResponseVO responseVO2 = nodeController.addNode(nodeVO2,0);

        Long start = Long.valueOf(String.valueOf(responseVO1.getContent()));
        Long end = Long.valueOf(String.valueOf(responseVO2.getContent()));

        RelationVO relationVO = new RelationVO();
        relationVO.setStart(start);
        relationVO.setEnd(end);
        relationVO.setType("sds");

        ResponseVO responseVO3 = relationController.addRelation(relationVO,0);

        relationVO.setType("nnn");
        relationVO.setIdentity(Long.valueOf(String.valueOf(responseVO3.getContent())));
        ResponseVO responseVO4 = relationController.updateRelation(relationVO,0);
        Assert.assertEquals(true,responseVO4.getSuccess());
    }

}
