package com.judicature.databackend.vo;

import java.util.List;

/**
 * @author Gabri
 */
public class StatisticsVO {
    Long totalNodeNum;
    Long totalRelationNum;
    Integer currentNodeNum;
    Integer currentRelationNum;
    List<String> nodeLabels;
    List<String> propertyKeys;
    List<String> relationTypes;

    public Long getTotalNodeNum() {
        return totalNodeNum;
    }

    public void setTotalNodeNum(Long totalNodeNum) {
        this.totalNodeNum = totalNodeNum;
    }

    public Long getTotalRelationNum() {
        return totalRelationNum;
    }

    public void setTotalRelationNum(Long totalRelationNum) {
        this.totalRelationNum = totalRelationNum;
    }

    public Integer getCurrentNodeNum() {
        return currentNodeNum;
    }

    public void setCurrentNodeNum(Integer currentNodeNum) {
        this.currentNodeNum = currentNodeNum;
    }

    public Integer getCurrentRelationNum() {
        return currentRelationNum;
    }

    public void setCurrentRelationNum(Integer currentRelationNum) {
        this.currentRelationNum = currentRelationNum;
    }

    public List<String> getNodeLabels() {
        return nodeLabels;
    }

    public void setNodeLabels(List<String> nodeLabels) {
        this.nodeLabels = nodeLabels;
    }

    public List<String> getPropertyKeys() {
        return propertyKeys;
    }

    public void setPropertyKeys(List<String> propertyKeys) {
        this.propertyKeys = propertyKeys;
    }

    public List<String> getRelationTypes() {
        return relationTypes;
    }

    public void setRelationTypes(List<String> relationTypes) {
        this.relationTypes = relationTypes;
    }
}
