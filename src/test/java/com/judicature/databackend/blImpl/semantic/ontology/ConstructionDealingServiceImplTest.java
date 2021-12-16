package com.judicature.databackend.blImpl.semantic.ontology;

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
public class ConstructionDealingServiceImplTest {
    @Autowired
    ConstructionDealingServiceImpl constructionDealingService;

    @Test
    public void dealPropertyTest() throws Exception{

    }
}
