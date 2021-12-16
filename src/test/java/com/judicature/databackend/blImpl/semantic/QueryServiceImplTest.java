package com.judicature.databackend.blImpl.semantic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryServiceImplTest {
    @Autowired
    QueryServiceImpl queryService;

    @Test
    public void createStatementTest() throws Exception{

    }

    @Test
    public void getInstanceTest() throws Exception{
        QueryServiceImpl.getInstance();
    }

//    @Test
//    public void queryOntologyTest() throws Exception{
//        queryService.queryOntology("新冠");
//    }
//
//    @Test
//    public void queryIndividualCommentTest() throws Exception{
//        queryService.queryIndividualComment("新冠");
//    }
//
//    @Test
//    public void createSparqlsTest() throws Exception{
//        List<AnswerStatement> answerStatements = new ArrayList<>();
//        AnswerStatement aS = new AnswerStatement();
//        aS.setStatementStr("新冠");
//        answerStatements.add(aS);
//        queryService.createSparqls(answerStatements);
//        queryService.createSparql(answerStatements);
//        queryService.createQueryStatements(answerStatements);
//        queryService.predicateDisambiguation(answerStatements);
//        queryService.individualsDisambiguation(answerStatements);
//    }
}

