package com.judicature.databackend.bl.semantic;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Gabri
 */
@Service
public interface GrammarParserService {

    /**
     * 依赖解析
     * @param terms
     * @return
     */
    public CoNLLSentence dependencyParser(List<Term> terms);

}
