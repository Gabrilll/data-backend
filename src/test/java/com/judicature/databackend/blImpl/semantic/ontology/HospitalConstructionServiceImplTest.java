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
public class HospitalConstructionServiceImplTest {
    @Autowired
    HospitalConstructionServiceImpl hospitalConstructionService;

    /**
     * 方舱医院
     * @throws Exception
     */
    @Test
    public void constructionTest() throws Exception{
        BaikePage baikePage = new BaikePage();
        baikePage.setUrl("https://baike.baidu.com/item/%E6%96%B9%E8%88%B1%E5%8C%BB%E9%99%A2");
        baikePage.setPicSrc("https://baike.baidu.com/pic/%E6%96%B9%E8%88%B1%E5%8C%BB%E9%99%A2/341329/1/1c950a7b02087bf40ad13e965c9e402c11dfa9ec6134?fr=lemma&ct=single#aid=1&pic=1c950a7b02087bf40ad13e965c9e402c11dfa9ec6134");
        baikePage.setLemmaSummary("方舱医院是以医疗方舱为载体，医疗与医技保障功能综合集成的可快速部署的成套野外移动医疗平台");
        baikePage.setLemmaTitle("方舱医院");
        List<String> poly = new ArrayList<>();
        baikePage.setPolysemants(poly);
        baikePage.setPolysemantExplain("");
        List<String> alias = new ArrayList<>();
        baikePage.setAlias(alias);
        List<String> cata = new ArrayList<>(Arrays.asList("词语定义","基本概况","发展概况","基本组成","主要功能","医疗方舱","优缺点","实际应用","社会评论"));
        baikePage.setCatalogue(cata);
        List<String> param = new ArrayList<>();
        baikePage.setParameterHasUrl(param);
        baikePage.setParameterHasUrlValues(Arrays.asList(""));
        baikePage.setParameterNames(new ArrayList<>(Arrays.asList("")));
        baikePage.setParameterValues(new ArrayList<>(Arrays.asList("")));

        boolean out = hospitalConstructionService.construction(baikePage);
        Assert.assertFalse(out);
    }
}
