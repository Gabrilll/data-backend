package com.judicature.databackend.blImpl.semantic.ontology;

import com.judicature.databackend.bl.semantic.ConstructionDealingService;
import com.judicature.databackend.config.SemanticConfig;
import com.judicature.databackend.data.NodeRepository;
import com.judicature.databackend.data.RelationRepository;
import com.judicature.databackend.data.semantic.ConstructionDAO;
import com.judicature.databackend.data.semantic.ConstructionDAOImpl;
import com.judicature.databackend.enums.OntologyClassEnum;
import com.judicature.databackend.model.BaikePage;
import com.judicature.databackend.util.FileIOUtil;
import com.judicature.databackend.util.LemmaClassify;
import com.judicature.databackend.util.VO2PO;
import com.judicature.databackend.vo.*;
import com.judicature.databackend.vo.semantic.OntologyVO;
import com.hankcs.hanlp.corpus.io.IOUtil;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Gabri
 */
@Service
@Component
public class ConstructionDealingServiceImpl implements ConstructionDealingService {


    ConstructionDAO constructionDAO=new ConstructionDAOImpl();

    SemanticConfig semanticConfig = SemanticConfig.getInstance();

    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    RelationRepository relationRepository;



    @Override
    public boolean dealProperty(Individual individual, BaikePage baikePage, boolean addClass, List<String> names, OntologyClassEnum ontologyClassEnum) {
        List<String> property;
        int index = 0;
        OntologyVO individualOntology=queryIndividual(baikePage.getLemmaTitle(),baikePage.getPolysemantExplain(),baikePage.getUrl(),false,ontologyClassEnum);
        NodeVO individualNode=individualOntology.getNodeVO();
        /*遍历当前实体属性名*/
        for (String parameterName : baikePage.getParameterNames()) {

            /*以’、‘分割属性值*/
            property = Arrays.asList(baikePage.getParameterValues().get(index).split("、"));
            /*如果没有’、‘，以’，‘分割*/
            if (property.size() == 1) {
                property = Arrays.asList(baikePage.getParameterValues().get(index).split("，"));
            }
            /*遍历当前属性的所有属性值*/
            for (String pro : property) {
                String url = null;
                int i = 0;

                /*当前页面所有超链接字段*/
                for (String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
                    /*如果超链接字段和属性值存在包含关系，添加一段关系*/
                    if (pro.contains(parameterHasUrlValue)||parameterHasUrlValue.contains(pro)) {
                        pro=parameterHasUrlValue;
                        url = baikePage.getParameterHasUrl().get(i);
                    }
                    ++i;
                }

                if (url != null) {
                    String polysemantExplain = "待更新";
                    OntologyClassEnum proClass= LemmaClassify.proClassify(pro);
                    /*查询属性实体*/
                    OntologyVO propertyOntology = this.queryIndividual(pro, polysemantExplain, url, true, proClass);
                    NodeVO nodeVO=propertyOntology.getNodeVO();
                    if(nodeVO!=null&&individualNode!=null&&nodeVO.getIdentity()!=null&&individualNode.getIdentity()!=null){
                        GraphVO graphVO=ConstructionGraph.getGraph();
                        if(graphVO.findNodeByUUID(nodeVO.getPropertyValueByName("UUID"))==null){
                            if(nodeVO.getIdentity()!=null){
                                graphVO.addNode(nodeVO);
                            }
                        }
                        if(graphVO.findNodeByUUID(individualNode.getPropertyValueByName("UUID"))==null){
                            if(individualNode.getIdentity()!=null){
                                graphVO.addNode(individualNode);
                            }
                        }
                        System.out.println("add relation");
                        RelationVO relationVO=new RelationVO();
                        relationVO.setStart(individualNode.getIdentity());
                        relationVO.setEnd(nodeVO.getIdentity());
                        relationVO.setType(parameterName);
                        Object tmp= relationRepository.addRelation(VO2PO.toRelation(relationVO));
                        Long id;
                        if(tmp!=null){
                            id=(long)tmp;
                        }else{
                            id= relationRepository.getRelationByTriplet(relationVO.getStart(),relationVO.getEnd(),parameterName);
                        }
                        if(id>0){
                            ConstructionGraph.getGraph().addRelation(relationVO);
                            relationVO.setIdentity(id);
                        }
                    }
                    constructionDAO.addObjectProperty(individual, parameterName, propertyOntology.getIndividual());
                }
            }
            ++index;
        }

        return true;
    }

