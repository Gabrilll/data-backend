package com.judicature.databackend.vo.semantic;

import com.judicature.databackend.model.AnswerStatement;
import com.judicature.databackend.model.PolysemantNamedEntity;
import com.judicature.databackend.model.PolysemantStatement;
import com.judicature.databackend.model.QueryResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PolysemantSituationVOTest {

    PolysemantSituationVO polysemantSituationVO = new PolysemantSituationVO();

    @BeforeEach
    public void setup(){
        polysemantSituationVO.setPolysemantStatement(null);
        polysemantSituationVO.setSparqls(null);
        polysemantSituationVO.setQueryResults(null);
        polysemantSituationVO.setActivePolysemantNamedEntities(null);
        polysemantSituationVO.setSemanticStatements(null);
        polysemantSituationVO.setIndividualsDisambiguationStatements(null);
        polysemantSituationVO.setPredicateDisambiguationStatements(null);
        polysemantSituationVO.setQueryStatements(null);
    }
    @Test
    void getPolysemantStatement() {
        PolysemantStatement polysemantStatement = polysemantSituationVO.getPolysemantStatement();
    }

    @Test
    void setPolysemantStatement() {
        PolysemantStatement p = new PolysemantStatement();
        polysemantSituationVO.setPolysemantStatement(p);
    }

    @Test
    void getSemanticStatements() {
        List<AnswerStatement> res = polysemantSituationVO.getSemanticStatements();
    }

    @Test
    void setSemanticStatements() {
        List<AnswerStatement> a = new ArrayList<>();
        polysemantSituationVO.setSemanticStatements(a);
    }

    @Test
    void getActivePolysemantNamedEntities() {
        polysemantSituationVO.getActivePolysemantNamedEntities();
    }

    @Test
    void setActivePolysemantNamedEntities() {
        List<PolysemantNamedEntity> list = new ArrayList<>();
        polysemantSituationVO.setActivePolysemantNamedEntities(list);
    }

    @Test
    void getIndividualsDisambiguationStatements() {
        polysemantSituationVO.getIndividualsDisambiguationStatements();
    }

    @Test
    void setIndividualsDisambiguationStatements() {
        List<AnswerStatement> a = new ArrayList<>();
        polysemantSituationVO.setIndividualsDisambiguationStatements(a);
    }

    @Test
    void getPredicateDisambiguationStatements() {
        polysemantSituationVO.getPredicateDisambiguationStatements();
    }

    @Test
    void setPredicateDisambiguationStatements() {
        List<AnswerStatement> a = new ArrayList<>();
        polysemantSituationVO.setPredicateDisambiguationStatements(a);
    }

    @Test
    void getQueryStatements() {
        polysemantSituationVO.getQueryStatements();
    }

    @Test
    void setQueryStatements() {
        List<AnswerStatement> a = new ArrayList<>();
        polysemantSituationVO.setQueryStatements(a);
    }

    @Test
    void getSparqls() {
        polysemantSituationVO.getSparqls();
    }

    @Test
    void setSparqls() {
        List<String> s = new ArrayList<>();
        polysemantSituationVO.setSparqls(s);
    }

    @Test
    void getQueryResults() {
        polysemantSituationVO.getQueryResults();
    }

    @Test
    void setQueryResults() {
        List<QueryResult> qRs = new ArrayList<>();
        polysemantSituationVO.setQueryResults(qRs);
    }

    @Test
    void testToString() {
        polysemantSituationVO.toString();
    }
}