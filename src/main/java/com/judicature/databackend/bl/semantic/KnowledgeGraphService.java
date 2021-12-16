package com.judicature.databackend.bl.semantic;

import com.judicature.databackend.model.PolysemantNamedEntity;
import com.judicature.databackend.vo.semantic.KnowledgeGraphVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Gabri
 */
@Service
public interface KnowledgeGraphService {

    /**
     * 获取命名实体的知识图谱
     *
     * @param polysemantEntities
     * @return
     */
    List<KnowledgeGraphVO> getKnowledgeGraphVO(List<PolysemantNamedEntity> polysemantEntities);


}
