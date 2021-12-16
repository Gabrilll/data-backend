package com.judicature.databackend.blImpl;

import com.judicature.databackend.vo.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 已测试方法：getGraph、exportXml、filterByNodeLabels、getStatistics、getGraphList、getGraphById
 * getGraphNum、removeGraph、getGraphByNode、getConstructionDetail、stopConstruction、randomlyInitGraph
 * 覆盖率：89%
 */

//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphServiceImplTest {
    @Autowired
    GraphServiceImpl graphService;

    /**
     * 测试随机获取图——成功获取
     */
    @Test
    public void getGraphTest1(){
        ResponseVO responseVO = graphService.getGraph();
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试随机获取图——Graph为空，随机初始化一张图
     */
    @Test
    public void getGraphTest2(){
        Graph.setInstance(null);
        ResponseVO responseVO = graphService.getGraph();
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试将知识图谱导出XML文件——导出成功
     */
    @Test
    public void exportXmlTest1(){
        ResponseVO responseVO = graphService.exportXml();
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试根据标签过滤节点——过滤失败（graphId不存在）
     */
    @Test
    public void filterByNodeLabelsTest1(){
        List<String> labels = new ArrayList<>();
        ResponseVO responseVO = graphService.filterByNodeLabels(labels,-1);
        Assert.assertEquals("过滤失败",responseVO.getMessage());
    }

    /**
     * 测试过滤条件为空——过滤成功
     */
    @Test
    public void filterByNodeLabelsTest3(){
        List<String> labels = new ArrayList<>();
        ResponseVO responseVO = graphService.filterByNodeLabels(labels,0);
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试获取图列表——获取成功
     */
    @Test
    public void getGraphListTest1(){
        ResponseVO responseVO = graphService.getGraphList();
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试获取图列表，图列表为空，随机初始化一张图
     */
    @Test
    public void getGraphListTest2(){
        GraphList.removeGraph(0);
        System.out.println(GraphList.getGraphNum());
        ResponseVO responseVO = graphService.getGraphList();
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试根据标签过滤节点——过滤成功
     * 过滤行为所在的图——graphId=0
     * 包含节点nodeVO1(id0,labels:["过滤条件1"]),nodeVO2(id:1,labels:["过滤条件2"]),nodeVO3(id:2,labels:["过滤条件1"])
     * 包含关系：re1:nodeVO1->nodeVO2; re2:nodeVO2->nodeVO3; re3:nodeVO3->nodeVO1
     * 过滤后剩下nodeVO2,nodeVO1,re3:nodeVO3->nodeVO1
     */
    @Test
    public void testFilterByNodeLabels2(){
        GraphVO graphVO = new GraphVO();
        //分别声明节点1、2、3
        NodeVO nodeVO1 = new NodeVO();
        Map<String,String> pro = new HashMap<>();
        pro.put("name","tiancai");
        nodeVO1.setIdentity(0L);
        List<String> labels1 = new ArrayList<>();
        labels1.add("过滤条件1");
        nodeVO1.setLabels(labels1);
        nodeVO1.setProperties(pro);

        NodeVO nodeVO2 = new NodeVO();
        nodeVO2.setIdentity(1L);
        List<String> labels2 = new ArrayList<>();
        labels2.add("过滤条件2");
        nodeVO2.setLabels(labels2);
        nodeVO2.setProperties(pro);

        NodeVO nodeVO3 = new NodeVO();
        nodeVO3.setIdentity(2L);
        List<String> labels3 = new ArrayList<>();
        labels3.add("过滤条件1");
        nodeVO3.setLabels(labels3);
        nodeVO3.setProperties(pro);

        //分别声明关系1：节点1->节点2；关系2：节点2->节点3；关系3：节点3->节点1
        RelationVO re1 = new RelationVO();
        re1.setStart(0L);
        re1.setEnd(1L);

        RelationVO re2 = new RelationVO();
        re2.setStart(1L);
        re2.setEnd(2L);

        RelationVO re3 = new RelationVO();
        re3.setStart(2L);
        re3.setEnd(0L);

        List<NodeVO> nodes = new ArrayList<>(Arrays.asList(nodeVO1,nodeVO2,nodeVO3));
        List<RelationVO> relationVOS = new ArrayList<>(Arrays.asList(re1,re2,re3));
        graphVO.setEdges(relationVOS);
        graphVO.setNodes(nodes);
        GraphList.addGraph(graphVO);

        List<String> labels = new ArrayList<>();
        labels.add("过滤条件1");
        ResponseVO responseVO = graphService.filterByNodeLabels(labels,0);

        Assert.assertTrue(responseVO.getSuccess());
    }

    @Test
    public void getStatisticsTest2(){
        GraphVO graphVO = new GraphVO();
        NodeVO node1 = new NodeVO();
        node1.setIdentity(0L);
        List<String> labels1 = new ArrayList<>();
        labels1.add("TestLabel1");
        node1.setLabels(labels1);
        Map<String,String> pro1 = new HashMap<>();
        pro1.put("pro1","nice");
        node1.setProperties(pro1);

        NodeVO node2 = new NodeVO();
        node2.setIdentity(1L);
        List<String> labels2 = new ArrayList<>();
        labels2.add("TestLabel2");
        node2.setLabels(labels2);
        Map<String,String> pro2 = new HashMap<>();
        pro2.put("pro2","nice");
        node2.setProperties(pro2);


        RelationVO relationVO = new RelationVO();
        relationVO.setIdentity(0L);
        relationVO.setStart(0L);
        relationVO.setStart(1L);
        relationVO.setProperties(pro1);
        relationVO.setType("test");

        List<NodeVO> nodes = new ArrayList<>(Arrays.asList(node1,node2));
        graphVO.setNodes(nodes);
        List<RelationVO> res = new ArrayList<>(Arrays.asList(relationVO));
        graphVO.setEdges(res);
        Graph.setInstance(graphVO);
        Graph.setInstance(GraphList.getGraph(0));

        ResponseVO responseVO = graphService.getStatistics();
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试根据图id在GraphList中获取图——获取成功
     */
    @Test
    public void getGraphByIdTest1(){
        ResponseVO responseVO = graphService.getGraphById(0);
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试获取图总数——获取成功且正确
     */
    @Test
    public void getGraphNumTest1(){
        ResponseVO responseVO = graphService.getGraphNum();
        Assert.assertEquals(GraphList.getGraphNum(),responseVO.getContent());
    }

    /**
     * 测试根据graphId移除GraphList中的图——移除成功
     */
    @Test
    public void removeGraphTest1(){
        ResponseVO responseVO = graphService.removeGraph(0);
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试根据节点id获取子图——获取成功
     * 获取节点53258相关子图
     */
    @Test
    public void getGraphByNodeTest1(){
        ResponseVO responseVO = graphService.getGraphByNode(53258L);
        Assert.assertTrue(responseVO.getSuccess());
    }


    /**
     * 测试根据startUrl和爬取页数，自动构建知识图谱——构建成功
     */
    @Test
    public void constructGraphTest1(){
        ResponseVO responseVO = graphService.constructGraph("https://baike.baidu.com/item/%E5%BC%A0%E8%89%BA%E8%B0%8B/147018?fr=aladdin",5);
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试获取构建时，已完成构建的页面数信息——获取成功
     */
    @Test
    public void getConstructionDetailTest1(){
        Assert.assertTrue(graphService.getConstructionDetail().getSuccess());
    }

    /**
     * 测试停止构建功能——成功停止
     */
    @Test
    public void stopConstructionTest(){
        Assert.assertTrue(graphService.stopConstruction().getSuccess());
    }

    /**
     * 测试
     */
    @Test
    public void getLabelsByGraphIdTest1() {
        GraphVO graph = new GraphVO();
        NodeVO node1 = new NodeVO();
        List<String> labels = new ArrayList<>(Arrays.asList("oh~myLove","howCute"));
        NodeVO node2 = new NodeVO();
        node1.setLabels(labels);
        node2.setLabels(labels);
        Map<String,String> pro = new HashMap<>();
        pro.put("name","tiancai");
        node1.setProperties(pro);
        node2.setProperties(pro);
        List<NodeVO> nodes = new ArrayList<>(Arrays.asList(node1,node2));
        graph.setNodes(nodes);
        GraphList.addGraph(graph);

        ResponseVO responseVO = graphService.getLabelsByGraphId(0);
        Assert.assertTrue(responseVO.getSuccess());
    }
}
