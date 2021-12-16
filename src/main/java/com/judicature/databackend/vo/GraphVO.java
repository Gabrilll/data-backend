package com.judicature.databackend.vo;

import java.util.ArrayList;
import java.util.List;

public class GraphVO {
    private List<NodeVO> nodes;
    private List<RelationVO> edges;
    private Integer id;

    public List<NodeVO> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeVO> nodes) {
        this.nodes = nodes;
    }

    public List<RelationVO> getEdges() {
        return edges;
    }

    public void setEdges(List<RelationVO> edges) {
        this.edges = edges;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void addNode(NodeVO nodeVO){
        if(nodes==null){
            nodes=new ArrayList<>();
        }
        this.nodes.add(nodeVO);
    }

    public void deleteNode(long identity){
        nodes.removeIf(n -> n.getIdentity() == identity);
        if (edges != null && edges.size() != 0){
            edges.removeIf(r -> r.getStart() == identity || r.getEnd() == identity);
        }

    }

    public void updateNode(NodeVO nodeVO){
        for(NodeVO n:nodes){
            if(n.getIdentity().equals(nodeVO.getIdentity())){
                int ind=nodes.indexOf(n);
                nodes.set(ind, nodeVO);
            }
        }
    }

    public  void addRelation(RelationVO relationVO){
        if(edges==null){
            edges=new ArrayList<>();
        }
        edges.add(relationVO);
    }

    public void deleteRelation(long identity){
        edges.removeIf(e->e.getIdentity()==identity);
    }

    public void updateRelation(long oldId,RelationVO relationVO){
        for(RelationVO r:edges){
            if(r.getIdentity()==oldId){
                int ind=edges.indexOf(r);
                edges.set(ind,relationVO);
            }
        }
    }

    public NodeVO findNodeByUUID(String UUID){
        if(UUID==null){
            return null;
        }
        if(nodes==null){
            nodes=new ArrayList<>();
        }
        for(NodeVO nodeVO:nodes){
            String value=nodeVO.getPropertyValueByName("UUID");
            if (value!=null&&value.equals(UUID)){
                return nodeVO;
            }
        }
        return null;
    }
}
