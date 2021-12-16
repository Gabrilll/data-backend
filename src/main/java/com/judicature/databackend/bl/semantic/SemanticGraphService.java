package com.judicature.databackend.bl.semantic;

import com.judicature.databackend.model.PolysemantNamedEntity;
import com.judicature.databackend.model.SemanticGraph;
import com.judicature.databackend.model.Word;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Gabri
 */
@Service
public interface SemanticGraphService {

    /**
     * 创建语义图
     *
     * @param coNLLsentence
     * @param polysemantNamedEntities
     * @return
     */
    public SemanticGraph buildSemanticGraph(CoNLLSentence coNLLsentence, List<PolysemantNamedEntity> polysemantNamedEntities);

    /**
     * 创建备用语义图
     *
     * @param words
     * @return
     */
    public SemanticGraph buildBackUpSemanticGraph(List<Word> words);

}
