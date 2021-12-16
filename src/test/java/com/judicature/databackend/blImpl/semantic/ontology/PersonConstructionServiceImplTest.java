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
public class PersonConstructionServiceImplTest {
    @Autowired
    PersonConstructionServiceImpl personConstructionService;

    /**
     * 钟南山
     * @throws Exception
     */
    @Test
    public void constructionTest() throws Exception{
        BaikePage baikePage = new BaikePage();
        baikePage.setUrl("https://baike.baidu.com/item/%E9%92%9F%E5%8D%97%E5%B1%B1/653914");
        baikePage.setPicSrc("https://baike.baidu.com/pic/%E9%92%9F%E5%8D%97%E5%B1%B1/653914/1/622762d0f703918fa0ecec3b6077319759ee3c6d6ea5?fr=lemma&ct=single#aid=1&pic=622762d0f703918fa0ecec3b6077319759ee3c6d6ea5");
        baikePage.setLemmaSummary("钟南山，男，汉族，中共党员，1936年10月20日 [1]  出生于江苏南京，福建厦门人，呼吸内科学家，广州医科大学附属第一医院国家呼吸系统疾病临床医学研究中心主任，中国工程院院士");
        baikePage.setLemmaTitle("钟南山");
        List<String> poly = new ArrayList<>();
        baikePage.setPolysemants(poly);
        baikePage.setPolysemantExplain("");
        List<String> alias = new ArrayList<>();
        baikePage.setAlias(alias);
        List<String> cata = new ArrayList<>(Arrays.asList("任务履历","主要成就","社会任职","任务生活","社会评价"));
        baikePage.setCatalogue(cata);
        List<String> param = new ArrayList<>();
        baikePage.setParameterHasUrl(param);
        baikePage.setParameterHasUrlValues(Arrays.asList(""));
        baikePage.setParameterNames(new ArrayList<>(Arrays.asList("")));
        baikePage.setParameterValues(new ArrayList<>(Arrays.asList("")));

        List<String> res = new ArrayList<>();
        res.add("李少芬");
        baikePage.setRelationValues(res);
        List<String> resName = new ArrayList<>(Arrays.asList("夫妻"));
        baikePage.setRelationNames(resName);
        baikePage.setRelationUrls(new ArrayList<>(Arrays.asList("https://baike.baidu.com/item/%E6%9D%8E%E5%B0%91%E8%8A%AC/6167921")));
        boolean out = personConstructionService.construction(baikePage);
        Assert.assertFalse(out);
    }
}
