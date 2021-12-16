package com.judicature.databackend.blImpl.semantic;

import com.judicature.databackend.bl.semantic.GrammarParserService;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.dependency.IDependencyParser;
import com.hankcs.hanlp.dependency.nnparser.NeuralNetworkDependencyParser;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Gabri
 */
@Service
public class GrammarParserServiceImpl implements GrammarParserService {

    private volatile static GrammarParserService singleInstance;

    /**
     * 私有化构造方法，实现单例
     */
    private GrammarParserServiceImpl() {
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static GrammarParserService getInstance() {
        if (singleInstance == null) {
            synchronized (GrammarParserServiceImpl.class) {
                if (singleInstance == null) {
                    singleInstance = new GrammarParserServiceImpl();
                }
            }
        }
        return singleInstance;
    }

    /**
     * 依存句法分析
     */
    @Override
    public CoNLLSentence dependencyParser(List<Term> terms) {
        // 基于神经网络的高性能依存句法分析器
        IDependencyParser parser = new NeuralNetworkDependencyParser().enableDeprelTranslator(false);
        CoNLLSentence coNLLsentence = parser.parse(terms);
        return coNLLsentence;
    }
}