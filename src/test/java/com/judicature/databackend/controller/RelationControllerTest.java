package com.judicature.databackend.controller;

import com.judicature.databackend.bl.RelationService;
import com.judicature.databackend.vo.RelationVO;
import com.judicature.databackend.vo.ResponseVO;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@RunWith(SpringRunner.class)
public class RelationControllerTest{
    MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    RelationController relationController;

    @Before()
    public void setup()    {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }


    @Test
    public void testAddRelation() throws Exception {
        RelationVO relationVO = new RelationVO();
        relationVO.setType("测试关系1");
        relationVO.setStart(1L);
        relationVO.setEnd(2L);
        Map<String,String> map=new HashMap<>();
        map.put("name","hhh");
        relationVO.setProperties(map);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(relationVO);
        //执行请求
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/relation/addRelation")
                .param("groupId","0")
                .content(json).contentType(MediaType.APPLICATION_JSON)).andReturn();
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();

        //断言，判断返回编码是否正确
        Assert.assertEquals(400,status);

    }

    @Test
    public void testDeleteRelation() throws Exception{
        RelationService relationService=mock(RelationService.class);
        when(relationService.deleteRelation(1L,0)).thenReturn(ResponseVO.buildSuccess(true));

        RelationController relationController=new RelationController(relationService);

        MockMvc mockMvc=standaloneSetup(relationController).build();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/relation/deleteRelation")
                        .param("groupId","0")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("identity","3")
//                .content("{\"id\":1}")
//                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                ).andReturn();

        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

//        //断言，判断返回编码是否正确
//        Assert.assertEquals(200,status);

    }

    @Test
    public void testUpdateRelation() throws Exception{
        RelationVO relationVO = new RelationVO();
        relationVO.setIdentity(2L);
        relationVO.setType("测试关系1");
        relationVO.setStart(1L);
        relationVO.setEnd(2L);

        RelationService relationService=mock(RelationService.class);
//        when(relationService.updateRelation(any(RelationVO.class), 0)).thenReturn(ResponseVO.buildSuccess(true));

        RelationController relationController=new RelationController(relationService);

        MockMvc mockMvc=standaloneSetup(relationController).build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(relationVO);
        //执行请求
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/relation/updateRelation")
                .param("groupId","0")
                .content(json).contentType(MediaType.APPLICATION_JSON)).andReturn();
        //获取返回编码
        int status = mvcResult.getResponse().getStatus();

        //获取返回结果
        String content = mvcResult.getResponse().getContentAsString();

        //断言，判断返回编码是否正确
        Assert.assertEquals(400,status);

    }


}
