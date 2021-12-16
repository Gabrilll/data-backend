package com.judicature.databackend.blImpl.semantic.ontology;

import com.judicature.databackend.model.BaikePage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class DrugConstructionServiceImplTest {
    @Autowired
    DrugConstructionServiceImpl drugConstructionService;

    /**
     * 利巴韦林
     * @throws Exception
     */
    @Test
    public void constructionTest1() throws Exception{
        BaikePage baikePage = new BaikePage();
        baikePage.setUrl("https://baike.baidu.com/item/%E5%88%A9%E5%B7%B4%E9%9F%A6%E6%9E%97");
        baikePage.setPicSrc("https://baike.baidu.com/pic/%E5%88%A9%E5%B7%B4%E9%9F%A6%E6%9E%97/1956513/1/0d338744ebf81a4c510ffc62d9607759252dd42a084c?fr=lemma&ct=single#aid=1&pic=0d338744ebf81a4c510ffc62d9607759252dd42a084c");
        baikePage.setLemmaSummary("利巴韦林（Ribavirin），化学名为1-β-D-呋喃核糖基-1H-1,2,4-三氮唑-3-羧酰胺，分子式为C8H12N4O5，为抗非逆转录病毒药。");
        baikePage.setLemmaTitle("利巴韦林");
        List<String> poly = new ArrayList<>(Arrays.asList("利巴韦林"));
        baikePage.setPolysemants(poly);
        baikePage.setPolysemantExplain("利巴韦林（Ribavirin），化学名为1-β-D-呋喃核糖基-1H-1,2,4-三氮唑-3-羧酰胺，分子式为C8H12N4O5，为抗非逆转录病毒药。");
        List<String> alias = new ArrayList<>(Arrays.asList("病毒唑","三氮唑核苷","1-β-D-呋喃核糖基-1H-1,2,4-三氮唑-3-羧酰胺"));
        baikePage.setAlias(alias);
        List<String> cata = new ArrayList<>(Arrays.asList("化合物简介","药典标准","临床用药须知","禁忌证","药物相互作用"));
        baikePage.setCatalogue(cata);
        baikePage.setParameterHasUrl(new ArrayList<>(Arrays.asList("https://baike.baidu.com/item/%E7%A6%81%E5%BF%8C%E8%AF%81")));
        baikePage.setParameterHasUrlValues(Arrays.asList("禁忌证"));
        baikePage.setParameterNames(new ArrayList<>(Arrays.asList("禁忌证")));
        baikePage.setParameterValues(new ArrayList<>(Arrays.asList("对本品过敏者禁用;孕妇及其男性伴侣禁用;血红蛋白病患者禁用;自身免疫性肝炎患者禁忌利巴韦林与干扰素α2b合用。")));
//        baikePage.setRelationNames(new ArrayList<>(Arrays.asList("科室")));
//        List<String> res = new ArrayList<>(Arrays.asList("https://baike.baidu.com/item/%E7%A7%91%E5%AE%A4/1283770?fr=aladdin"));
//        baikePage.setRelationUrls(res);
//        baikePage.setRelationValues(new ArrayList<>(Arrays.asList("发热门诊")));
        boolean out = drugConstructionService.construction(baikePage);
        Assert.assertFalse(out);

    }
}
