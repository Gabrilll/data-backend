package com.judicature.databackend.blImpl.semantic;

import com.judicature.databackend.bl.semantic.GrammarParserGraphService;
import com.judicature.databackend.vo.semantic.Arg;
import com.judicature.databackend.vo.semantic.DependencyNode;
import com.judicature.databackend.vo.semantic.DependencyVO;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import org.springframework.stereotype.Service;

/**
 * @author Gabri
 */
@Service
public class GrammarParserGraphServiceImpl implements GrammarParserGraphService {

    private volatile static GrammarParserGraphService singleInstance;

    /**
     * 私有化构造方法，实现单例
     */
    private GrammarParserGraphServiceImpl() {
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static GrammarParserGraphService getInstance() {
        if (singleInstance == null) {
            synchronized (GrammarParserGraphServiceImpl.class) {
                if (singleInstance == null) {
                    singleInstance = new GrammarParserGraphServiceImpl();
                }
            }
        }
        return singleInstance;
    }

//    /**
//     * 依存句法分析
//     */
//    public CoNLLSentence dependencyParser(List<Term> terms) {
//        // 基于神经网络的高性能依存句法分析器
//        IDependencyParser parser = new NeuralNetworkDependencyParser().enableDeprelTranslator(false);
//        CoNLLSentence cONllSentence = parser.parse(terms);
//        return cONllSentence;
//    }

    /**
     * 获取依存语法图
     */
    @Override
    public DependencyVO getDependencyGraphVO(CoNLLSentence cONllSentence) {
        DependencyVO dependencyVO = new DependencyVO();
        if (cONllSentence != null) {
            for (CoNLLWord dependency : cONllSentence) {
                DependencyNode dependencyNode = new DependencyNode();
                Arg arg = new Arg();
                arg.setLength(0);
                dependencyNode.setId(dependency.ID - 1);
                dependencyNode.setCont(dependency.LEMMA);
                dependencyNode.setPos(dependency.POSTAG);
                dependencyNode.setNe("0");
                dependencyNode.setParent(dependency.HEAD.ID - 1);
                dependencyNode.setRelate(dependency.DEPREL);
                dependencyNode.setArg(arg);
                dependencyVO.getDependencyNodes().add(dependencyNode);
            }
        }
        return dependencyVO;
    }
}
