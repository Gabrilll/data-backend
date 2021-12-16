package com.judicature.databackend.blImpl.semantic;

import com.judicature.databackend.model.PolysemantNamedEntity;
import com.judicature.databackend.model.Word;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
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
public class SemanticGraphServiceImplTest {
    @Autowired
    SemanticGraphServiceImpl semanticGraphService;

    @Autowired
    GrammarParserServiceImpl grammarParserService;

    @Test
    public void buildSemanticGraphTest() throws Exception{
        List<Term> terms = StandardTokenizer.segment("目标飞行器");
        CoNLLSentence coNLLsentence = grammarParserService.dependencyParser(terms);

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

        semanticGraphService.buildSemanticGraph(coNLLsentence,polysemantNamedEntities);
    }

    @Test
    public void getInstanceTest() throws Exception{
        SemanticGraphServiceImpl.getInstance();
    }

    @Test
    public void buildBackUpSemanticGraphTest() throws Exception{
        List<Word> words = new ArrayList<>();
        Word e = new Word();
        e.setName("目标飞行器");

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

        e.setPolysemantNamedEntities(polysemantNamedEntities);

        words.add(e);
        semanticGraphService.buildBackUpSemanticGraph(words);
    }
}
