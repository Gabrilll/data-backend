package com.judicature.databackend.blImpl.semantic;

import com.judicature.databackend.bl.semantic.GrammarParserGraphService;
import com.judicature.databackend.vo.semantic.DependencyVO;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.dependency.IDependencyParser;
import com.hankcs.hanlp.dependency.nnparser.NeuralNetworkDependencyParser;
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
public class GrammarParserGraphServiceImplTest {
    @Autowired
    GrammarParserGraphServiceImpl grammarParserGraphService;

    /**
     * 获取依存语法图
     * @throws Exception
     */
    @Test
    public void getDependencyGraphVOTest() throws Exception{
        IDependencyParser parser = new NeuralNetworkDependencyParser().enableDeprelTranslator(false);
        List<Term> terms = StandardTokenizer.segment("新型冠状病毒肺炎该如何防治呢？");
        CoNLLSentence cONllSentence = parser.parse(terms);
        DependencyVO dependencyVO = grammarParserGraphService.getDependencyGraphVO(cONllSentence);
        Assert.assertNotNull(dependencyVO);
    }

    /**
     * 获取单例
     */
    @Test
    public void getInstanceTest() throws Exception {
        GrammarParserGraphService singleInstance = GrammarParserGraphServiceImpl.getInstance();
    }
}
