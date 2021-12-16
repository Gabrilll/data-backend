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
public class DiseaseConstructionServiceImplTest {
    @Autowired
    DiseaseConstructionServiceImpl diseaseConstructionService;

    /**
     * 第一次构建，词典中没有该实体，创建后加入
     * @throws Exception
     */
    @Test
    public void constructionTest1() throws Exception {
        BaikePage baikePage = new BaikePage();
        baikePage.setUrl("https://baike.baidu.com/item/2019%E6%96%B0%E5%9E%8B%E5%86%A0%E7%8A%B6%E7%97%85%E6%AF%92/24267858?fr=aladdin");
        baikePage.setPicSrc("https://baike.baidu.com/pic/2019%E6%96%B0%E5%9E%8B%E5%86%A0%E7%8A%B6%E7%97%85%E6%AF%92/24267858/0/cdbf6c81800a19d8bc3ec24013b0958ba61ea8d3e816?fr=lemma&ct=single#aid=0&pic=cdbf6c81800a19d8bc3ec24013b0958ba61ea8d3e816");
        baikePage.setLemmaSummary("2019新型冠状病毒（2019-nCoV，世卫组织2020年1月命名 [1]  ；SARS-CoV-2，国际病毒分类委员会2020年2月11日命名");
        baikePage.setLemmaTitle("2019新型冠状病毒");
        List<String> poly = new ArrayList<>(Arrays.asList("2019新型冠状病毒"));
        baikePage.setPolysemants(poly);
        baikePage.setPolysemantExplain("新型冠状病毒是以前从未在人体中发现的冠状病毒新毒株。");
        List<String> alias = new ArrayList<>(Arrays.asList("新冠","新型肺炎","新型冠状肺炎","新型冠状病毒肺炎"));
        baikePage.setAlias(alias);
        List<String> cata = new ArrayList<>(Arrays.asList("发现经过","主要症状","病理解剖","传播途径","易感人群","诊断标准","研究处置","命名过程","疫情防控","预防方法","及时就诊","防护指南","捐助情况","社会影响","相关报道"));
        baikePage.setCatalogue(cata);
        baikePage.setParameterHasUrl(new ArrayList<>(Arrays.asList("https://baike.baidu.com/item/%E6%98%93%E6%84%9F%E4%BA%BA%E7%BE%A4/1310094?fr=aladdin")));
        baikePage.setParameterHasUrlValues(Arrays.asList("易感人群"));
        baikePage.setParameterNames(new ArrayList<>(Arrays.asList("易感人群")));
        baikePage.setParameterValues(new ArrayList<>(Arrays.asList("各个年龄段的人都可能被感染，被感染的主要是成年人，其中老年人和体弱多病的人似乎更容易被感染。儿童和孕产妇是新型冠状病毒感染的肺炎的易感人群")));
        baikePage.setRelationNames(new ArrayList<>(Arrays.asList("科室")));
        List<String> res = new ArrayList<>(Arrays.asList("https://baike.baidu.com/item/%E7%A7%91%E5%AE%A4/1283770?fr=aladdin"));
        baikePage.setRelationUrls(res);
        baikePage.setRelationValues(new ArrayList<>(Arrays.asList("发热门诊")));

        boolean out = diseaseConstructionService.construction(baikePage);
        Assert.assertTrue(out);
    }

    /**
     * 第二次构建，词典中已经有了，直接构建
     * @throws Exception
     */
    @Test
    public void constructionTest2() throws Exception {
        BaikePage baikePage = new BaikePage();
        baikePage.setUrl("https://baike.baidu.com/item/2019%E6%96%B0%E5%9E%8B%E5%86%A0%E7%8A%B6%E7%97%85%E6%AF%92/24267858?fr=aladdin");
        baikePage.setPicSrc("https://baike.baidu.com/pic/2019%E6%96%B0%E5%9E%8B%E5%86%A0%E7%8A%B6%E7%97%85%E6%AF%92/24267858/0/cdbf6c81800a19d8bc3ec24013b0958ba61ea8d3e816?fr=lemma&ct=single#aid=0&pic=cdbf6c81800a19d8bc3ec24013b0958ba61ea8d3e816");
        baikePage.setLemmaSummary("2019新型冠状病毒（2019-nCoV，世卫组织2020年1月命名 [1]  ；SARS-CoV-2，国际病毒分类委员会2020年2月11日命名");
        baikePage.setLemmaTitle("2019新型冠状病毒");
        List<String> poly = new ArrayList<>(Arrays.asList("2019新型冠状病毒"));
        baikePage.setPolysemants(poly);
        baikePage.setPolysemantExplain("新型冠状病毒是以前从未在人体中发现的冠状病毒新毒株。");
        List<String> alias = new ArrayList<>(Arrays.asList("新冠","新型肺炎","新型冠状肺炎","新型冠状病毒肺炎"));
        baikePage.setAlias(alias);
        List<String> cata = new ArrayList<>(Arrays.asList("发现经过","主要症状","病理解剖","传播途径","易感人群","诊断标准","研究处置","命名过程","疫情防控","预防方法","及时就诊","防护指南","捐助情况","社会影响","相关报道"));
        baikePage.setCatalogue(cata);
        baikePage.setParameterHasUrl(new ArrayList<>(Arrays.asList("https://baike.baidu.com/item/%E6%98%93%E6%84%9F%E4%BA%BA%E7%BE%A4/1310094?fr=aladdin")));
        baikePage.setParameterHasUrlValues(Arrays.asList("易感人群"));
        baikePage.setParameterNames(new ArrayList<>(Arrays.asList("易感人群")));
        baikePage.setParameterValues(new ArrayList<>(Arrays.asList("各个年龄段的人都可能被感染，被感染的主要是成年人，其中老年人和体弱多病的人似乎更容易被感染。儿童和孕产妇是新型冠状病毒感染的肺炎的易感人群")));
        baikePage.setRelationNames(new ArrayList<>(Arrays.asList("科室")));
        List<String> res = new ArrayList<>(Arrays.asList("https://baike.baidu.com/item/%E7%A7%91%E5%AE%A4/1283770?fr=aladdin"));
        baikePage.setRelationUrls(res);
        baikePage.setRelationValues(new ArrayList<>(Arrays.asList("发热门诊")));

        diseaseConstructionService.construction(baikePage);
    }
}
