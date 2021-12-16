package com.judicature.databackend.bl.semantic;

import com.judicature.databackend.model.AnswerStatement;
import com.judicature.databackend.model.PolysemantStatement;
import com.judicature.databackend.model.QueryResult;
import com.judicature.databackend.model.SemanticGraph;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Gabri
 */
@Service
public interface QueryService {

    /**
     * 断言集合
     *
     * @return
     */
    public List<AnswerStatement> createStatement(SemanticGraph semanticGraph);

    /**
     * 歧义断言集合（一个歧义断言包含断言集合）
     *
     * @param answerStatements
     * @return
     */
    public List<PolysemantStatement> createPolysemantStatements(List<AnswerStatement> answerStatements);

    /**
     * 实体消岐
     *
     * @param myStatements
     * @return
     */
    public List<AnswerStatement> individualsDisambiguation(List<AnswerStatement> myStatements);

    /**
     * 谓语消岐
     *
     * @param myStatements
     * @return
     */
    public List<AnswerStatement> predicateDisambiguation(List<AnswerStatement> myStatements);

    /**
     * 构造查询断言
     *
     * @param myStatements
     * @return
     */
    public List<AnswerStatement> createQueryStatements(List<AnswerStatement> myStatements);

    /**
     * 根据断言构造查询语句
     *
     * @param myStatements
     * @return
     */
    public String createSparql(List<AnswerStatement> myStatements);

    /**
     * 根据断言构造多个查询语句
     *
     * @param myStatements
     * @return
     */
    public List<String> createSparqls(List<AnswerStatement> myStatements);

    /**
     * 执行查询语句 查询本体
     *
     * @param SPARQL
     * @return
     */
    public QueryResult queryOntology(String SPARQL);

    /**
     * @param individualName
     * @return
     */
    public String queryIndividualComment(String individualName);
}