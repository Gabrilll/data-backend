package com.judicature.databackend.util;

import com.judicature.databackend.vo.GraphList;
import com.judicature.databackend.vo.GraphVO;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GraphListTest {

    @Test
    public void testAddGraph(){
        List<GraphVO> graphs= GraphList.getGraphList();
        GraphVO g=new GraphVO();
        GraphList.addGraph(g);
        Assert.assertEquals(graphs.size(),GraphList.getGraphList().size());
    }
}
