package com.judicature.databackend.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Gabri
 */
public class GraphList {
    private static final List<GraphVO> graphs=new ArrayList<>();

    private GraphList(){}

    public static List<GraphVO> getGraphList(){
        return graphs;
    }

    public static void addGraph(GraphVO graphVO){
        if(graphVO.getNodes()==null){
            graphVO.setNodes(new ArrayList<>());
        }
        if(graphVO.getEdges()==null){
            graphVO.setEdges(new ArrayList<>());
        }

        for(NodeVO node:graphVO.getNodes()){
            for(Map.Entry<String,String> pro:node.getProperties().entrySet()){
                if(pro.getValue().length()>30){
                    pro.setValue(pro.getValue().substring(0,30));
                }
            }
        }
        setCls(graphVO.getNodes());
        graphs.add(graphVO);
        graphVO.setId(graphs.size()-1);
    }

    public static GraphVO getGraph(int id){
        if(id<graphs.size()){
            return graphs.get(id);
        }else{
            return new GraphVO();
        }
    }

    public static int getGraphNum(){
        return graphs.size();
    }

    public static void removeGraph(int id){
        graphs.removeIf(graph -> graph.getId() == id);
    }

    public static void setCls(List<NodeVO> nodes){
        ArrayList<String> labelType = new ArrayList<>();
        labelType.add("others");
        if (nodes==null){
            return;
        }
        for(NodeVO node:nodes){
            if(node.getLabels().isEmpty()){
                node.setCls("0");
                continue;
            }
            int idx = labelType.indexOf(node.getLabels().get(0));
            if(idx == -1){
                labelType.add(node.getLabels().get(0));
                node.setCls(String.valueOf(labelType.size() - 1));
            }
            else{
                node.setCls(String.valueOf(idx));
            }
        }
    }
}
