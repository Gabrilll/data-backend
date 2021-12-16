package com.judicature.databackend.vo.semantic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ArgTest {


    Arg arg = new Arg();

    @BeforeEach
    public void setup(){
        arg.setEnd(1);
        arg.setBeg(0);
        arg.setType(null);
        arg.setLength(0);
        arg.setId(0);
    }
    @Test
    void getId() {
        arg.getId();
    }

    @Test
    void setId() {
        arg.setId(1);
    }

    @Test
    void getLength() {
        arg.getLength();
    }

    @Test
    void setLength() {
        arg.setLength(1);
    }

    @Test
    void getType() {
        arg.getType();
    }

    @Test
    void setType() {
        arg.setType("1");
    }

    @Test
    void getBeg() {
        arg.getBeg();
    }

    @Test
    void setBeg() {
        arg.setBeg(1);
    }

    @Test
    void getEnd() {
        arg.getEnd();
    }

    @Test
    void setEnd() {
        arg.setEnd(1);
    }
}