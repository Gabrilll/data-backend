package com.judicature.databackend.vo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ResponseVOTest {
    @Test
    public void TestResponseVO() throws Exception{
        ResponseVO responseVO = new ResponseVO();
        responseVO.setMessage("oh,my god!");
        responseVO.setSuccess(true);
        Object object = new Object();
        responseVO.setContent(object);

        responseVO.getContent();
        responseVO.getMessage();
        responseVO.getSuccess();

        ResponseVO responseVO1 = new ResponseVO();
        responseVO1.buildSuccess();
        ResponseVO responseVO2 = new ResponseVO();
        responseVO2.buildFailure("fail!");
        ResponseVO responseVO3 = new ResponseVO();
        responseVO3.buildSuccess(object);
    }
}
