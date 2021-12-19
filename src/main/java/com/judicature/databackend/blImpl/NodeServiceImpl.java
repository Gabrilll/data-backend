package com.judicature.databackend.blImpl;

import com.judicature.databackend.bl.HistoryService;
import com.judicature.databackend.bl.NodeService;
import com.judicature.databackend.data.GraphRepository;
import com.judicature.databackend.data.NodeRepository;
import com.judicature.databackend.enums.OperationType;
import com.judicature.databackend.mongodb.DocumentRepository;
import com.judicature.databackend.po.Document;
import com.judicature.databackend.po.Node;
import com.judicature.databackend.util.ChineseCharToEnUtil;
import com.judicature.databackend.util.VO2PO;
import com.judicature.databackend.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.CollationKey;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NodeServiceImpl implements NodeService {
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    HistoryService historyService;
    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    GraphRepository graphRepository;

    /**
     * 存储全局搜索的节点
     */
    private Map<String, Long> searchNodes = new HashMap<>();

    /**
     * 存储层级目录的节点
     */
    private Map<String, Map<String, Map<String, List<NodeVO>>>> res = new HashMap<>();

    /**
     * 添加节点
     *
     * @param nodeVO node
     * @return response
     */
    @Override
    public ResponseVO addNode(NodeVO nodeVO, int graphId) {
        Node node = VO2PO.toNode(nodeVO);
        try {
            long identity = nodeRepository.addNode(node);
            HistoryVO history = new HistoryVO(OperationType.AddNode, identity);
            historyService.addHistory(history);
            nodeVO.setIdentity(identity);
            GraphList.getGraph(graphId).addNode(nodeVO);
            return ResponseVO.buildSuccess(identity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("实体创建失败");
        }
    }

    /**
     * 删除节点
     *
     * @param identity id
     * @return response
     */
    @Override
    public ResponseVO deleteNode(Long identity, int graphId) {
        HistoryVO historyVO = new HistoryVO(OperationType.DeleteNode, identity);
        try {
            nodeRepository.deleteNode(identity);
            historyService.addHistory(historyVO);
            GraphList.getGraph(graphId).deleteNode(identity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("节点删除失败");
        }
        return ResponseVO.buildSuccess(true);
    }

    /**
     * 更新节点信息
     *
     * @param nodeVO node
     * @return response
     */
    @Override
    public ResponseVO updateNode(NodeVO nodeVO, int graphId) {
        Node node = VO2PO.toNode(nodeVO);
        if (nodeVO.getIdentity().equals(54994L)) {
            for (String key : nodeVO.getProperties().keySet()) {
                System.out.println(nodeVO.getProperties().get(key));
            }
        }
        HistoryVO historyVO = new HistoryVO(OperationType.UpdateNode, nodeVO.getIdentity(), nodeVO.getProperties());
        try {
            nodeRepository.updateNode(node);
            historyService.addHistory(historyVO);
            NodeVO updateVO = VO2PO.toNodeVO(nodeRepository.getNodeById(nodeVO.getIdentity()));
            GraphList.getGraph(graphId).updateNode(updateVO);
            return ResponseVO.buildSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("更新失败");
        }
    }

//    @Override
//    public List<NodeVO> getNodeByRe(RelationVO relationVO) {
//        return null;
//    }

    /**
     * 获取所有节点
     *
     * @return nodeVOs
     */
    @Override
    public List<NodeVO> getAllNodes() {
        try {
            List<Node> res = nodeRepository.getAllNodes();
            return res.stream().map(VO2PO::toNodeVO).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 获取和特定节点有关系的所有节点
     *
     * @param identity id
     * @return nodeVOs
     */
    @Override
    public List<NodeVO> getSubNodes(Long identity) {
        List<Node> res = nodeRepository.getSubNodes(identity);
        return res.stream().map(VO2PO::toNodeVO).collect(Collectors.toList());
    }

    @Override
    public NodeVO getNodeById(long id) {
        try {
            return VO2PO.toNodeVO(nodeRepository.getNodeById(id));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public NodeVO getNodeByName(String name) {
        try {
            return VO2PO.toNodeVO(nodeRepository.getNodeByName(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Map<String,<Map<char,List<NodeVO>>>>
     *
     * @return
     */
    @Override
    public ResponseVO getNodesList() {
        if (!res.isEmpty()) {
            return ResponseVO.buildSuccess(res);
        }
        String[] labelsList = new String[]{"刑事案件", "敲诈勒索",
                "医疗事故", "人民检察院", "人民法院", "仲裁", "公共交通"
        };

        Map<String, Map<String, List<NodeVO>>> diseaseList = new HashMap<>();
        Map<String, Map<String, List<NodeVO>>> medicineList = new HashMap<>();
        Map<String, Map<String, List<NodeVO>>> list = new HashMap<>();

        for (String s : labelsList) {
            System.out.println("类别：" + s);
            List<NodeVO> nodes = getNodesByLabel(s);
            System.out.println("总数：" + nodes.size());
            Map<String, List<NodeVO>> v = new HashMap<>();
//            Map<String,Map<String,List<NodeVO>>> nodesList = new HashMap<>();
//            List<NodeVO> containList = new ArrayList<>();
            String key = "a";
            int i = 0;
            while (i < nodes.size() && key.charAt(0) <= 'z') {
                List<NodeVO> containList = new ArrayList<>();
                String alphabet = nodes.get(i).getProperties().get("name");
                String tempKey = ChineseCharToEnUtil.getFirstSpell(alphabet).substring(0, 1);
                if (tempKey.charAt(0) < 'a' || tempKey.charAt(0) > 'z') {
                    i++;
                    continue;
                }
                while (tempKey.equals(key) && i < nodes.size()) {
                    NodeVO n = nodes.get(i);
                    Map<String, String> pro = new HashMap<>();
                    pro.put("name", n.getProperties().get("name"));
                    n.setProperties(pro);
                    containList.add(n);
                    i++;
                    if (i == nodes.size()) {
                        v.put(key, containList);
                        break;
                    }
                    tempKey = ChineseCharToEnUtil.getFirstSpell(nodes.get(i).getProperties().get("name")).substring(0, 1);
                }
                v.put(key, containList);
                key = String.valueOf((char) (key.charAt(0) + 1));
            }
            while (key.charAt(0) <= 'z') {
                List<NodeVO> emptyList = new ArrayList<>();
                v.put(key, emptyList);
                key = String.valueOf((char) (key.charAt(0) + 1));
            }
//            nodesList.put(s, v);

            if (s.equals("疾病") || s.equals("科室")) {
                diseaseList.put(s, v);
                if (s.equals("科室")) {
                    res.put("疾病", diseaseList);
                }
            } else if (s.equals("症状")) {
                list.put("症状", v);
                res.put("病症表现", list);
            } else {
                medicineList.put(s, v);
                if (s.equals("抗微生物药")) {
                    res.put("药物", medicineList);
                }
            }
        }
        return ResponseVO.buildSuccess(res);
    }

    @Override
    public List<NodeVO> getNodesByLabel(String label) {
        try {
            List<Node> res = nodeRepository.getNodeByLabel(label);
            List<NodeVO> nodes = res.stream().map(VO2PO::toNodeVO).sorted(new Comparator<NodeVO>() {
                Collator collator = Collator.getInstance(Locale.CHINA);

                @Override
                public int compare(NodeVO o1, NodeVO o2) {
                    CollationKey key1 = collator.getCollationKey(o1.getProperties().get("name"));
                    CollationKey key2 = collator.getCollationKey(o2.getProperties().get("name"));
                    return key1.compareTo(key2);
                }
            }).collect(Collectors.toList());
            return nodes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public ResponseVO getSearchNodes() {
        if (searchNodes.isEmpty()) {
            List<NodeVO> diseaseList = getNodesByLabel("疾病");
            List<NodeVO> symptomList = getNodesByLabel("症状");
            if (diseaseList != null) {
                for (NodeVO nodeVO : diseaseList) {
                    searchNodes.put(nodeVO.getProperties().get("name"), nodeVO.getIdentity());
                }
            }
            if (symptomList != null) {
                for (NodeVO nodeVO : symptomList) {
                    searchNodes.put(nodeVO.getProperties().get("name"), nodeVO.getIdentity());
                }
            }
            if (searchNodes.isEmpty()) {
                ResponseVO.buildFailure("数据库连接失败！");
            }
            return ResponseVO.buildSuccess(searchNodes);
        } else {
            return ResponseVO.buildSuccess(searchNodes);
        }
    }

    @Override
    public List<DocumentVO> recommend(String name) {
//        NodeVO doc = VO2PO.toNodeVO(nodeRepository.getNodeByName(name));
//        List<NodeVO> nodes = nodeRepository.getNodeByLabel("裁判文书").stream().map(VO2PO::toNodeVO).collect(Collectors.toList());
//        log.info("总计" + nodes.size() + "份裁判文书");
//        Map<String, Double> similarity = new HashMap<>();
//        for (NodeVO n : nodes) {
//            if (Objects.equals(n.getIdentity(), doc.getIdentity())) {
//                continue;
//            }
//            similarity.put(n.getPropertyValueByName("name"), calculateSimilarity(n.getPropertyValueByName("name"), doc.getIdentity()));
//        }
//        List<Map.Entry<String, Double>> entryArrayList = new ArrayList<>(similarity.entrySet());
//        entryArrayList.sort(Map.Entry.comparingByValue());
//        List<DocumentVO> documentVOS = new ArrayList<>();
//        for (Map.Entry<String, Double> entry : entryArrayList.subList(0, 5)) {
//            Document document = documentRepository.findDistinctByName(entry.getKey());
//            DocumentVO documentVO = new DocumentVO(document.getId(), document.getName(), new ArrayList<>(document.getKeywords().keySet()), document.getText());
//            documentVOS.add(documentVO);
//        }
        List<String> docs = nodeRepository.getNearestDocs(name);
        log.info(String.valueOf(docs.size()));
        List<Document> res = documentRepository.findDocumentByName(docs);
        return res.stream().map(d -> new DocumentVO(d.getId(), d.getName(), new ArrayList<>(d.getKeywords().keySet()), d.getText())).collect(Collectors.toList()).subList(0, 5);
    }

    private double calculateSimilarity(String n1, Long id2) {

        Document d1 = documentRepository.findDistinctByName(n1);
//        Date date = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//        log.info(dateFormat.format(date));
        Map<String, Double> keywords = d1.getKeywords();
        List<Long> res = nodeRepository.getKeyNodes(keywords);
        log.info(String.valueOf(res.size()));
        List<Double> weight = nodeRepository.getDistanceBetweenNodes(id2, res);

//        for (String k : keys) {
//            List<Long> nodes = nodeRepository.getKeyNodes(k);
//            nodes.addAll(nodeRepository.getKeyNodesByEdge(k));
//            nodes.removeIf(n -> Objects.equals(n, id2));
////            log.info(dateFormat.format(date));
//            List<Long> dis = nodeRepository.getDistanceBetweenNodes(id2, nodes);
//            for (long d : dis) {
//                if (d > 0) {
//                    weight += keywords.get(k) / d;
//                }
//            }
//
//        }
        log.info(n1 + ":" + weight);

        return weight.stream().reduce(Double::sum).get();
    }
}
