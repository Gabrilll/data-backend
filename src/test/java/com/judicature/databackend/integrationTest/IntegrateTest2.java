package com.judicature.databackend.integrationTest;

import com.judicature.databackend.controller.GraphController;
import com.judicature.databackend.controller.HistoryController;
import com.judicature.databackend.controller.NodeController;
import com.judicature.databackend.controller.RelationController;
import com.judicature.databackend.vo.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@SpringBootTest
@RunWith(SpringRunner.class)
public class IntegrateTest2 {
    @Autowired
    GraphController graphController;
    @Autowired
    NodeController nodeController;
    @Autowired
    RelationController relationController;
    @Autowired
    HistoryController historyController;

    // 测试对图谱进行修改后查询历史操作
    @Test
    public void testAddNodeAndSearchHistory(){
        NodeVO nodeVO = new NodeVO();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("hhh");
        nodeVO.setLabels(labels);
        Map<String,String> map = new HashMap<>();
        map.put("name","sds");
        nodeVO.setProperties(map);
        GraphVO graphVO = new GraphVO();
        List<NodeVO> nodes = new ArrayList<>();
        List<RelationVO> relationVOS = new ArrayList<>();
        graphVO.setNodes(nodes);
        graphVO.setEdges(relationVOS);
        Graph.setInstance(graphVO);
        ResponseVO responseVO = nodeController.addNode(nodeVO,0);
        Assert.assertEquals(true,responseVO.getSuccess());

        ResponseVO responseVO1 = historyController.getHistory();
        Assert.assertEquals(true,responseVO1.getSuccess());
    }

    // 测试打开图谱后展示统计数据
    @Test
    public void testGetAndStatistics(){
        ResponseVO responseVO1 = graphController.getGraph();
        ResponseVO responseVO2 = graphController.getStatistics();

        Assert.assertEquals(true,responseVO1.getSuccess());
        Assert.assertEquals(true,responseVO2.getSuccess());
    }

    // 测试打开图谱根据节点标签过滤数据
    @Test
    public void testFilterLabels(){
        ResponseVO responseVO1 = graphController.getGraph();
        List<String> labels = new ArrayList<>();
        labels.add("test");
        FilterLabelsVO filterLabelsVO = new FilterLabelsVO();
        filterLabelsVO.setLabels(labels);
        ResponseVO responseVO2 = graphController.filterByNodeLabels(filterLabelsVO,0);

        Assert.assertEquals(true,responseVO1.getSuccess());
        Assert.assertEquals(true,responseVO2.getSuccess());
    }


}
