package com.judicature.databackend.vo;

import com.judicature.databackend.model.AnswerStatement;
import com.judicature.databackend.model.QueryResult;

import java.util.List;

/**
 * 智能问答
 */
public class SemanticAnswerVO {
    List<AnswerStatement> semanticStatement;
    String smartQA;
    List<QueryResult> queryResults;

    public List<AnswerStatement> getSemanticStatement() {
        return semanticStatement;
    }

    public void setSemanticStatement(List<AnswerStatement> semanticStatement) {
        this.semanticStatement = semanticStatement;
    }

    public String getSmartQA() {
        return smartQA;
    }

    public void setSmartQA(String smartQA) {
        this.smartQA = smartQA;
    }

    public List<QueryResult> getQueryResults() {
        return queryResults;
    }

    public void setQueryResults(List<QueryResult> queryResults) {
        this.queryResults = queryResults;
    }
}
