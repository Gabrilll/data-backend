package com.judicature.databackend.bl.semantic;

import com.judicature.databackend.vo.semantic.DependencyVO;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import org.springframework.stereotype.Service;

/**
 * @author Gabri
 */
@Service
public interface GrammarParserGraphService {

    /**
     * 获取依赖图
     *
     * @param cONllSentence
     * @return
     */
    DependencyVO getDependencyGraphVO(CoNLLSentence cONllSentence);

}
