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
public class LivingConstructionServiceImplTest {
    @Autowired
    LivingConstructionServiceImpl livingConstructionService;

    @Test
    public void constructionTest() throws Exception{
        BaikePage baikePage = new BaikePage();
        baikePage.setUrl("https://baike.baidu.com/item/%E7%8C%AB/22261?fr=aladdin");
        baikePage.setPicSrc("https://baike.baidu.com/pic/%E7%8C%AB/22261/1/0eb30f2442a7d933c94b0ca9a64bd11372f0010c?fr=lemma&ct=single#aid=1&pic=0eb30f2442a7d933c94b0ca9a64bd11372f0010c");
        baikePage.setLemmaSummary("猫，属于猫科动物，分家猫、野猫，是全世界家庭中较为广泛的宠物。家猫的祖先据推测是古埃及的沙漠猫，波斯的波斯猫，已经被人类驯化了3500年（但未像狗一样完全地被驯化）。");
        baikePage.setLemmaTitle("猫（猫科猫属动物）");
        List<String> poly = new ArrayList<>();
        baikePage.setPolysemants(poly);
        baikePage.setPolysemantExplain("");
        List<String> alias = new ArrayList<>();
        baikePage.setAlias(alias);
        List<String> cata = new ArrayList<>(Arrays.asList("物种始源","形态特征","生活习性","驯养方法","主要品种","注意事项"));
        baikePage.setCatalogue(cata);
        List<String> param = new ArrayList<>();
        baikePage.setParameterHasUrl(param);
        baikePage.setParameterHasUrlValues(Arrays.asList(""));
        baikePage.setParameterNames(new ArrayList<>(Arrays.asList("")));
        baikePage.setParameterValues(new ArrayList<>(Arrays.asList("")));

        boolean out = livingConstructionService.construction(baikePage);
        Assert.assertFalse(out);
    }
}
