package com.judicature.databackend.vo.semantic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShortAnswerVOTest {
    ShortAnswerVO shortAnswerVO = new ShortAnswerVO();

    @BeforeEach
    public void setup(){
        shortAnswerVO.setPolysemantSituationVOs(null);
    }
    @Test
    void setPolysemantSituationVOs() {
        PolysemantSituationVO p = new PolysemantSituationVO();
        List<PolysemantSituationVO> arr = new ArrayList<>(Arrays.asList(p));
        shortAnswerVO.setPolysemantSituationVOs(arr);
    }

    @Test
    void getPolysemantSituationVOs() {
        List<PolysemantSituationVO> res = shortAnswerVO.getPolysemantSituationVOs();
    }

    @Test
    void testToString() {
        String s = shortAnswerVO.toString();
    }
}