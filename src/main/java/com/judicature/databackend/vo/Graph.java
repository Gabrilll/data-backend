package com.judicature.databackend.vo;

/**
 * @author Gabri
 */
public class Graph {
    private static GraphVO instance;

    private Graph() {
    }

    public static GraphVO getInstance() {
        return instance;
    }

    public static void setInstance(GraphVO graphVO) {
        instance = graphVO;
    }

    public static void addNode(NodeVO nodeVO) {
        instance.addNode(nodeVO);
    }

    public static void deleteNode(long identity) {
        instance.deleteNode(identity);
    }

    public static void updateNode(NodeVO nodeVO){
        instance.updateNode(nodeVO);
    }

    public static void addRelation(RelationVO relationVO){
        instance.addRelation(relationVO);
    }

    public static void deleteRelation(long identity){
        instance.deleteRelation(identity);
    }

    public static void updateRelation(long oldId,RelationVO relationVO){
        instance.updateRelation(oldId,relationVO);
    }
}
