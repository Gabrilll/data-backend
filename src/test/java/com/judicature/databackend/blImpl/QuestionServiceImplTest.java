package com.judicature.databackend.blImpl;

import com.judicature.databackend.bl.QuestionService;
import com.judicature.databackend.vo.QAndR;
import com.judicature.databackend.vo.ResponseVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 覆盖率：92%
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceImplTest {
    @Autowired
    QuestionService questionService;

    /**
     * 测试回答问题——"新冠，整体预防措施"，成功
     */
    @Test
    public void answerTest1() throws Exception {
        QAndR qAndR = questionService.answer("新冠，整体预防措施");
        Assert.assertNotNull(qAndR);
    }
//
//    /**
//     * 测试问题推荐——新冠，整体预防措施——成功
//     */
//    @Test
//    public void recommendQueTest3() throws Exception{
//        questionService.answer("新冠，整体预防措施");
//        ResponseVO responseVO = questionService.recommendQue("新冠，整体预防措施");
//        Assert.assertTrue(responseVO.getSuccess());
//    }

    /**
     * 测试问答——查询疾病科室:慢性鼻窦炎的就诊科室为——耳鼻咽喉头颈外科，成功
     */
    @Test
    public void answerTest2() throws Exception{
        QAndR qAndR = questionService.answer("得了慢性鼻窦炎，应该去什么科室就诊？");
        Assert.assertNotNull(qAndR);
    }

    /**
     * 测试问答——查询新冠科室:新型冠状病毒肺炎的就诊科室为——发热门诊，成功
     */
    @Test
    public void answerTest3() throws Exception{
        QAndR qAndR = questionService.answer("新冠的就诊科室");
        Assert.assertNotNull(qAndR);
    }

    /**
     * 测试问答：新冠的治疗手段
     */
    @Test
    public void answerTest4() throws Exception{
        QAndR qAndR= questionService.answer("新型冠状病毒肺炎的中医治疗");
        Assert.assertNotNull(qAndR);
    }

//    /**
//     * 测试问题推荐——"新冠肺炎的治疗手段有哪些？"——成功
//     */
//    @Test
//    public void recommendQueTest1() throws Exception {
//        questionService.answer("新冠肺炎的治疗手段有哪些？");
//        ResponseVO responseVO = questionService.recommendQue("新冠肺炎的治疗手段有哪些？");
//        Assert.assertTrue(responseVO.getSuccess());
//    }

    /**
     * 测试问答：肺部感染的发病机制是什么?——成功
     */
    @Test
    public void answerTest5() throws Exception {
        QAndR qAndR = questionService.answer("肺部感染的发病机制是什么?");
        Assert.assertNotNull(qAndR);
    }

    /**
     * 测试问答——新冠期间，孩子应该如何自我防护？——成功
     */
    @Test
    public void answerTest6() throws Exception{
        QAndR qAndR = questionService.answer("新冠期间，孩子应该如何自我防护？");
        Assert.assertNotNull(qAndR);
    }

//    /**
//     * 测试问题推荐——新冠期间，孩子应该如何自我防护？——成功
//     */
//    @Test
//    public void recommendQueTest2() throws Exception{
//        questionService.answer("新冠期间，孩子应该如何自我防护？");
//        ResponseVO responseVO = questionService.recommendQue("新冠期间，孩子应该如何自我防护？");
//        Assert.assertTrue(responseVO.getSuccess());
//    }

    /**
     * 测试问答——新冠该如何预防？——成功
     * 此测试查出bug：多次问答，存储的最新一次relatedNode需要修改
     */
    @Test
    public void answerTest7() throws Exception{
        QAndR qAndR = questionService.answer("新冠该如何预防？");
        Assert.assertNotNull(qAndR);
    }

    /**
     * 测试问答——新冠期间，在地铁上应该如何自我防护？
     */
    @Test
    public void answerTest8() throws Exception{
        QAndR qAndR = questionService.answer("新冠期间，在地铁上应该如何自我防护？");
        Assert.assertNotNull(qAndR);
    }

//    /**
//     * 测试问题推荐——新冠期间，在地铁上应该如何自我防护？
//     */
//    @Test
//    public void recommendQueTest4() throws Exception{
//        questionService.answer("新冠期间，在地铁上应该如何自我防护？");
//        ResponseVO responseVO = questionService.recommendQue("新冠期间，在地铁上应该如何自我防护？");
//        Assert.assertTrue(responseVO.getSuccess());
//    }

    /**
     * 测试问答——问题主体不在数据库中——失败
     */
    @Test
    public void answerTest9() throws Exception{
        QAndR qAndR = questionService.answer("哈哈哈我不在数据库");
        Assert.assertNotNull(qAndR);
    }

    /**
     * 测试问答——问题属性不在数据库——失败
     */
    @Test
    public void answerTest10() throws Exception{
        QAndR qAndR = questionService.answer("新冠的定义");
        Assert.assertNotNull(qAndR);
    }

//    /**
//     * 测试问题推荐——relatedNode为空
//     */
//    public void recommendQueTest5() throws Exception{
//        ResponseVO responseVO = questionService.recommendQue("");
//        Assert.assertEquals("快提些问题吧~",responseVO.getContent());
//    }

    /**
     * 测试问答——3M-8210可以防护新冠吗？
     * @throws Exception
     */
    @Test
    public void answerTest11() throws Exception{
        QAndR qAndR = questionService.answer("3M-8210可以防护新冠吗？");
        Assert.assertNotNull(qAndR);
    }

    /**
     * 测试问答——新型肺炎的易感人群
     */
    @Test
    public void answerTest12() throws Exception{
        QAndR qAndR = questionService.answer("新型肺炎的易感人群");
        Assert.assertNotNull(qAndR);
    }

    /**
     * 测试问答——白喉的易感人群
     * @throws Exception
     */
    @Test
    public void answerTest13() throws Exception{
        QAndR qAndR = questionService.answer("白喉的易感人群");
        Assert.assertNotNull(qAndR);
    }

    @Test
    public void semanticSearchTest1() throws Exception {
        ResponseVO responseVO = questionService.semanticSearch("新冠肺炎的治疗手段有哪些?");
    }
    @Test
    public void semanticSearchTest2() throws Exception {
        ResponseVO responseVO = questionService.semanticSearch("新冠肺炎是什么？");
    }

    @Test
    public void answerTest14() throws Exception{
        QAndR qAndR = questionService.answer("你好");
        Assert.assertNotNull(qAndR);
    }

    @Test
    public void answerTest15() throws Exception{
        QAndR qAndR = questionService.answer("新型，整体防护措施");
        Assert.assertNotNull(qAndR);
    }

}
