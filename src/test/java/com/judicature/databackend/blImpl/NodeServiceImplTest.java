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
 * 覆盖率：90%
 */
//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeServiceImplTest {

    @Autowired
    NodeServiceImpl nodeService;

    /**
     * 测试向指定graphId的图添加节点
     */
    @Test
    public void addNodeTest1() throws Exception{
        //初始化一个graphVO
        GraphVO graphVO = new GraphVO();
        List<NodeVO> nodes = new ArrayList<>();
        graphVO.setNodes(nodes);
        GraphList.addGraph(graphVO);

        //添加节点
        NodeVO node=new NodeVO();
        ArrayList<String> labels=new ArrayList<>();
        labels.add("testAddNode");
        node.setLabels(labels);

        Map<String,String> map=new HashMap<>();
        map.put("name","lhy");
        node.setProperties(map);

        ResponseVO responseVO=nodeService.addNode(node,0);
        System.out.println(responseVO.getContent());
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试添加节点——添加失败，图不存在
     */
    @Test
    public void addNodeTest2() throws Exception{
        NodeVO node=new NodeVO();
        ResponseVO responseVO = nodeService.addNode(node,-1);
        Assert.assertEquals(false,responseVO.getSuccess());
    }

    /**
     * 测试删除节点——成功
     * 此测试发现bug：未考虑图中只有节点的情况
     */
    @Test
    public void deleteNodeTest1() throws Exception{
        //初始化图
        GraphVO graphVO = new GraphVO();
        NodeVO nodeVO = new NodeVO();
        nodeVO.setIdentity(54988L);
        List<String> labels = new ArrayList<>(Arrays.asList("123"));
        nodeVO.setLabels(labels);
        List<NodeVO> list = new ArrayList<>(Arrays.asList(nodeVO));
        graphVO.setNodes(list);
        List<RelationVO> edges = new ArrayList<>();
        graphVO.setEdges(edges);

        Long id = 54988L;
        GraphList.addGraph(graphVO);
        ResponseVO re = nodeService.deleteNode(id,0);
        Assert.assertTrue(re.getSuccess());
    }

    /**
     * 测试删除节点——失败，节点不存在
     * 此测试发现bug：删除节点，删除失败应该返回ResponseVO.buildFailure("节点删除失败")
     */
    @Test
    public void deleteNodeTest2() throws Exception{
        //初始化图
        GraphVO graphVO = new GraphVO();
        NodeVO nodeVO = new NodeVO();
        nodeVO.setIdentity(1L);
        List<String> labels = new ArrayList<>(Arrays.asList("123"));
        nodeVO.setLabels(labels);
        List<NodeVO> list = new ArrayList<>(Arrays.asList(nodeVO));
        graphVO.setNodes(list);
        graphVO.setEdges(new ArrayList<>());
        GraphList.addGraph(graphVO);

        Long id = -1L;
        ResponseVO responseVO = nodeService.deleteNode(id,-1);
        Assert.assertFalse(responseVO.getSuccess());
    }


    /**
     * 测试更新节点——成功
     */
    @Test
    public void updateNodeTest1() throws Exception{
        //初始化图
        GraphVO graphVO = new GraphVO();
        NodeVO nodeVO = new NodeVO();
        nodeVO.setIdentity(54988L);
        List<String> labels = new ArrayList<>(Arrays.asList("123"));
        nodeVO.setLabels(labels);
        List<NodeVO> list = new ArrayList<>(Arrays.asList(nodeVO));
        graphVO.setNodes(list);
        graphVO.setEdges(new ArrayList<>());
        GraphList.addGraph(graphVO);

        NodeVO node = new NodeVO();
        node.setIdentity(54988L);
        Map<String,String> map=new HashMap<>();
        map.put("name","hhh");
        node.setProperties(map);
        node.setLabels(labels);
        ResponseVO responseVO = nodeService.updateNode(node,0);
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试更新节点——失败，图不存在
     */
    @Test
    public void updateNodeTest3() throws Exception{
        NodeVO node = new NodeVO();
        node.setIdentity(3L);
        ResponseVO responseVO = nodeService.updateNode(node,-1);
        Assert.assertFalse(responseVO.getSuccess());
    }

    /**
     * 测试获取所有节点——成功
     */
    @Test
    public void getAllNodesTest() throws Exception{
        List<NodeVO> nodes = nodeService.getAllNodes();
        Assert.assertFalse(nodes.isEmpty());
    }

    /**
     * 测试根据节点id获取部分子图的所有节点——成功
     */
    @Test
    public void getSubNodesTest() throws Exception{
        List<NodeVO> nodes = nodeService.getSubNodes(53258L);
        Assert.assertFalse(nodes.isEmpty());
    }

    /**
     * 测试根据id获取节点——失败，节点id不存在
     */
    @Test
    public void getNodeByIdTest1(){
        Long id = -1L;
        NodeVO node = nodeService.getNodeById(id);
        Assert.assertNull(node);
    }

    /**
     * 测试根据id获取节点——成功，id为53258的节点的name为"疾病"
     */
    @Test
    public void getNodeByIdTest2(){
        Long id = 53258L;
        NodeVO nodeVO = nodeService.getNodeById(id);
        Assert.assertEquals("疾病",nodeVO.getProperties().get("name"));
    }

    /**
     * 测试根据name获取节点——成功
     */
    @Test
    public void getNodeByNameTest1(){
        NodeVO nodeVO = nodeService.getNodeByName("新型冠状病毒肺炎");
        Assert.assertEquals("新型冠状病毒肺炎",nodeVO.getProperties().get("name"));
    }

    /**
     * 测试根据name获取节点——失败，该name不存在
     */
    @Test
    public void getNodeByNameTest2(){
        Assert.assertNull(nodeService.getNodeByName("我是乱码"));
    }

    /**
     * 测试获取层级目录——从数据库中读取，成功
     */
    @Test
    public void getNodesListTest1(){
        ResponseVO responseVO = nodeService.getNodesList();
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试获取层级目录——已经读取过了，成功
     */
    @Test
    public void getNodesListTest2(){
        ResponseVO responseVO = nodeService.getNodesList();
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 根据label获取节点列表——label为"科室"的节点共18个，成功
     * 此测试测试出来：代码第一次取searchNodes时，直接返回null，发现bug
     */
    @Test
    public void getNodesByLabelTest1(){
        List<NodeVO> nodes = nodeService.getNodesByLabel("科室");
        Assert.assertEquals(18,nodes.size());
    }

    /**
     * 根据label获取节点列表——label为"我是乱码"的节点不存在，失败
     */
    @Test
    public void getNodesByLabelTest2(){
        List<NodeVO> nodes = nodeService.getNodesByLabel("我是乱码");
        Assert.assertEquals(0,nodes.size());
    }

    /**
     * 测试获取全局搜索的节点列表——第一次从数据库读取，成功
     */
    @Test
    public void getSearchNodesTest1(){
        ResponseVO responseVO = nodeService.getSearchNodes();
        Assert.assertTrue(responseVO.getSuccess());
    }

    /**
     * 测试获取全局搜索的节点列表——第二次直接从缓存读取，成功
     */
    @Test
    public void getSearchNodesTest2(){
        ResponseVO responseVO = nodeService.getSearchNodes();
        Assert.assertTrue(responseVO.getSuccess());
    }


}
