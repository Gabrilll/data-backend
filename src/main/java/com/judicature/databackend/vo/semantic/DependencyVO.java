package com.judicature.databackend.vo.semantic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabri
 */
public class DependencyVO implements java.io.Serializable {

    /**
     * default serial version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 依存关系的弧线集合
     */
    private List<DependencyNode> dependencyNodes = new ArrayList<>();

    public List<DependencyNode> getDependencyNodes() {
        return dependencyNodes;
    }

    public void setDependencyNodes(List<DependencyNode> dependencyNodes) {
        this.dependencyNodes = dependencyNodes;
    }

    @Override
    public String toString() {
        return "DependencyVO{" +
                "dependencyNodes=" + dependencyNodes +
                '}';
    }
}
