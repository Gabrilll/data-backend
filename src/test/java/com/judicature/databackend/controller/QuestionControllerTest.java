package com.judicature.databackend.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@RunWith(SpringRunner.class)
public class QuestionControllerTest {
    MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    QuestionController questionController;

    @Before()
    public void setup()    {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }

    @Test
    public void getAnswerTest() throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/question/answerQuestion")
                .param("question","手足口病")).andReturn();
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
    public void getRecommendTest() throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/question/recommendQue")
                .param("question","手足口病")).andReturn();
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
    public void semanticSearchTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/question/semanticSearch")
                .param("question","手足口病是什么？")).andReturn();
//                .accept(MediaType.parseMediaType("application/json;charset=utf-8"))
//                .contentType(MediaType.APPLICATION_JSON)
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();

        //断言，判断返回编码是否正确
        Assert.assertEquals(200,status);
    }
}
