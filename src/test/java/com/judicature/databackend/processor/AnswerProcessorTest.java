package com.judicature.databackend.processor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnswerProcessorTest {
    /**
     * 测试问句分析——手足口病的定义
     */
    @Test
    public void analysisTest1() throws Exception {
        AnswerProcessor answerProcessor = new AnswerProcessor();
        List<String> res = answerProcessor.analysis("手足口病的定义");
        Assert.assertEquals("0",res.get(0));
    }

    /**
     * 测试问句分析——新冠期间，孩子应该如何进行自我防护？
     */
    @Test
    public void analysisTest2() throws Exception {
        AnswerProcessor answerProcessor = new AnswerProcessor();
        List<String> res = answerProcessor.analysis("新冠期间，孩子应该如何进行自我防护？");
        Assert.assertEquals("9",res.get(0));
    }

}
