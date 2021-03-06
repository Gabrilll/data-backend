package com.judicature.databackend.data.semantic;

import com.judicature.databackend.config.SemanticConfig;
import com.judicature.databackend.enums.AnswerTypeEnum;
import com.judicature.databackend.model.Answer;
import com.judicature.databackend.model.QueryResult;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.util.FmtUtils;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabri
 */
public class QueryDAOImpl implements QueryDAO {

    private volatile static QueryDAO singleInstance;

    @SuppressWarnings(value = "unused")
    private static final Logger log = LoggerFactory.getLogger(QueryDAOImpl.class);

    static SemanticConfig semanticConfig=SemanticConfig.getInstance();

    private static OntModel model = semanticConfig.getModel();

    /**
     * 私有化构造方法，实现单例模式
     */
    private QueryDAOImpl() {
    }

    public static QueryDAO getInstance() {
        if (singleInstance == null) {
            synchronized (QueryDAOImpl.class) {
                if (singleInstance == null) {
                    singleInstance = new QueryDAOImpl();
                }
            }
        }
        return singleInstance;
    }

    // TODO 测试
    public void test() {
        String prefix = "prefix mymo: <" + semanticConfig.getPizzaNs() + ">\n" +
                "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                "prefix owl: <" + OWL.getURI() + ">\n";
        String QL = "SELECT ?导演姓名 WHERE { mymo:美人鱼  mymo:有导演  ?导演.\n"
                + "?导演  mymo:有姓名  ?导演姓名.}";
        String SPARQL = prefix + QL;
        showQuery(model, SPARQL);
        // TODO 将测试代码写在这
    }

