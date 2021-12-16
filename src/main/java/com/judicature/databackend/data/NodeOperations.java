package com.judicature.databackend.data;

import com.judicature.databackend.po.Node;

import java.util.List;

public interface NodeOperations {
    long addNode(Node node);
    int updateNode(Node node);
    Node getNodeById(long identity);
    List<Node> getAllNodes();
    List<Node> getSubNodes(Long identity);
    Node getNodeByName(String name);
    List<Node> getNodeByLabel(String label);
    Node getNodeByStartAndRe(String startName, String reName);
    List<Node> getNodesByP(String startName, String reName);
    Node getNodeByUUID(String uuid);
}
