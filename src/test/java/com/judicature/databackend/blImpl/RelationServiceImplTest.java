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
 * 覆盖率 85%
 */
//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class RelationServiceImplTest {
    @Autowired
    RelationServiceImpl relationService;

    /**
     * 测试添加关系
     */
    @Test
    public void addRelationTest1(){
        RelationVO relationVO = new RelationVO();
        relationVO.setType("testRe");
        relationVO.setStart(53860L);
        relationVO.setEnd(53866L);
        Map<String,String> map=new HashMap<>();
        map.put("name","hhh");
        relationVO.setProperties(map);

        GraphVO graphVO = new GraphVO();
        NodeVO node1 = new NodeVO();
        node1.setIdentity(53860L);
        NodeVO node2 = new NodeVO();
        node2.setIdentity(53866L);
        List<NodeVO> nodes = new ArrayList<>(Arrays.asList(node1,node2));
        List<RelationVO> res = new ArrayList<>();
        graphVO.setEdges(res);
        System.out.println(GraphList.getGraphNum());
        GraphList.addGraph(graphVO);

        ResponseVO responseVO=relationService.addRelation(relationVO,0);
        Assert.assertTrue(responseVO.getSuccess());

    }

    //添加关系失败
    @Test
    public void addRelationTest2(){
        RelationVO relationVO = new RelationVO();
        relationVO.setType("testRe");
        relationVO.setStart(0L);
        relationVO.setEnd(-1L);
        Map<String,String> map=new HashMap<>();
        map.put("name","hhh");
        relationVO.setProperties(map);

        GraphVO graphVO = new GraphVO();
        GraphList.addGraph(graphVO);

        ResponseVO responseVO=relationService.addRelation(relationVO,0);
        Assert.assertFalse(responseVO.getSuccess());

    }

    //成功删除关系
    @Test
    public void deleteRelationTest1(){
        GraphVO graphVO = new GraphVO();
        RelationVO relationVO = new RelationVO();
        relationVO.setIdentity(6263L);
        List<RelationVO> relationVOS = new ArrayList<>(Arrays.asList(relationVO));
        graphVO.setEdges(relationVOS);
        GraphList.addGraph(graphVO);

        Long id = 6263L;
        ResponseVO responseVO = relationService.deleteRelation(id,0);
        Assert.assertTrue(responseVO.getSuccess());
    }

    //删除关系失败
    @Test
    public void deleteRelationTest2(){
        GraphVO graphVO = new GraphVO();
        RelationVO relationVO = new RelationVO();
        relationVO.setIdentity(6263L);
        List<RelationVO> res = new ArrayList<>(Arrays.asList(relationVO));
        graphVO.setEdges(res);
        GraphList.addGraph(graphVO);

        ResponseVO responseVO = relationService.deleteRelation(-1L,0);
        Assert.assertFalse(responseVO.getSuccess());
    }

    @Test
    public void updateRelationTest1(){
        GraphVO graphVO = new GraphVO();
        RelationVO relation = new RelationVO();
        relation.setIdentity(6263L);
        relation.setStart(53285L);
        relation.setEnd(53258L);
        relation.setType("subClassOf");
        List<RelationVO> res = new ArrayList<>(Arrays.asList(relation));
        graphVO.setEdges(res);
        GraphList.addGraph(graphVO);

        RelationVO relationVO = new RelationVO();
        relationVO.setIdentity(6263L);
        relationVO.setStart(53285L);
        relationVO.setEnd(53258L);
        relationVO.setType("newRe");
        Map<String,String> map=new HashMap<>();
        map.put("name","hhh");
        relationVO.setProperties(map);
        ResponseVO responseVO = relationService.updateRelation(relationVO,0);
        Assert.assertTrue(responseVO.getSuccess());
    }

    @Test
    public void updateRelationTest2(){
        RelationVO relationVO = new RelationVO();
        ResponseVO responseVO = relationService.updateRelation(relationVO,0);
        Assert.assertFalse(responseVO.getSuccess());
    }

    @Test
    public void getAllReTest1(){
        List<RelationVO> re = relationService.getAllRe();
        Assert.assertNotEquals(0,re.size());
    }

    @Test
    public void getSubReTest1() {
        List<Long> ids = new ArrayList<>();
        ids.add(3L);
        List<RelationVO> re = relationService.getSubRe(ids);
        Assert.assertEquals(0,re.size());
    }

    @Test
    public void getSubReTest2() {
        List<Long> ids = new ArrayList<>();
        List<RelationVO> re = relationService.getSubRe(ids);
        Assert.assertEquals(0,re.size());
    }

}
