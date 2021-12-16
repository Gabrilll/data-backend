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
public class OthersConstructionServiceImplTest {
    @Autowired
    OthersConstructionServiceImpl othersConstructionService;

    /**
     * 世界卫生组织
     * @throws Exception
     */
    @Test
    public void constructionTest() throws Exception{
        BaikePage baikePage = new BaikePage();
        baikePage.setUrl("https://baike.baidu.com/item/%E4%B8%96%E7%95%8C%E5%8D%AB%E7%94%9F%E7%BB%84%E7%BB%87");
        baikePage.setPicSrc("https://baike.baidu.com/pic/%E4%B8%96%E7%95%8C%E5%8D%AB%E7%94%9F%E7%BB%84%E7%BB%87/483426/1/b812c8fcc3cec3fd4b549723d988d43f87942779?fr=lemma&ct=single#aid=1&pic=b812c8fcc3cec3fd4b549723d988d43f87942779");
        baikePage.setLemmaSummary("世界卫生组织（英文名称：World Health Organization，缩写WHO，中文简称世卫组织）是联合国下属的一个专门机构，总部设置在瑞士日内瓦，只有主权国家才能参加，是国际上最大的政府间卫生组织");
        baikePage.setLemmaTitle("世界卫生组织");
        List<String> poly = new ArrayList<>();
        baikePage.setPolysemants(poly);
        baikePage.setPolysemantExplain("");
        List<String> alias = new ArrayList<>();
        baikePage.setAlias(alias);
        List<String> cata = new ArrayList<>(Arrays.asList("历史","宗旨","会徽","世卫目标","组织机构","成员国","组织任务","历任总干事","经费来源","出版物","与中国关系"));
        baikePage.setCatalogue(cata);
        List<String> param = new ArrayList<>();
        baikePage.setParameterHasUrl(param);
        baikePage.setParameterHasUrlValues(Arrays.asList(""));
        baikePage.setParameterNames(new ArrayList<>(Arrays.asList("")));
        baikePage.setParameterValues(new ArrayList<>(Arrays.asList("")));

        boolean out = othersConstructionService.construction(baikePage);
        Assert.assertFalse(out);
    }
}
