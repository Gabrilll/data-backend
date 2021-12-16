package com.judicature.databackend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AnswerTest {

    Answer answer = new Answer();

    @BeforeEach
    void setUp() {
        answer.setType(null);
        answer.setContent(null);
    }

    @Test
    void getType() {
        answer.getType();
    }

    @Test
    void setType() {
        answer.setType(null);
    }

    @Test
    void getContent() {
        answer.getContent();
    }

    @Test
    void setContent() {
        answer.setContent(null);
    }
}