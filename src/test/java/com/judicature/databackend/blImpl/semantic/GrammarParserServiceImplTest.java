package com.judicature.databackend.blImpl.semantic;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrammarParserServiceImplTest {
    @Autowired
    GrammarParserServiceImpl grammarParserService;

    /**
     * 依存句法分析
     */
    @Test
    public void dependencyParserTest() throws Exception{
        List<Term> terms = StandardTokenizer.segment("新型冠状病毒肺炎该如何防治呢？");
        CoNLLSentence coNLLsentence = grammarParserService.dependencyParser(terms);
        Assert.assertNotNull(coNLLsentence);
    }

    /**
     * 获取单例
     */
    @Test
    public void getInstanceTest() throws Exception{
        GrammarParserServiceImpl.getInstance();
    }
}
