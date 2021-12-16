package com.judicature.databackend.blImpl.semantic;

import com.judicature.databackend.bl.semantic.NamedEntityService;
import com.judicature.databackend.data.semantic.QueryDAO;
import com.judicature.databackend.data.semantic.QueryDAOImpl;
import com.judicature.databackend.model.PolysemantNamedEntity;
import com.judicature.databackend.util.StringHandle;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gabri
 */
@Service
public class NamedEntityServiceImpl implements NamedEntityService {

    private volatile static NamedEntityService singleInstance;

    private static final QueryDAO queryDAO;

    static {
        queryDAO = QueryDAOImpl.getInstance();
    }

    /**
     * 私有化构造方法，实现单例模式
     */
    private NamedEntityServiceImpl() {
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static NamedEntityService getInstance() {
        if (singleInstance == null) {
            synchronized (NamedEntityServiceImpl.class) {
                if (singleInstance == null) {
                    singleInstance = new NamedEntityServiceImpl();
                }
            }
        }
        return singleInstance;
    }

    @Override
    public boolean fillNamedEntities(List<PolysemantNamedEntity> polysemantNamedEntities) {
        for (PolysemantNamedEntity polysemantNameEntity : polysemantNamedEntities) {
            // 先查询其等价实体 避免搜索星爷时 无法正确返回其属性
            String sameEntityUUID = queryDAO.querySameIndividual(polysemantNameEntity.getUUID());
            // 得到等价实体名
            String entityUUID = sameEntityUUID == null ? polysemantNameEntity.getUUID() : sameEntityUUID;
            // 查询该实体的所有属性
            StmtIterator propertyStatements = queryDAO.queryIndividualProperties(entityUUID);

            Map<String, String> dataProperties = new LinkedHashMap<>();
            Map<String, String> objectProperties = new LinkedHashMap<>();
            if (propertyStatements != null) {
                while (propertyStatements.hasNext()) {
                    Statement propertyStatement = propertyStatements.next();
                    // 属性名
                    String predicateNodeName = propertyStatement.getPredicate().getLocalName();
                    // 属性值
                    RDFNode objectNode = propertyStatement.getObject();
                    if ("有picSrc".equals(predicateNodeName)) {
                        // 设置图片Src
                        polysemantNameEntity.setPicSrc(objectNode.toString());
                    }
                    if ("有描述".equals(predicateNodeName)) {
                        // 设置描述
                        polysemantNameEntity.setLemmaSummary(objectNode.toString());
                    }
                    // 数据属性
                    if (objectNode.isLiteral()) {
                        dataProperties.put(predicateNodeName, objectNode.toString());
                    } else {
                        String objectNodeValue = objectNode.toString().split("#")[1];
                        String objectName = null;
                        // 该属性值不是中文且字符长度为32  则表示UUID
                        if (!StringHandle.isIncludeChinese(objectNodeValue) && objectNodeValue.length() == 32)
                        {
                            objectName = queryDAO.queryIndividualComment(objectNodeValue);
                        } else {
                            objectName = objectNodeValue;
                        }
                        objectProperties.put(predicateNodeName, objectName);
                    }
                }
            }
            polysemantNameEntity.setDataProperties(dataProperties);
            polysemantNameEntity.setObjectProperties(objectProperties);
        }
        return true;
    }
}