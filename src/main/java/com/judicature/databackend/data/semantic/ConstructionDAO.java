package com.judicature.databackend.data.semantic;

import org.apache.jena.ontology.Individual;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Gabri
 */

@Repository
public interface ConstructionDAO extends ConstructionBaseDAO {

    /**
     * 添加单个疾病类对象属性
     *
     * @param individualStart
     * @param relationName
     * @param individualEnd
     * @return
     */
    public boolean addDiseaseObjectProperty(Individual individualStart, String relationName, Individual individualEnd);

    /**
     * 添加一组疾病类对象属性
     *
     * @param individualStart
     * @param relationName
     * @param individualEnd
     * @return
     */
    public boolean addDiseaseObjectProperties(Individual individualStart, List<String> relationName, List<Individual> individualEnd);

    /**
     * 添加单个组织类对象属性
     *
     * @param individualStart
     * @param relationName
     * @param individualEnd
     * @return
     */
    public boolean addOrganizationProperty(Individual individualStart, String relationName, Individual individualEnd);

    /**
     * 添加一组组织类对象属性
     *
     * @param individualStart
     * @param relationName
     * @param individualEnd
     * @return
     */
    public boolean addOrganizationProperties(Individual individualStart, List<String> relationName, List<Individual> individualEnd);

    /**
     * 添加单个人物类对象属性
     *
     * @param individualStart
     * @param relationName
     * @param individualEnd
     * @return
     */
    public boolean addPersonProperty(Individual individualStart, String relationName, Individual individualEnd);

    /**
     * 添加一组人物类对象属性
     *
     * @param individualStart
     * @param relationName
     * @param individualEnd
     * @return
     */
    public boolean addPersonProperties(Individual individualStart, List<String> relationName, List<Individual> individualEnd);
}
