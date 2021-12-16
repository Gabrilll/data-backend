package com.judicature.databackend.bl.semantic;

import com.judicature.databackend.enums.OntologyClassEnum;
import com.judicature.databackend.model.BaikePage;
import com.judicature.databackend.vo.semantic.OntologyVO;
import org.apache.jena.ontology.Individual;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Gabri
 */
@Service
public interface ConstructionDealingService {
    /**
     * 处理实体和属性对象的关系
     * @param individual
     * @param baikePage
     * @param addClass
     * @return
     */
    public boolean dealProperty(Individual individual, BaikePage baikePage, boolean addClass, List<String> name,OntologyClassEnum ontologyClassEnum);

    /**
     * 查询并返回实体（属性未知-->若不存在就新增实体）
     * @param individualName
     * @param polysemantExplain
     * @param url
     * @param isAliases
     * @param parentClass
     * @return
     */
    public OntologyVO queryIndividual(String individualName, String polysemantExplain, String url, boolean isAliases, OntologyClassEnum parentClass);

    /**
     * 查询并返回实体（属性已知-->若不存在就新增实体）
     * @param individualName
     * @param polysemantExplain
     * @param url
     * @param isAliases
     * @param parentClass
     * @return
     */
    public OntologyVO queryIndividual(String individualName, String polysemantExplain, String url, boolean isAliases, OntologyClassEnum parentClass,List<String> propertyNames,List<String> propertyValues);

}
