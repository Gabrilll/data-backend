package com.judicature.databackend.data;

import com.judicature.databackend.po.Node;

import java.util.List;

public interface GraphOperations {


    /**
     * @param labels 根据节点类型过滤
     * @return
     */
    List<Node> filterByNodeLabels(List<String> labels);

}
