package com.judicature.databackend.enums;

/**
 * @author Gabri
 */

public enum OperationType {

    /**/
    AddNode("添加节点"),
    /**/
    DeleteNode("删除节点"),
    /**/
    UpdateNode("更新节点信息"),
    /**/
    AddRelation("添加关系"),
    /**/
    DeleteRelation("删除关系"),
    /**/
    UpdateRelation("更新关系信息");

    private String value;

    OperationType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
