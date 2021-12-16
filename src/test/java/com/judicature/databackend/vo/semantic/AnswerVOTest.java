package com.judicature.databackend.vo.semantic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AnswerVOTest {

    AnswerVO answerVO = new AnswerVO();

    @BeforeEach
    public void setup(){
        answerVO.setShortAnswer(null);
        answerVO.setAnswerTime(1L);
        answerVO.setWords(null);
        answerVO.setQuestion(null);
        answerVO.setAskTime(1L);
        answerVO.setKnowledgeGraphVos(null);
    }
    @Test
    void getAskTime() {
        answerVO.getAskTime();
    }

    @Test
    void setAskTime() {
        answerVO.setAskTime(1L);
    }

    @Test
    void getAnswerTime() {
        answerVO.getAnswerTime();
    }

    @Test
    void setAnswerTime() {
        answerVO.setAnswerTime(1L);
    }

    @Test
    void getQuestion() {
        answerVO.getQuestion();
    }

    @Test
    void setQuestion() {
        answerVO.setQuestion("你好");
    }

    @Test
    void getWords() {
        answerVO.getWords();
    }

    @Test
    void setWords() {
        answerVO.setWords(null);
    }

    @Test
    void getShortAnswer() {
        answerVO.getShortAnswer();
    }

    @Test
    void setShortAnswer() {
        answerVO.setShortAnswer(null);
    }

    @Test
    void getKnowledgeGraphVos() {
        answerVO.getKnowledgeGraphVos();
    }

    @Test
    void setKnowledgeGraphVos() {
        answerVO.setShortAnswer(null);
    }

    @Test
    void testToString() {
        answerVO.toString();
    }
}