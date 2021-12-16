package com.judicature.databackend.controller;

import com.judicature.databackend.vo.NodeVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NodeControllerTest {
    MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    NodeController nodeController;

    @Before()
    public void setup()    {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }


    @Test
    public void testAddNode() throws Exception {
        NodeVO nodeVO = new NodeVO();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("testAddNode");
        nodeVO.setLabels(labels);
        Map<String,String> map=new HashMap<>();
        map.put("name","testAddNode");
        nodeVO.setProperties(map);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(nodeVO);

        //执行请求
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/node/addNode")
                .param("graphId","0")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);

    }

    @Test
    public void testDeleteNode() throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/node/deleteNode")
                .param("graphId","0")
                .param("identity","1")
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
//                .get("/api/graph/deleteNode")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("identity","0L")
//        ).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();


    }

    @Test
    public void testUpdateNode() throws Exception {
        NodeVO nodeVO = new NodeVO();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("testAddNode");
        nodeVO.setLabels(labels);
        Map<String,String> map=new HashMap<>();
        map.put("name","testNewNode");
        nodeVO.setProperties(map);
        nodeVO.setIdentity(1L);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(nodeVO);
        //执行请求
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/node/updateNode")
                .param("graphId","0")
                .content(json).contentType(MediaType.APPLICATION_JSON)).andReturn();
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);

    }

    @Test
    public void getNodesListTest() throws Exception {
        //执行请求
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/node/getNodesList")).andReturn();
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }

    @Test
    public void getSearchNodesTest() throws Exception{
        //执行请求
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/node/getSearchNodes")).andReturn();
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }
}
