package com.judicature.databackend.vo;

/**
 *自动构建的图
 * @author Gabri
 */
public class ConstructionGraph {
    private static GraphVO graph=new GraphVO();

    private ConstructionGraph(){}

    public static GraphVO getGraph(){
        return graph;
    }

    public static void startConstruction(){
        graph=new GraphVO();
    }



}
