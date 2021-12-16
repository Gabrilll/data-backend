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
public class EventConstructionServiceImplTest {
    @Autowired
    EventConstructionServiceImpl eventConstructionService;

    /**
     * 神舟十二号发射
     * @throws Exception
     */
    @Test
    public void constructionTest() throws Exception{
        BaikePage baikePage = new BaikePage();
        baikePage.setUrl("https://baike.baidu.com/item/%E7%A5%9E%E8%88%9F%E5%8D%81%E4%BA%8C%E5%8F%B7");
        baikePage.setPicSrc("https://baike.baidu.com/pic/%E7%A5%9E%E8%88%9F%E5%8D%81%E4%BA%8C%E5%8F%B7/24695455/1/a08b87d6277f9e2f0708b60f7f7efe24b899a901552f?fr=lemma&ct=single#aid=1&pic=a08b87d6277f9e2f0708b60f7f7efe24b899a901552f");
        baikePage.setLemmaSummary("神舟十二号，简称“神十二”，为中国载人航天工程发射的第十二艘飞船，是空间站关键技术验证阶段第四次飞行任务，也是空间站阶段首次载人飞行任务。");
        baikePage.setLemmaTitle("神舟十二号");
        List<String> poly = new ArrayList<>();
        baikePage.setPolysemants(poly);
        baikePage.setPolysemantExplain("");
        List<String> alias = new ArrayList<>();
        baikePage.setAlias(alias);
        List<String> cata = new ArrayList<>(Arrays.asList("发射过程","交会对接","飞行任务","飞行运载","任务标识","飞船结构","飞船设计","技术性能"));
        baikePage.setCatalogue(cata);
        List<String> param = new ArrayList<>();
        baikePage.setParameterHasUrl(param);
        baikePage.setParameterHasUrlValues(Arrays.asList(""));
        baikePage.setParameterNames(new ArrayList<>(Arrays.asList("")));
        baikePage.setParameterValues(new ArrayList<>(Arrays.asList("")));

        boolean out = eventConstructionService.construction(baikePage);
        Assert.assertFalse(out);
    }
}