    @Override
    public OntologyVO queryIndividual(String individualName, String polysemantExplain, String url, boolean isAliases, OntologyClassEnum parentClass) {
        // 以生省内存的方式读取Answer_Dict词典
        LinkedList<String> dictIndividualList = IOUtil.readLineListWithLessMemory(semanticConfig.getConstruct_individualDictPath());
        Individual individual = null;
        NodeVO nodeVO = null;
        Long rowNum = 0L;
        String uuid=null;
        // 遍历词典中的实体记录 判断当前实体是否已经存在
        for (String row : dictIndividualList) {
            ++rowNum;
            String[] fieldsDict = row.split("_");
            if (fieldsDict.length < 6) {
                continue;
            }
            // UUID
            String dictIndividualUUID = fieldsDict[0];
            // 实体名
            String dictIndividualName = fieldsDict[1];
            // 歧义说明
            String dictPolysemantExplain = fieldsDict[2];
            // 实体百科页面URL
            String dictIndividualURL = fieldsDict[3];
            // 是否是本名
            String dictIsAliasesWrite = fieldsDict[4];
            // 实体所属类型
//            int dictIndividualClass = Integer.parseInt(fieldsDict[5]);
            // 如果词典中歧义理解字段为待更新
            // 第一种情况：如果找到实体名相同并且明确指出该实体没有歧义则   该实体就是当前迭代到的实体
            if (individualName.equals(dictIndividualName) && "无".equals(dictPolysemantExplain)) {
                individual = constructionDAO.getIndividual(dictIndividualUUID);
                nodeVO=VO2PO.toNodeVO(nodeRepository.getNodeByUUID(dictIndividualUUID));
                if(nodeVO!=null && ConstructionGraph.getGraph().findNodeByUUID(dictIndividualUUID)==null){
                    if(nodeVO.getIdentity()!=null){
                        ConstructionGraph.getGraph().addNode(nodeVO);
                    }
                }
                uuid=dictIndividualUUID;
                break;
                // 找到完全相同的实体了 使用#去除所有框架定位网页
            } else if (individualName.equals(dictIndividualName) && url.split("#")[0].equals(dictIndividualURL) ) {
                // 如果此时抓到的实体歧义不为空 则表示该实体有同名实体 则更新词典 TODO 应该把 != null 去掉
                if ("待更新".equals(dictPolysemantExplain)) {
                    if (polysemantExplain == null) {
                        polysemantExplain = "无";
                    }
                    // 更新词典 修改歧义说明字段
                    row = dictIndividualUUID + "_" + dictIndividualName + "_" + polysemantExplain + "_" + dictIndividualURL + "_" + dictIsAliasesWrite + "_" + parentClass.getIndex();
                    // 更新Answer_Dict
                    FileIOUtil.updateContent(semanticConfig.getConstruct_individualDictPath(), rowNum, row);
                }
                // 获取该实体
                individual = constructionDAO.getIndividual(dictIndividualUUID);
                nodeVO=VO2PO.toNodeVO(nodeRepository.getNodeByUUID(dictIndividualUUID));
                if(nodeVO!=null && ConstructionGraph.getGraph().findNodeByUUID(dictIndividualUUID)==null){
                    if(nodeVO.getIdentity()!=null){
                        ConstructionGraph.getGraph().addNode(nodeVO);
                    }
                }
                uuid=dictIndividualUUID;
                if(individual!=null && nodeVO !=null){
                    break;
                }
            }
        }

        // 如果词典中不存在该实体，则插入词典并且创建一个实体
        if (individual == null) {
            String individualUUID = UUID.randomUUID().toString().replace("-", "");
            String isAliasesWrite = null;
            if (isAliases) {
                isAliasesWrite = "1";
            } else {
                isAliasesWrite = "0";
            }
            if (polysemantExplain == null) {
                polysemantExplain = "无";
            }
            String row_add_individual = individualUUID + "_" + individualName + "_" + polysemantExplain + "_" + url.split("#")[0] + "_" + isAliasesWrite + "_" + parentClass.getIndex();
            FileIOUtil.appendContent(semanticConfig.getConstruct_individualDictPath(), row_add_individual);
            // 获取具体类型
            OntClass concreteClass = constructionDAO.getOntClass(parentClass.getName());
            individual = constructionDAO.createIndividual(individualUUID, concreteClass);

            /*新增一个节点*/
            List<String> labels=new ArrayList<>();
            labels.add(parentClass.getName());
            nodeVO=createNode(individualUUID,individualName,labels,new ArrayList<>(),new ArrayList<>());
            uuid=individualUUID;

        }
        // 创建comment
        constructionDAO.addComment(individual, individualName);

        OntologyVO ontologyVO = new OntologyVO();
        ontologyVO.setIndividual(individual);
        if(nodeVO==null){
            List<String> labels=new ArrayList<>();
            labels.add(parentClass.getName());
            nodeVO=createNode(uuid,individualName,labels,new ArrayList<>(),new ArrayList<>());
        }
        ontologyVO.setNodeVO(nodeVO);

        return ontologyVO;
    }

