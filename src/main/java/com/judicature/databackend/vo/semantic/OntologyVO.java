package com.judicature.databackend.vo.semantic;

import com.judicature.databackend.vo.NodeVO;
import org.apache.jena.ontology.Individual;

/**
 * @author Gabri
 */
public class OntologyVO {
    private Individual individual;
    private NodeVO nodeVO;

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public NodeVO getNodeVO() {
        return nodeVO;
    }

    public void setNodeVO(NodeVO nodeVO) {
        this.nodeVO = nodeVO;
    }
}
