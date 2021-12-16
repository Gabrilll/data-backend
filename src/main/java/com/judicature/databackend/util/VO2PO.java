package com.judicature.databackend.util;

import com.judicature.databackend.po.History;
import com.judicature.databackend.po.Node;
import com.judicature.databackend.po.Property;
import com.judicature.databackend.po.Relation;
import com.judicature.databackend.vo.HistoryVO;
import com.judicature.databackend.vo.NodeVO;
import com.judicature.databackend.vo.PropertyVO;
import com.judicature.databackend.vo.RelationVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Gabri
 */
public class VO2PO {

    /**
     * Property Map to Property List
     *
     * @param propMap Map
     * @return List
     */
    public static List<Property> toProperties(Map<String, String> propMap) {
        ArrayList<Property> properties = new ArrayList<>();
        if (propMap != null) {
            for (Map.Entry<String, String> entry : propMap.entrySet()) {
                Property property = new Property();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
                properties.add(property);
            }
        }
        return properties;
    }

    /**
     * property map to propertyVO list
     *
     * @param propMap
     * @return
     */
    public static List<PropertyVO> toPropertyVOs(Map<String, String> propMap) {
        ArrayList<PropertyVO> properties = new ArrayList<>();
        if (propMap != null) {
            for (Map.Entry<String, String> entry : propMap.entrySet()) {
                PropertyVO property = new PropertyVO();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
                properties.add(property);
            }
        }
        return properties;
    }

    public static Map<String,String> toPropertyMap(List<Property> properties){
        HashMap<String,String> propertyMap=new HashMap<>(properties.size());
        for(Property p:properties){
            propertyMap.put(p.getKey(),p.getValue());
        }
        return propertyMap;
    }

    /**
     * NodeVO to Node
     *
     * @param nodeVO NodeVO
     * @return Node
     */
    public static Node toNode(NodeVO nodeVO) {
        Node node = new Node();
        node.setIdentity(nodeVO.getIdentity());
        node.setLabels(nodeVO.getLabels());
        node.setProperties(toProperties(nodeVO.getProperties()));
        return node;
    }

    public static NodeVO toNodeVO(Node node){
        if(node==null) {
            return null;
        }
        NodeVO nodeVO=new NodeVO();
        nodeVO.setIdentity(node.getIdentity());
        nodeVO.setLabels(node.getLabels());
        nodeVO.setProperties(toPropertyMap(node.getProperties()));
        return nodeVO;
    }

    /**
     * RelationVO to Relation
     *
     * @param relationVO RelationVO
     * @return Relation
     */
    public static Relation toRelation(RelationVO relationVO) {
        Relation relation = new Relation();
        relation.setIdentity(relationVO.getIdentity());
        relation.setType(relationVO.getType());
        relation.setStart(relationVO.getStart());
        relation.setEnd(relationVO.getEnd());
        relation.setProperties(toProperties(relationVO.getProperties()));
        return relation;
    }

    public static RelationVO toRelationVO(Relation relation){
        RelationVO relationVO=new RelationVO();
        relationVO.setIdentity(relation.getIdentity());
        relationVO.setProperties(toPropertyMap(relation.getProperties()));
        relationVO.setType(relation.getType());
        relationVO.setStart(relation.getStart());
        relationVO.setEnd(relation.getEnd());
        return relationVO;
    }

    /**
     * historyVO to history
     *
     * @param historyVO
     * @return
     */
    public static History toHistory(HistoryVO historyVO) {
        History history = new History();
        history.setId(historyVO.getId());
        history.setOperationType(historyVO.getOperationType());
        history.setDate(historyVO.getDate());
        history.setObjectId(historyVO.getObjectId());
        if (historyVO.getProperties() != null) {
            history.setProperties(historyVO.getProperties());
        }
        return history;
    }

    public static List<HistoryVO> toHistoryVOs(List<History> histories) {
        if (histories == null) {
            return new ArrayList<>();
        }
        return histories.stream().map(h -> {
            HistoryVO historyVO = new HistoryVO();
            historyVO.setId(h.getId());
            historyVO.setOperationType(h.getOperationType());
            historyVO.setObjectId(h.getObjectId());
            historyVO.setDate(h.getDate());
            historyVO.setProperties(h.getProperties());
            return historyVO;
        }).collect(Collectors.toList());
    }


}
