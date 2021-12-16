package com.judicature.databackend.blImpl.semantic;

import com.judicature.databackend.bl.semantic.NamedEntityService;
import com.judicature.databackend.model.PolysemantNamedEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class NamedEntityServiceImplTest {
    @Autowired
    NamedEntityServiceImpl namedEntityService;

    @Test
    public void fillNamedEntitiesTest() throws Exception{
        List<PolysemantNamedEntity> polysemantNamedEntities = new ArrayList<>();
        PolysemantNamedEntity p = new PolysemantNamedEntity();
        p.setUUID("f1c1e75f1e994f5f8d44de5600c68e94");
        p.setEntityName("目标飞行器");
        p.setEntityUrl("https://baike.baidu.com/item/%E7%9B%AE%E6%A0%87%E9%A3%9E%E8%A1%8C%E5%99%A8/6814032");
        p.setPicSrc("1623992822353390.jpg");
        p.setLemmaSummary("目标飞行器是指2011年9月29日晚21时16分许，中国在酒泉卫星发射中心载人航天发射场发射的飞行器，用“长征二号F”T1运载火箭，将中国全新研制的首个目标飞行器“天宫一号”发射升空。 [1] ");
        Map<String,String> pro = new HashMap<>();
        pro.put("发射地","酒泉卫星发射中心");
        p.setDataProperties(pro);
        polysemantNamedEntities.add(p);

        boolean res = namedEntityService.fillNamedEntities(polysemantNamedEntities);
        Assert.assertTrue(res);
    }

    @Test
    public void getInstanceTest() throws Exception{
        NamedEntityService singleInstance = NamedEntityServiceImpl.getInstance();
    }
}