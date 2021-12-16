package com.judicature.databackend.blImpl;

import com.judicature.databackend.bl.QuestionService;
import com.judicature.databackend.bl.semantic.*;
import com.judicature.databackend.config.SemanticConfig;
import com.judicature.databackend.data.NodeRepository;
import com.judicature.databackend.model.*;
import com.judicature.databackend.po.Node;
import com.judicature.databackend.processor.AnswerProcessor;
import com.judicature.databackend.util.Alias;
import com.judicature.databackend.util.Dict;
import com.judicature.databackend.util.VO2PO;
import com.judicature.databackend.vo.NodeVO;
import com.judicature.databackend.vo.QAndR;
import com.judicature.databackend.vo.ResponseVO;
import com.judicature.databackend.vo.SemanticAnswerVO;
import com.judicature.databackend.vo.semantic.AnswerVO;
import com.judicature.databackend.vo.semantic.KnowledgeGraphVO;
import com.judicature.databackend.vo.semantic.PolysemantSituationVO;
import com.judicature.databackend.vo.semantic.ShortAnswerVO;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能问答、个性化推荐实现
 */
@Service
public class QuestionServiceImpl implements QuestionService {

//    @Autowired
//    AnswerProcessor queryProcessor;

    /** relatedNode存储最近一次查询的节点 */
    NodeVO relatedNode;

    /** 存储最近依次查询模板号 modelIndex*/
    private int modelIndex;

    List<String> recommendLabelsPeo = new ArrayList<>(Arrays.asList("儿童","学生","孕妇","老年人","农民工","伤残人士","环卫工人"));
    List<String> recommendLabelsPla = new ArrayList<>(Arrays.asList("出租汽车","城市轨道交通", "民航","道路客运","私家车","商场","学校","超市","银行","医疗机构","家庭",
            "机关事业单位","宾馆","写字楼","养老院","企业","水路客运","城市公共汽电车","铁路客运"));

    private List<String> COVID = new ArrayList<>(Arrays.asList("新型冠状病毒肺炎","新冠","新冠肺炎","新型冠状肺炎"));

    private List<NodeVO> relatedDisease;


    @Autowired
    NodeServiceImpl nodeService;
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    WordSegmentationService wordSegmentationService;
    @Autowired
    GrammarParserService grammarParserService;
    @Autowired
    SemanticGraphService semanticGraphService;
    @Autowired
    QueryService queryService;
    @Autowired
    NamedEntityService namedEntityService;
    @Autowired
    KnowledgeGraphService knowledgeGraphService;

    public List<String> dict= Dict.getDict();
    public Map<String,String> matchHis= Alias.getAlias();


