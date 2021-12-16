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
public class DocumentConstructionServiceImplTest {
    @Autowired
    DocumentConstructionServiceImpl documentConstructionService;

    /**
     * 新型冠状病毒肺炎诊疗方案（试行第八版）
     * @throws Exception
     */
    @Test
    public void constructionTest1() throws Exception{
        BaikePage baikePage = new BaikePage();
        baikePage.setUrl("https://baike.baidu.com/item/%E6%96%B0%E5%9E%8B%E5%86%A0%E7%8A%B6%E7%97%85%E6%AF%92%E8%82%BA%E7%82%8E%E8%AF%8A%E7%96%97%E6%96%B9%E6%A1%88%EF%BC%88%E8%AF%95%E8%A1%8C%E7%AC%AC%E5%85%AB%E7%89%88%EF%BC%89/53291576");
        baikePage.setPicSrc("https://baike.baidu.com/pic/%E6%96%B0%E5%9E%8B%E5%86%A0%E7%8A%B6%E7%97%85%E6%AF%92%E8%82%BA%E7%82%8E%E8%AF%8A%E7%96%97%E6%96%B9%E6%A1%88%EF%BC%88%E8%AF%95%E8%A1%8C%E7%AC%AC%E5%85%AB%E7%89%88%EF%BC%89/53291576/1/6d81800a19d8bc3eb13581b760c2b11ea8d3fd1fef3e?fr=lemma&ct=single#aid=1&pic=6d81800a19d8bc3eb13581b760c2b11ea8d3fd1fef3e");
        baikePage.setLemmaSummary("《新型冠状病毒肺炎诊疗方案（试行第八版）》是为进一步做好新型冠状病毒肺炎医疗救治工作，组织专家在总结前期新冠肺炎诊疗经验和参考世界卫生组织及其他国家诊疗指南基础上，对诊疗方案进行修订。");
        baikePage.setLemmaTitle("新型冠状病毒肺炎诊疗方案（试行第八版）");
        List<String> poly = new ArrayList<>(Arrays.asList("世卫组织"));
        baikePage.setPolysemants(poly);
        baikePage.setPolysemantExplain("世界卫生组织是联合国下属的一个专门机构。");
        List<String> alias = new ArrayList<>(Arrays.asList("新冠诊疗方案"));
        baikePage.setAlias(alias);
        List<String> cata = new ArrayList<>(Arrays.asList("历史","宗旨","会徽","世卫目标","组织机构","成员国","组织任务","历任总干事","经费来源","出版物","与中国关系","与美国关系","六十周年"));
        baikePage.setCatalogue(cata);
        baikePage.setParameterHasUrl(new ArrayList<>(Arrays.asList("https://baike.baidu.com/item/%E4%B8%AD%E5%8D%8E%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E5%9B%BD%E5%AE%B6%E5%8D%AB%E7%94%9F%E5%81%A5%E5%BA%B7%E5%A7%94%E5%91%98%E4%BC%9A?fromtitle=%E5%9B%BD%E5%AE%B6%E5%8D%AB%E7%94%9F%E5%81%A5%E5%BA%B7%E5%A7%94&fromid=23545624",
                "https://baike.baidu.com/item/%E5%9B%BD%E5%AE%B6%E4%B8%AD%E5%8C%BB%E8%8D%AF%E7%AE%A1%E7%90%86%E5%B1%80")));
        baikePage.setParameterHasUrlValues(Arrays.asList("机构"));
        baikePage.setParameterNames(new ArrayList<>(Arrays.asList("机构")));
        baikePage.setParameterValues(new ArrayList<>(Arrays.asList("中华人民共和国国家卫生健康委员会","国家中医药管理局")));
//        baikePage.setRelationNames(new ArrayList<>(Arrays.asList("成员国")));
//        List<String> res = new ArrayList<>(Arrays.asList("https://baike.baidu.com/item/%E6%88%90%E5%91%98%E5%9B%BD"));
//        baikePage.setRelationUrls(res);
//        baikePage.setRelationValues(new ArrayList<>(Arrays.asList("阿根廷","巴哈马","巴巴多斯","伯利兹","玻利维亚","巴西","加拿大")));
        boolean out = documentConstructionService.construction(baikePage);
        Assert.assertFalse(out);
    }
}
