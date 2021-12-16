package com.judicature.databackend.bl.semantic;

import com.judicature.databackend.model.PolysemantNamedEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Gabri
 */
@Service
public interface NamedEntityService {

    /**
     * 填充命名实体的相关属性
     *
     * @param namedEntity
     * @return
     */
    public boolean fillNamedEntities(List<PolysemantNamedEntity> namedEntity);


}
