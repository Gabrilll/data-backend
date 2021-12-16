package com.judicature.databackend.controller;

import com.judicature.databackend.vo.FilterLabelsVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GraphControllerTest {
    MockMvc mockMvc;

    @Autowired
    GraphController graphController;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetGraph() throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/getGraph")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
//                .accept(MediaType.parseMediaType("application/json;charset=utf-8"))
//                .contentType(MediaType.APPLICATION_JSON)
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);

    }

    @Test
    public void testExportXml() throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/exportXml")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);

    }

    @Test
    public void testFilterByNodeLabels() throws Exception{
        FilterLabelsVO labels = new FilterLabelsVO();
        List<String> labelsList = new ArrayList<>();
        labelsList.add("症状");
        labels.setLabels(labelsList);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(labels);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/graph/filterByNodeLabels")
                .param("graphId","0")
                .content(json).contentType(MediaType.APPLICATION_JSON)).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);

    }

    @Test
    public void testUploadFile() throws Exception{
        File file = new File("src/main/resources/testFile2.json");
        String json = FileUtils.readFileToString(file);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/graph/updateFile")
                .content("{}").contentType(MediaType.APPLICATION_JSON)).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();



    }

    @Test
    public void testGetStatistics() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/getStatistics")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }

    //新增测试如下：

    @Test
    public void getGraphListTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/getGraphList")).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }

    @Test
    public void getGraphByIdTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/getGraphById")
                .param("id","0")).andReturn();
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }

    @Test
    public void getGraphNumTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/getGraphNum")).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }

    @Test
    public void removeGraphTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/removeGraph")
                .param("id","0")).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }

    @Test
    public void getGraphByNodeTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/getGraphByNode")
                .param("id","0")).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }

    @Test
    public void constructGraphTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/constructGraph")
                .param("startUrl","https://baike.baidu.com/item/%E5%88%98%E5%BE%B7%E5%8D%8E/114923?fr=aladdin")
                .param("id","3")).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

//        //断言，判断返回编码是否正确
//        Assert.assertEquals(200,status);
    }

    @Test
    public void getConstructionDetailTest() throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/getConstructionDetail")).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }

    @Test
    public void stopConstructionTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/graph/stopConstruction")).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }

//    @Test
//    public void getLabelsByGraphIdTest() throws Exception {
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
//                .get("/api/graph/getLabelsByGraphId")
//                .param("graphId","0")).andReturn();
//
//        //获取返回编码
//        int status = mvcResult.getResponse().getStatus();
//
//        //断言，判断返回编码是否正确
//        Assert.assertEquals(200,status);
//    }
}