    /**
     * 查询并返回实体
     *
     * @param individualName
     * @param polysemantExplain
     * @param url
     * @param isAliases
     * @param parentClass
     * @return
     */
    @Override
    public OntologyVO queryIndividual(String individualName, String polysemantExplain, String url, boolean isAliases, OntologyClassEnum parentClass, List<String> propertyNames, List<String> propertyValues) {
        // 以生省内存的方式读取Answer_Dict词典
        LinkedList<String> dictIndividualList = IOUtil.readLineListWithLessMemory(semanticConfig.getConstruct_individualDictPath());
        Individual individual = null;
        NodeVO nodeVO = null;
        Long rowNum = 0L;
        String uuid=null;
        // 遍历词典中的实体记录 判断当前实体是否已经存在
        for (String row : dictIndividualList) {
            ++rowNum;
            String[] fieldsDict = row.split("_");
            if (fieldsDict.length < 6) {
                continue;
            }
            // UUID
            String dictIndividualUUID = fieldsDict[0];
            // 实体名
            String dictIndividualName = fieldsDict[1];
            // 歧义说明
            String dictPolysemantExplain = fieldsDict[2];
            // 实体百科页面URL
            String dictIndividualURL = fieldsDict[3];
            // 是否是本名
            String dictIsAliasesWrite = fieldsDict[4];
            // 实体所属类型
//            int dictIndividualClass = Integer.parseInt(fieldsDict[5]);
            // 如果词典中歧义理解字段为待更新
            // 第一种情况：如果找到实体名相同并且明确指出该实体没有歧义则   该实体就是当前迭代到的实体
            if (individualName.equals(dictIndividualName) && "无".equals(dictPolysemantExplain)) {
                individual = constructionDAO.getIndividual(dictIndividualUUID);
                nodeVO=VO2PO.toNodeVO(nodeRepository.getNodeByUUID(dictIndividualUUID));
                if(nodeVO!=null && ConstructionGraph.getGraph().findNodeByUUID(dictIndividualUUID)==null){
                    if(nodeVO.getIdentity()!=null){
                        ConstructionGraph.getGraph().addNode(nodeVO);
                    }
                }
                uuid=dictIndividualUUID;
                break;
                // 找到完全相同的实体了 使用#去除所有框架定位网页
            } else if (individualName.equals(dictIndividualName) && url.split("#")[0].equals(dictIndividualURL) ) {
                // 如果此时抓到的实体歧义不为空 则表示该实体有同名实体 则更新词典 TODO 应该把 != null 去掉
                if ("待更新".equals(dictPolysemantExplain)) {
                    if (polysemantExplain == null) {
                        polysemantExplain = "无";
                    }
                    // 更新词典 修改歧义说明字段
                    row = dictIndividualUUID + "_" + dictIndividualName + "_" + polysemantExplain + "_" + dictIndividualURL + "_" + dictIsAliasesWrite + "_" + parentClass.getIndex();
                    // 更新Answer_Dict
                    FileIOUtil.updateContent(semanticConfig.getConstruct_individualDictPath(), rowNum, row);
                }
                // 获取该实体
                individual = constructionDAO.getIndividual(dictIndividualUUID);
                nodeVO=VO2PO.toNodeVO(nodeRepository.getNodeByUUID(dictIndividualUUID));
                if(nodeVO!=null && ConstructionGraph.getGraph().findNodeByUUID(dictIndividualUUID)==null){
                    if(nodeVO.getIdentity()!=null){
                        ConstructionGraph.getGraph().addNode(nodeVO);
                    }
                }
                uuid=dictIndividualUUID;
                if(individual!=null && nodeVO !=null){
                    break;
                }
            }
        }

        // 如果词典中不存在该实体，则插入词典并且创建一个实体
        if (individual == null) {
            String individualUUID = UUID.randomUUID().toString().replace("-", "");
            String isAliasesWrite = null;
            if (isAliases) {
                isAliasesWrite = "1";
            } else {
                isAliasesWrite = "0";
            }
            if (polysemantExplain == null) {
                polysemantExplain = "无";
            }
            String row_add_individual = individualUUID + "_" + individualName + "_" + polysemantExplain + "_" + url.split("#")[0] + "_" + isAliasesWrite + "_" + parentClass.getIndex();
            FileIOUtil.appendContent(semanticConfig.getConstruct_individualDictPath(), row_add_individual);
            // 获取疾病类型
            OntClass concreteClass = constructionDAO.getOntClass(parentClass.getName());
            individual = constructionDAO.createIndividual(individualUUID, concreteClass);

            /*新增一个节点*/
            List<String> labels=new ArrayList<>();
            labels.add(parentClass.getName());
            nodeVO=createNode(individualUUID,individualName,labels,propertyNames,propertyValues);
            uuid=individualUUID;

        }

        // 创建comment
        constructionDAO.addComment(individual, individualName);

        if(nodeVO==null){
            List<String> labels=new ArrayList<>();
            labels.add(parentClass.getName());
            nodeVO=createNode(uuid,individualName,labels,propertyNames,propertyValues);
        }

        OntologyVO ontologyVO=new OntologyVO();
        ontologyVO.setIndividual(individual);
        ontologyVO.setNodeVO(nodeVO);
        return ontologyVO;
    }

    public NodeVO createNode(String uuid,String name,List<String> labels,List<String> propertyNames,List<String> propertyValues){
        System.out.println("name:"+name);
        System.out.println("uuid:"+uuid);
        NodeVO nodeVO = new NodeVO();
        nodeVO.addProperty("UUID", uuid);
        nodeVO.addProperty("name",name);
        nodeVO.setLabels(labels);
        Map<String, String> properties = new HashMap<>();
        for (int i = 0; i < propertyNames.size(); i++) {
            properties.put(propertyNames.get(i), propertyValues.get(i));
        }

        nodeVO.addProperties(properties);
        long id= nodeRepository.addNode(VO2PO.toNode(nodeVO));
        if(id>0){
            ConstructionGraph.getGraph().addNode(nodeVO);
            nodeVO.setIdentity(id);
        }

        return nodeVO;
    }

}
