package com.judicature.databackend.blImpl.semantic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class WordSegmentationServiceImplTest {
    @Autowired
    WordSegmentationServiceImpl wordSegmentationService;

    @Test
    public void wordSegmentationTest() throws Exception{
        wordSegmentationService.wordSegmentation("新型肺炎的原理");
    }

    @Test
    public void fuzzyMatchTest() throws Exception{
        wordSegmentationService.fuzzyMatch("啥");
        wordSegmentationService.fuzzyMatch("新冠");
        wordSegmentationService.fuzzyMatch("新型冠状病毒肺炎");
        wordSegmentationService.fuzzyMatch("手足口病");
    }

    @Test
    public void getInstanceTest() throws Exception{
        WordSegmentationServiceImpl.getInstance();
    }
}