    @Override
    public QAndR answer(String question) throws Exception {
        QAndR qAndR = new QAndR();
        if (question.equals("新冠，整体预防措施")){
            StringBuilder res = new StringBuilder();
            findPreventMethods(res, recommendLabelsPeo);
            findPreventMethods(res, recommendLabelsPla);
            modelIndex = 8;
            List<String> rQ = recommendQue(question);
            qAndR.setRecommend(rQ);
            qAndR.setAnswer(res.toString());
            return qAndR;
        }
        AnswerProcessor queryProcessor = new AnswerProcessor();
        List<String> queryStr = queryProcessor.analysis(question);
        modelIndex = Integer.parseInt(queryStr.get(0));

        StringBuilder res = new StringBuilder();
        switch (modelIndex) {
            /** 疾病相关问题：可以直接查询节点属性的内容 */
            case 0: case 1: case 2: case 3:case 4: case 5: case 6: case 7: case 8:{
                String diseaseName = queryStr.get(1);
                if (modelIndex == 8 && COVID.contains(diseaseName)) {
                    res.append("如果您想咨询新冠期间的具体预防措施，请输入：新冠，整体预防措施\n" +
                            "或者您想咨询具体个人预防措施或场所防护措施，您可以试试下列问题哦~");
                    break;
                }
                if (COVID.contains(diseaseName)){
                    diseaseName = "新型冠状病毒肺炎";
                }
                String propertyName = "";
                if (queryStr.size() > 2){
                    propertyName = queryStr.get(2);
                }
                else {
                    break;
                }
                if(relatedNode == null || !relatedNode.getProperties().get("name").equals(diseaseName)){
                    relatedNode = nodeService.getNodeByName(diseaseName);
                }
                if (relatedNode == null){
                    qAndR.setRecommend(new ArrayList<>(Arrays.asList("快提些问题吧~")));
                    qAndR.setAnswer("信息不足，可以换个问题哦~");
                    return qAndR;
                }
                Map<String,String> propertyList = relatedNode.getProperties();
                String pValue = propertyList.get(propertyName);
                if (pValue == null){
                    break;
                }
                res.append(pValue);
                break;
            }
            /** 新冠预防措施问题——以节点形式存在*/
            case 9: case 10:{
                String preKind = queryStr.get(3);
                //打开同义词文件
                File file = new File(SemanticConfig.getInstance().toAbsolutePath("src/main/resources/statics/thesaurus.txt"));
                BufferedReader br = null;
                String line;
                try {
                    br = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException e1) {
                    System.err.println("文件不存在");
                    e1.printStackTrace();
                }
                //处理获取同义词Map
                Map<String,List<String>> thesaurusList = new HashMap<>();
                try {
                    while ((line = br.readLine()) != null) {
                        List<String> list = new ArrayList<>(Arrays.asList(line.split(",")));
                        thesaurusList.put(list.get(0),list);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (Map.Entry<String,List<String>> entry:thesaurusList.entrySet()){
                    if (entry.getValue().contains(preKind)){
//                        System.out.println(entry.getKey()+"预防措施");
                        List<NodeVO> nodes = nodeService.getNodesByLabel(entry.getKey()+"预防措施");
                        res.append(entry.getKey()).append("预防措施\n");
                        int idx = 1;
                        for(NodeVO node:nodes){
                            res.append(idx).append(":");
                            if (node.getProperties().get("措施描述") == null){
                                res.append(node.getProperties().get("措施主题")).append("\n");
                            }
                            else {
                                res.append(node.getProperties().get("措施描述")).append("\n");
                            }
                            idx++;
                        }
                    }
                }
                break;
            }
            /**疾病相关问题——可能需要查询节点间关系*/
            case 11: case 12:{
                String diseaseName = queryStr.get(1);
                if (COVID.contains(diseaseName)){
                    NodeVO nodeVO = nodeService.getNodeByName(COVID.get(0));
                    if (modelIndex == 11 && nodeVO.getProperties().get("就诊科室") != null){
                        res.append(COVID.get(0)).append("的就诊科室为(仅供参考)：").append(nodeVO.getProperties().get("就诊科室"));
                    }
                    else if (modelIndex == 12 && nodeVO.getProperties() != null){
                        res.append(COVID.get(0)).append("的易感人群为：").append(nodeVO.getProperties().get("易感人群"));
                    }
                    else {
                        break;
                    }
                }
                else if (modelIndex == 11){
                    Node node = nodeRepository.getNodeByStartAndRe(diseaseName,"科室");
                    NodeVO nodeVO = VO2PO.toNodeVO(node);
                    res.append(diseaseName).append("的就诊科室为(仅供参考)：").append(nodeVO.getProperties().get("name"));
                }
                else {
                    Node nodeHigh1 = nodeRepository.getNodeByStartAndRe(diseaseName,"高发人群");
                    Node nodeCommon1 = nodeRepository.getNodeByStartAndRe(diseaseName,"多发人群");
                    NodeVO nodeVO = nodeService.getNodeByName(diseaseName);
                    if (nodeHigh1 != null){
                        NodeVO nodeVOHigh = VO2PO.toNodeVO(nodeHigh1);
                        res.append(diseaseName).append("的高发人群为：").append(nodeVOHigh.getProperties().get("name"));
                    }
                    if (nodeCommon1 != null){
                        NodeVO nodeVOCommon = VO2PO.toNodeVO(nodeCommon1);
                        res.append(diseaseName).append("的多发人群为：").append(nodeVOCommon.getProperties().get("name"));
                    }
                    if (nodeVO != null){
                        if (nodeVO.getProperties().get("高发人群")!=null){
                            res.append(diseaseName).append("的高发人群为：").append(nodeVO.getProperties().get("高发人群"));
                        }
                        if (nodeVO.getProperties().get("多发人群")!=null){
                            res.append(diseaseName).append("的多发人群为：").append(nodeVO.getProperties().get("多发人群"));
                        }
                    }
                }

                break;

            }
            /** 药物相关问题*/

            /** 口罩相关问题*/
            case 13:{
                String maskName = queryStr.get(1);
                String pro = "";
                if (queryStr.size() > 2){
                    pro = queryStr.get(2);
                }
                else {
                    break;
                }
                NodeVO nodeVO = nodeService.getNodeByName(maskName);
                res.append(maskName).append(pro).append("：").append(nodeVO.getProperties().get(pro));
                break;
            }
            /** 根据描述症状，输出可能患的病*/
            case 14: {
                relatedDisease = new ArrayList<>();
                res.append("您可能患的病如下（仅供参考，详情请去医院检查）：");
                for (int i = 1;i<queryStr.size();i++){
                    //关系直接为”涉及疾病“
                    String appearName = queryStr.get(i);
                    List<NodeVO> list1 = findDisease(appearName);
                    for (NodeVO nodeVO:list1){
                        findDisease(nodeVO.getProperties().get("name"));
                    }
                }
                if(relatedDisease.size() == 0){
                    qAndR.setRecommend(new ArrayList<>(Arrays.asList("快提些问题吧~")));
                    qAndR.setAnswer("对不起，您的描述TT无法找到病症，请多加一点描述吧~");
                }
                else {
                    for (NodeVO nodeVO:relatedDisease){
                        res.append(nodeVO.getProperties().get("name")).append("；");
                    }
                }
                break;
            }
            case 15:
                qAndR.setAnswer(queryStr.get(1));
                qAndR.setRecommend(new ArrayList<>(Arrays.asList("请提些问题吧~")));
                break;
            default:
                break;
        }
        if (res.length() != 0){
            qAndR.setAnswer(res.toString());
            List<String> rQ = recommendQue(question);
            qAndR.setRecommend(rQ);
            return qAndR;
        }
        qAndR.setRecommend(new ArrayList<>(Arrays.asList("快提些问题吧~")));
        qAndR.setAnswer("信息不足，可以换个问题哦~");
        return qAndR;
    }

    private List<NodeVO> findDisease(String symName){
        List<NodeVO> res = new ArrayList<>();
        Node node = nodeRepository.getNodeByStartAndRe(symName,"涉及疾病");
        if(node != null){
            relatedDisease.add(VO2PO.toNodeVO(node));
        }
        List<Node> nodes1 = nodeRepository.getNodesByP(symName,"涉及症状");
        if (nodes1 != null){
            List<NodeVO> nodeVOS1 = nodes1.stream().map(VO2PO::toNodeVO).collect(Collectors.toList());
            for (NodeVO nodeVO: nodeVOS1){
                if(nodeVO.getLabels().get(0).equals("疾病")){
                    relatedDisease.add(nodeVO);
                }
                else{
                    res.add(nodeVO);
                }
            }
        }
        return res;
    }

    private void findPreventMethods(StringBuilder res, List<String> recommendLabelsPla) {
        for(String s: recommendLabelsPla){
            List<NodeVO> nodes = nodeService.getNodesByLabel(s+"预防措施");
            res.append(s).append("防护措施：\n");
            int idx = 1;
            for(NodeVO node:nodes){
                res.append(idx).append(".");
                if (node.getProperties().get("措施描述") == null){
                    res.append(node.getProperties().get("措施主题")).append("\n");
                }
                else {
                    res.append(node.getProperties().get("措施描述")).append("\n");
                }
                idx++;
            }
        }
    }

    @Override
    public List<String> recommendQue(String question) throws Exception {
        List<String> recommend = new ArrayList<>();
        if (modelIndex == 8 || modelIndex == 9 || modelIndex == 10 || question.equals("新冠，整体预防措施")){
            if (modelIndex == 9){
                for(String s:recommendLabelsPeo){
                    recommend.add("新冠期间，"+s+"该如何进行个人防护？");
                }
            }
            else if (modelIndex == 10){
                for(String s:recommendLabelsPla){
                    recommend.add("新冠期间，在"+s+"该如何进行场所防护？");
                    if (s.equals("家庭")){
                        break;
                    }
                }
            }else {
                for(String s:recommendLabelsPeo){
                    recommend.add("新冠期间，"+s+"该如何进行个人防护？");
                }
                for(String s:recommendLabelsPla){
                    recommend.add("新冠期间，在"+s+"该如何进行场所防护？");
                    if (s.equals("家庭")){
                        break;
                    }
                }
            }
            return recommend;
        }
        else if (modelIndex == 14){
            if (relatedDisease == null){
                recommend.add("快提些问题吧~");
                return recommend;
            }
            else {
                for (NodeVO nodeVO:relatedDisease){
                    String name = nodeVO.getProperties().get("name");
                    for (Map.Entry<String,String> entry:nodeVO.getProperties().entrySet()){
                        String proName = entry.getKey();
                        List<String> proList = new ArrayList<>(Arrays.asList("简介概述","发病机制","药物治疗","一般治疗","饮食禁忌","传染性","中医治疗","临床表现","预防","易感人群","高发人群","多发人群"));
                        if(proList.contains(proName)){
                            String que = name +"的"+entry.getKey();
                            recommend.add(que);
                        }
                    }
                }
                return recommend;
            }
        }

        if(relatedNode == null){
            recommend.add("快提些问题吧~");
            return recommend;
        }
        Map<String,String> propertyList = relatedNode.getProperties();
        String name = propertyList.get("name");
        for (Map.Entry<String,String> entry:propertyList.entrySet()){
            String proName = entry.getKey();
            List<String> proList = new ArrayList<>(Arrays.asList("简介概述","发病机制","药物治疗","一般治疗","饮食禁忌","传染性","中医治疗","临床表现","预防","易感人群","高发人群","多发人群"));
            if(proList.contains(proName)){
                String que = name +"的"+entry.getKey();
                recommend.add(que);
            }

        }
        return recommend;
    }

    /**
     * 语义搜索
     * @param question question
     * @return
     */
    @Override
    public ResponseVO semanticSearch(String question) {
        long askTime = System.currentTimeMillis();

        AnswerVO answerVO = new AnswerVO();
        answerVO.setAskTime(askTime);

        /*分词*/
        WordSegmentResult wordSegmentResult = wordSegmentationService.wordSegmentation(question);
        List<Term> terms = wordSegmentResult.getTerms();
        List<Word> words = wordSegmentResult.getWords();

        List<PolysemantNamedEntity> polysemantNamedEntities = wordSegmentResult.getPolysemantEntities();
        System.out.println("HanLP分词结果为:" + terms);


        /*依存句法分析*/
        CoNLLSentence coNLLSentence = grammarParserService.dependencyParser(terms);
        System.out.println("HanLP语法解析结果:\n" + coNLLSentence);

        /*语义图构建*/
        SemanticGraph semanticGraph = semanticGraphService.buildSemanticGraph(coNLLSentence, polysemantNamedEntities);
        /*无法解析*/
        if (semanticGraph.getAllVertices().isEmpty()) {
            semanticGraph = semanticGraphService.buildBackUpSemanticGraph(words);
        }

        /*语义图断言构建*/
        List<AnswerStatement> semanticStatements = queryService.createStatement(semanticGraph);

        /*获取歧义断言（同名实体）*/
        List<PolysemantStatement> polysemantStatements = queryService.createPolysemantStatements(semanticStatements);
        List<PolysemantSituationVO> polysemantSituationVOS = new ArrayList<>();

        for (PolysemantStatement polysemantStatement : polysemantStatements) {
            /*实体消歧*/
            List<AnswerStatement> individualDisambiguationStatements = queryService.individualsDisambiguation(polysemantStatement.getAnswerStatements());

            /*谓语消歧*/
            List<AnswerStatement> predicateDisambiguationStatements=queryService.predicateDisambiguation(individualDisambiguationStatements);

            /*构造查询断言*/
            List<AnswerStatement> queryStatements=queryService.createQueryStatements(predicateDisambiguationStatements);

            /*构造查询语句*/
            List<String> sparqls=queryService.createSparqls(queryStatements);
            List<QueryResult> queryResults=new ArrayList<>();

            for(String sparql:sparqls){
                QueryResult queryResult=queryService.queryOntology(sparql);
                List<Answer> answers=new ArrayList<>();
                for(Answer answer:queryResult.getAnswers()){
                    String[] uuids=answer.getContent().split(":");
                    String uuid=null;
                    if(uuids.length>1){
                        uuid=uuids[1];
                    }else{
                        uuid=uuids[0];
                    }
                    if(uuids.length<=1||answer.getContent().length()!=33){
                        answers.add(answer);
                    }else{
                        String comment=queryService.queryIndividualComment(uuid);
                        Answer ans=new Answer();
                        ans.setContent(comment);
                        answers.add(ans);
                    }
                }
                queryResult.setAnswers(answers);
                queryResults.add(queryResult);
            }

            PolysemantSituationVO polysemantSituationVO=new PolysemantSituationVO();
            polysemantSituationVO.setPolysemantStatement(polysemantStatement);
            polysemantSituationVO.setSemanticStatements(semanticStatements);

            List<PolysemantNamedEntity> activePolysemantNamedEntities=new ArrayList<>();
            int idx=0;

            for(AnswerStatement answerStatement:polysemantStatement.getAnswerStatements()){
                PolysemantNamedEntity subjectActivePolysemantNamedEntity=answerStatement.getSubject().acquireActiveEntity();
                PolysemantNamedEntity objectActivePolysemantNamedEntity=answerStatement.getObject().acquireActiveEntity();

                if(idx==0){
                    activePolysemantNamedEntities.add(subjectActivePolysemantNamedEntity);
                }
                activePolysemantNamedEntities.add(objectActivePolysemantNamedEntity);
                ++idx;
            }

            polysemantSituationVO.setActivePolysemantNamedEntities(activePolysemantNamedEntities);
            polysemantSituationVO.setIndividualsDisambiguationStatements(individualDisambiguationStatements);
            polysemantSituationVO.setPredicateDisambiguationStatements(predicateDisambiguationStatements);
            polysemantSituationVO.setQueryStatements(queryStatements);
            polysemantSituationVO.setSparqls(sparqls);
            polysemantSituationVO.setQueryResults(queryResults);
            polysemantSituationVOS.add(polysemantSituationVO);
        }

        namedEntityService.fillNamedEntities(polysemantNamedEntities);

        answerVO.setQuestion(question);
        long answerTime=System.currentTimeMillis();
        answerVO.setAnswerTime(answerTime);
        answerVO.setWords(words);

        ShortAnswerVO shortAnswer=new ShortAnswerVO();
        shortAnswer.setPolysemantSituationVOs(polysemantSituationVOS);
        answerVO.setShortAnswer(shortAnswer);

        List<KnowledgeGraphVO> knowledgeGraphVOS=new ArrayList<>();
        if(polysemantNamedEntities!=null){
            knowledgeGraphVOS=knowledgeGraphService.getKnowledgeGraphVO(polysemantNamedEntities);
        }
        answerVO.setKnowledgeGraphVos(knowledgeGraphVOS);

        SemanticAnswerVO semanticAnswerVO=new SemanticAnswerVO();
        if(!answerVO.getShortAnswer().getPolysemantSituationVOs().isEmpty()){
            semanticAnswerVO.setSemanticStatement(answerVO.getShortAnswer().getPolysemantSituationVOs().get(0).getSemanticStatements());
            semanticAnswerVO.setQueryResults(answerVO.getShortAnswer().getPolysemantSituationVOs().get(0).getQueryResults());
        }


        StringBuilder qa= new StringBuilder();
        for(Term t:terms){
            qa.append(t.word);
        }
        semanticAnswerVO.setSmartQA(qa.toString());
        return  ResponseVO.buildSuccess(semanticAnswerVO);

    }
}
