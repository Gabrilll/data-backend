package com.judicature.databackend.model;

import edu.stanford.nlp.graph.DirectedMultiGraph;


/**
 * @author Gabri
 */
public class SemanticGraph extends DirectedMultiGraph<SemanticGraphVertex, SemanticGraphEdge> {

    /**
     * default serial version ID
     */
    private static final long serialVersionUID = 1L;

    public boolean printGraph() {
        StringBuilder s = new StringBuilder();
        s.append("{\n");
        s.append("Graph:\n");
        for (SemanticGraphVertex sourceVertex : this.getAllVertices()) {
            for (SemanticGraphEdge edge : this.getOutgoingEdges(sourceVertex)) {
                SemanticGraphVertex destVertex = new SemanticGraphVertex();
                for (SemanticGraphVertex v : this.getAllVertices()) {
                    for (SemanticGraphEdge e : this.getIncomingEdges(v)) {
                        if (e.equals(edge)) {
                            destVertex = v;
                        }
                    }
                }
                s.append(sourceVertex.getWord().getName()).append("(").append(sourceVertex.getWord().getPostag()).append(")");
                s.append("-----").append(edge.getWord().getName()).append("(").append(edge.getWord().getPostag()).append(")").append("---->");
                s.append(destVertex.getWord().getName()).append("(").append(destVertex.getWord().getPostag()).append(")");
                s.append("\n");
            }
        }
        s.append('}');
        System.out.println("语义图结构如下:" + s.toString());
        return true;
    }
}
