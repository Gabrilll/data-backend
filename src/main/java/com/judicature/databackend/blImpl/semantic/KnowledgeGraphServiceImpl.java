package com.judicature.databackend.blImpl.semantic;

import com.judicature.databackend.bl.semantic.KnowledgeGraphService;
import com.judicature.databackend.data.semantic.QueryDAO;
import com.judicature.databackend.data.semantic.QueryDAOImpl;
import com.judicature.databackend.model.PolysemantNamedEntity;
import com.judicature.databackend.util.StringHandle;
import com.judicature.databackend.vo.semantic.KnowledgeGraphNodeVO;
import com.judicature.databackend.vo.semantic.KnowledgeGraphStatementVO;
import com.judicature.databackend.vo.semantic.KnowledgeGraphVO;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Gabri
 */
@Service
public class KnowledgeGraphServiceImpl implements KnowledgeGraphService {

    private volatile static KnowledgeGraphService singleInstance;

    private static final QueryDAO queryDAO;

    static {
        queryDAO = QueryDAOImpl.getInstance();
    }

    /**
     * 私有化构造方法，实现单例模式
     */
    private KnowledgeGraphServiceImpl() {
    }

    public static KnowledgeGraphService getInstance() {
        if (singleInstance == null) {
            synchronized (KnowledgeGraphServiceImpl.class) {
                if (singleInstance == null) {
                    singleInstance = new KnowledgeGraphServiceImpl();
                }
            }
        }
        return singleInstance;
    }


    @Override
    public List<KnowledgeGraphVO> getKnowledgeGraphVO(List<PolysemantNamedEntity> polysemantNamedEntities) {
        List<KnowledgeGraphVO> knowledgeGraphVos = new ArrayList<>();
        int subjectId = 0;
        int objectId = 0;
        for (PolysemantNamedEntity polysemantNameEntity : polysemantNamedEntities) {
            KnowledgeGraphVO knowledgeGraphVO = new KnowledgeGraphVO();
            // 先查询其等价实体
            String sameEntityUuid = null;
            /* 如果该实体为实体别名 */
            if ("0".equals(polysemantNameEntity.getIsAliases())) {
                sameEntityUuid = queryDAO.querySameIndividual(polysemantNameEntity.getUUID());
            }

            // 得到等价实体名
            String entityUuid = sameEntityUuid == null ? polysemantNameEntity.getUUID() : sameEntityUuid;

            // StmtIterator propertyStatements = queryDAO.queryIndividualProperties(polysemantNameEntity.getUUID())
            List<Statement> propertyStatements = queryDAO.queryIndividualMainProperties(entityUuid);
            List<PolysemantNamedEntity> subjectPolysemantNamedEntities = new ArrayList<>();
            // 控制前端的结点个数 20
            int count = 0;
            for (Statement propertyStatement : propertyStatements) {
                if (count < 10) {
                    KnowledgeGraphStatementVO knowledgeGraphStatementVO = new KnowledgeGraphStatementVO();
                    KnowledgeGraphNodeVO subject = new KnowledgeGraphNodeVO();
                    KnowledgeGraphNodeVO predicate = new KnowledgeGraphNodeVO();
                    KnowledgeGraphNodeVO object = new KnowledgeGraphNodeVO();
                    subject.setId(subjectId);
                    subject.setName(polysemantNameEntity.getOntClass() + ":" + polysemantNameEntity.getEntityName());
                    subject.setShape("dot");
                    subject.setColor("red");
                    subject.setAlpha(1);
                    subjectPolysemantNamedEntities.add(polysemantNameEntity);
                    subject.setPolysemantNamedEntities(subjectPolysemantNamedEntities);
                    RDFNode objectNode = propertyStatement.getObject();
                    String[] name = propertyStatement.getPredicate().getURI().split("#");
                    String predicateName = null;
                    if (name.length > 1) {
                        predicateName = name[1];
                    } else {
                        predicateName = name[0];
                    }
                    predicate.setName(predicateName);
                    predicate.setColor("green");
                    predicate.setSize(15.0);
                    predicate.setAlpha(1);

                    // 是否为文本（以此标识区分对象属性和数据属性）
                    if (!"type".equals(predicateName) && !"有picSrc".equals(predicateName)) {
                        if (objectNode.isLiteral()) {
                            object.setId(objectId);
                            object.setName(objectNode.toString());
                            // 数据属性为矩形
                            object.setShape("rect");
                            object.setColor("#a7af00");
                            // TODO alpha为0表示不可见 1表示可见
                            object.setAlpha(1);
                        } else {
                            object.setId(objectId);
                            String[] objectNodeValueArr = objectNode.toString().split("#");
                            String objectNodeValue = null;
                            if (objectNodeValueArr.length > 1) {
                                objectNodeValue = objectNodeValueArr[1];
                            } else {
                                objectNodeValue = objectNodeValueArr[0];
                            }
                            String objectName = null;
                            // 该属性值不是中文且字符长度为32  则表示UUID
                            if (!StringHandle.isIncludeChinese(objectNodeValue) && objectNodeValue.length() == 32) {
                                objectName = queryDAO.queryIndividualComment(objectNodeValue);
                            } else {
                                objectName = objectNodeValue;
                            }
                            object.setName(objectName);
                            // 对象属性为圆形
                            object.setShape("dot");
                            for (PolysemantNamedEntity polysemantEntityForColor : polysemantNamedEntities) {
                                if (objectNode.toString().split("#")[1].equals(polysemantEntityForColor.getEntityName())) {
                                    object.setColor("red");
                                }
                            }
                            if (object.getColor() == null) {
                                object.setColor("#b2b19d");
                            }
                            object.setAlpha(1);
                        }
                        knowledgeGraphStatementVO.setSubject(subject);
                        knowledgeGraphStatementVO.setPredicate(predicate);
                        knowledgeGraphStatementVO.setObject(object);
                        knowledgeGraphVO.getKnowledgeGraphStatements().add(knowledgeGraphStatementVO);
                        ++subjectId;
                        ++objectId;
                    }
                }
                ++count;
            }
            knowledgeGraphVos.add(knowledgeGraphVO);
        }
        return knowledgeGraphVos;
    }
}