    // TODO 测试
    public void showQuery(Model model, String q) {
        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(results, model);
        } finally {
            qexec.close();
        }
    }

    /**
     * TODO 暂时还没使用到 查询实体是否存在
     */
    @Override
    public boolean individualExist(String individualName) {
        // TODO 获取模型和载入本体数据的方法是否应该写成静态方法
        String individualNameUrl = semanticConfig.getPizzaNs() + individualName;
        Individual individual = model.getIndividual(individualNameUrl);
        return individual != null;
    }

    /**
     * 查询等价实体 如查询星爷的等价实体？ ————》周星驰
     */
    @Override
    public String querySameIndividual(String individualName) {
        String sameIndividual = null;
        String prefix = "prefix mymo: <" + semanticConfig.getPizzaNs() + ">\n" +
                "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                "prefix owl: <" + OWL.getURI() + ">\n";
        String QL = "SELECT ?等价实体   WHERE {?等价实体   owl:sameAs mymo:" + individualName + ".\n}";
        String SPARQL = prefix + QL;
        Query query = QueryFactory.create(SPARQL);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(results);
        int numCols = resultSetRewindable.getResultVars().size();
        while (resultSetRewindable.hasNext()) {
            QuerySolution querySolution = resultSetRewindable.next();
            for (int col = 0; col < numCols; col++) {
                String rVar = results.getResultVars().get(col);
                RDFNode obj = querySolution.get(rVar);
                sameIndividual = FmtUtils.stringForRDFNode(obj).split(":")[1];
            }

        }
        return sameIndividual;
    }

    /**
     * TODO 可以使用listSameAs方法  查询等价实体 如查询周星驰的所有等价实体
     */
    @Override
    public List<String> querySameIndividuals(String individualName) {
        List<String> sameIndividuals = new ArrayList<String>();
        String prefix = "prefix mymo: <" + semanticConfig.getPizzaNs() + ">\n" +
                "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                "prefix owl: <" + OWL.getURI() + ">\n";
        String QL = "SELECT ?等价实体   WHERE {mymo:" + individualName + " owl:sameAs ?等价实体.\n}";
        String SPARQL = prefix + QL;
        Query query = QueryFactory.create(SPARQL);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(results);
        int numCols = resultSetRewindable.getResultVars().size();
        while (resultSetRewindable.hasNext()) {
            QuerySolution querySolution = resultSetRewindable.next();
            for (int col = 0; col < numCols; col++) {
                String rVar = results.getResultVars().get(col);
                RDFNode obj = querySolution.get(rVar);
                String sameIndividual = FmtUtils.stringForRDFNode(obj).split(":")[1];
                sameIndividuals.add(sameIndividual);
            }
        }
        return sameIndividuals;
    }


    /**
     * 查询所有以Subject为主语的断言
     */
    @Override
    public List<Statement> getStatementsBySubject(String subject) {
        List<Statement> statements = new ArrayList<>();
        StmtIterator stmtIter = model.listStatements();
        while (stmtIter.hasNext()) {
            Statement statement = stmtIter.next();
            String subjectName = null;
            if (statement.getSubject() != null && statement.getSubject().getURI() != null) {
                String[] urlFields = statement.getSubject().getURI().split("#");
                if (urlFields.length > 1) {
                    subjectName = urlFields[1];
                } else {
                    subjectName = urlFields[0];
                }
                if (subjectName != null) {
                    if (subjectName.equals(subject)) {
                        statements.add(statement);
                    }
                }
            }
        }
        return statements;
    }

    /**
     * 查询所有以Object为宾语的断言
     */
    @Override
    public List<Statement> getStatementsByObject(String object) {
        List<Statement> statements = new ArrayList<>();
        StmtIterator stmtIter = model.listStatements();
        while (stmtIter.hasNext()) {
            Statement statement = stmtIter.next();
            if (statement.getObject().toString().split("#").length > 1) {
                if (statement.getObject().toString().split("#")[1].equals(object)) {
                    statements.add(statement);
                }
            }
        }
        return statements;
    }

    /**
     * 查询答案
     */
    @Override
    public QueryResult queryOntology(String sparql) {
        String prefix = "prefix mymo: <" + semanticConfig.getPizzaNs() + ">\n" +
                "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                "prefix owl: <" + OWL.getURI() + ">\n";

        Query query = QueryFactory.create(prefix + sparql);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            QueryResult result = new QueryResult();
            ResultSet resultSet = qexec.execSelect();
            ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(resultSet);
            int numCols = resultSetRewindable.getResultVars().size();
            List<Answer> answers = new ArrayList<>();
            while (resultSetRewindable.hasNext()) {
                QuerySolution rBind = resultSetRewindable.nextSolution();
                for (int col = 0; col < numCols; col++) {
                    String rVar = resultSet.getResultVars().get(col);
                    RDFNode obj = rBind.get(rVar);
                    String answerStr = FmtUtils.stringForRDFNode(obj);
                    if (numCols > 1) {
                        Answer answer = new Answer();
                        answer.setType(AnswerTypeEnum.TABLE);
                        answer.setContent(answerStr);
                        answers.add(answer);
                    } else {
                        Answer answer = new Answer();
                        answer.setType(AnswerTypeEnum.STRING);
                        answer.setContent(answerStr);
                        answers.add(answer);
                    }
                }
            }
            result.setAnswers(answers);
            return result;
        }
    }

    /**
     * 查询某一个本体的所有属性
     *
     * @return
     */
    @Override
    public StmtIterator queryIndividualProperties(String individualName) {
        String individualNameUrl = semanticConfig.getPizzaNs() + individualName;
        Individual individual = model.getIndividual(individualNameUrl);
        StmtIterator properties = null;
        if (individual != null) {
            properties = individual.listProperties(); // 列出该实体所有的属性
        }
        return properties;
    }

    /**
     * 查询某一个实体的主要属性
     */
    @Override
    public List<Statement> queryIndividualMainProperties(String individualName) {
        String individualNameUrl = semanticConfig.getPizzaNs() + individualName;
        Individual individual = model.getIndividual(individualNameUrl);
        if(individual==null) {
            return new ArrayList<>();
        }
        // 列出所有的对象属性
        // ExtendedIterator<ObjectProperty> object = m.listObjectProperties();
        // 列出该实体所有的属性
        StmtIterator properties = individual.listProperties();
        List<Statement> mainProperties = new ArrayList<>();
        while (properties.hasNext()) {
            Statement propertyStatement = properties.next();
            RDFNode objectNode = propertyStatement.getObject();
            // 对象属性 返回
            if (!objectNode.isLiteral()) {
                mainProperties.add(propertyStatement);
            } else if (objectNode.toString().length() < 30) {
                mainProperties.add(propertyStatement);
            }
        }
        return mainProperties;
    }

    @Override
    public String queryIndividualComment(String individualName) {
        String individualNameUrl = semanticConfig.getPizzaNs() + individualName;
        Individual individual = model.getIndividual(individualNameUrl);
        String comment = individual.getComment(null);
        return comment;
    }

    /**
     * TODO 暂时还未使用到 获取所有的实体
     */
    @Override
    public ExtendedIterator<Individual> getAllIndividuals() {
        return model.listIndividuals();
    }


}