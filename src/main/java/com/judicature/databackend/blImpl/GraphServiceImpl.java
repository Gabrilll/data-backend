package com.judicature.databackend.blImpl;

import com.judicature.databackend.bl.GraphService;
import com.judicature.databackend.bl.NodeService;
import com.judicature.databackend.bl.RelationService;
import com.judicature.databackend.config.OSSConfig;
import com.judicature.databackend.crawler.OntologyConstructionLauncher;
import com.judicature.databackend.data.GraphRepository;
import com.judicature.databackend.data.RelationRepository;
import com.judicature.databackend.mongodb.DocumentRepository;
import com.judicature.databackend.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GraphServiceImpl implements GraphService {
    @Autowired
    NodeService nodeService;
    @Autowired
    RelationRepository relationRepository;
    @Autowired
    GraphRepository graphRepository;
    @Autowired
    RelationService relationService;
    @Autowired
    OntologyConstructionLauncher ontologyConstructionLauncher;
    @Autowired
    DocumentRepository documentRepository;

    /**
     * 随机获取子图
     *
     * @return response
     */
    @Override
    public ResponseVO getGraph() {
        GraphVO graphVO = Graph.getInstance();
        if (graphVO == null) {
            GraphVO graph = randomlyInitGraph();
            List<NodeVO> nodes = graph.getNodes();

            //此处已生成Set<NodeVO>,对NodeVO进行处理，修改cls属性,生成nodesNew
            GraphList.setCls(graph.getNodes());

            Graph.setInstance(graph);
            return ResponseVO.buildSuccess(graph);
        } else {
            return ResponseVO.buildSuccess(graphVO);
        }

    }

    /**
     * 导出xml文件
     *
     * @return response
     */
    @Override
    public ResponseVO exportXml() {
        try {
            boolean success = graphRepository.exportXml();
            if (!success) {
                return ResponseVO.buildFailure("导出失败");
            }
            String path = System.getenv("NEO4J_HOME");
            if (path == null) {
                path = "/var/lib/neo4j/import/test.graphml";
            } else {
                path += "/import/test.graphml";
            }

            OSSConfig.setObjectName("export.xml");
            int res = OSSConfig.upload(path);
            if (res == 1) {
                return ResponseVO.buildSuccess("https://sec123.oss-cn-shanghai.aliyuncs.com/export.xml");
            } else {
                return ResponseVO.buildFailure("导出失败");
            }
        } catch (Exception e) {
            return ResponseVO.buildFailure("导出失败");
        }
    }

    /**
     * 根据节点labels对节点进行过滤
     *
     * @param labels labels
     * @return response
     */
    @Override
    public ResponseVO filterByNodeLabels(List<String> labels, int graphId) {
        try {
            GraphVO graphVO = GraphList.getGraph(graphId);
            if (labels.isEmpty()) {
                return ResponseVO.buildSuccess(graphVO);
            }
            List<NodeVO> nodeVOs = new ArrayList<>();
            List<RelationVO> relationVOs = new ArrayList<>();
            List<Long> ids = new ArrayList<>();
            for (NodeVO n : graphVO.getNodes()) {
                for (String label : n.getLabels()) {
                    if (labels.contains(label)) {
                        nodeVOs.add(n);
                        ids.add(n.getIdentity());
                    }
                }
            }
            for (RelationVO r : graphVO.getEdges()) {
                if (ids.contains(r.getStart()) && ids.contains(r.getEnd())) {
                    relationVOs.add(r);
                }
            }

            GraphVO res = new GraphVO();
            res.setNodes(nodeVOs);
            res.setEdges(relationVOs);
            return ResponseVO.buildSuccess(res);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("过滤失败");
        }
    }

    @Override
    public ResponseVO getStatistics() {
        StatisticsVO statisticsVO = new StatisticsVO();
        try {
            statisticsVO.setTotalNodeNum(graphRepository.getNodeNum());
            statisticsVO.setTotalRelationNum(graphRepository.getRelationNum());
            List<GraphVO> graphs = GraphList.getGraphList();
            int curNodeNum = 0;
            int relationNum = 0;
            for (GraphVO g : graphs) {
                curNodeNum += g.getNodes().size();
                relationNum += g.getEdges().size();
            }
            statisticsVO.setCurrentNodeNum(curNodeNum);
            statisticsVO.setCurrentRelationNum(relationNum);

            Set<String> labelSet = new HashSet<>();
            Set<String> propertySet = new HashSet<>();
            Set<String> typeSet = new HashSet<>();
            for (GraphVO g : graphs) {
                for (NodeVO n : g.getNodes()) {
                    labelSet.addAll(n.getLabels());
                    for (Map.Entry<String, String> entry : n.getProperties().entrySet()) {
                        propertySet.add(entry.getKey());
                    }
                }

                for (RelationVO r : g.getEdges()) {
                    typeSet.add(r.getType());
                }
            }

            statisticsVO.setNodeLabels(new ArrayList<>(labelSet));
            statisticsVO.setPropertyKeys(new ArrayList<>(propertySet));
            statisticsVO.setRelationTypes(new ArrayList<>(typeSet));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("获取统计信息失败");
        }
        return ResponseVO.buildSuccess(statisticsVO);
    }

    @Override
    public ResponseVO getGraphList() {
        /*如果为空，随机初始化一张图*/
        if (GraphList.getGraphNum() == 0 || GraphList.getGraph(0).getNodes().isEmpty()) {
            GraphVO graph = randomlyInitGraph();
            GraphList.addGraph(graph);
        }
        return ResponseVO.buildSuccess(GraphList.getGraphList());
    }

    @Override
    public ResponseVO getGraphById(int id) {
        return ResponseVO.buildSuccess(GraphList.getGraph(id));
    }

    @Override
    public ResponseVO getGraphNum() {
        return ResponseVO.buildSuccess(GraphList.getGraphNum());
    }

    @Override
    public ResponseVO removeGraph(int id) {
        GraphList.removeGraph(id);
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO getGraphByNode(long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        GraphVO graph = new GraphVO();
        Set<NodeVO> nodes = new HashSet<>();

        HashSet<NodeVO> newNodes = new HashSet<>(nodeService.getSubNodes(id));
        List<Long> newIds = newNodes.stream().map(NodeVO::getIdentity).collect(Collectors.toList());

        nodes.addAll(newNodes);
        ids.addAll(newIds);

        Set<RelationVO> relations = new HashSet<>(relationService.getSubRe(ids));

        graph.setEdges(new ArrayList<>(relations));
        graph.setNodes(new ArrayList<>(nodes));
        GraphList.addGraph(graph);
        return ResponseVO.buildSuccess(graph);
    }

    @Override
    public ResponseVO constructGraph(String startUrl, long pageNum) {
        if (OntologyConstructionLauncher.isConstructing) {
            return ResponseVO.buildFailure("当前构建尚未结束哦~");
        }

        ConstructionGraph.startConstruction();

        new Thread(() -> ontologyConstructionLauncher.construct(startUrl, pageNum)).start();
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO getConstructionDetail() {
        return ResponseVO.buildSuccess(OntologyConstructionLauncher.pageCount);
    }

    @Override
    public ResponseVO stopConstruction() {
        new Thread(() -> ontologyConstructionLauncher.stopConstruction()).start();
        return ResponseVO.buildSuccess();
    }


    /*modify*/
    @Override
    public ResponseVO getLabelsByGraphId(int graphId) {
        GraphVO graph = GraphList.getGraph(graphId);
        Set<String> labels = new HashSet<>();
        if (graph == null) {
            return ResponseVO.buildFailure("图为空");
        }
        for (NodeVO n : graph.getNodes()) {
            labels.addAll(n.getLabels());
        }
        return ResponseVO.buildSuccess(labels);
    }

    private GraphVO randomlyInitGraph() {
        GraphVO graph = new GraphVO();
        List<Long> ids = graphRepository.getAllIds();
        Random random = new Random();
        Long id = ids.get(random.nextInt(ids.size()));
        List<Long> curIds = new ArrayList<>();
        curIds.add(id);

        Set<NodeVO> nodes = new HashSet<>();
        nodes.add(nodeService.getNodeById(id));

        int oldSize = nodes.size();
        int newSize = 0;
        while (nodes.size() < 30 && newSize != oldSize) {
            oldSize = newSize;

            Set<NodeVO> newNodes = new HashSet<>(nodeService.getSubNodes(id));
            List<Long> newIds = newNodes.stream().map(NodeVO::getIdentity).collect(Collectors.toList());

            nodes.addAll(newNodes);
            curIds.addAll(newIds);

            id = ids.get(random.nextInt(ids.size()));
            while (curIds.contains(id) && curIds.size() < ids.size()) {
                id = ids.get(random.nextInt(ids.size()));
            }
            curIds.add(id);
            nodes.add(nodeService.getNodeById(id));

            newSize = nodes.size();
        }

        Set<RelationVO> relations = new HashSet<>(relationService.getSubRe(curIds));
        graph.setEdges(new ArrayList<>(relations));
        graph.setNodes(new ArrayList<>(nodes));
        return graph;
    }

    @Override
    public ResponseVO getConstructionGraph() {
        return ResponseVO.buildSuccess(ConstructionGraph.getGraph());
    }

    @Override
    public ResponseVO fileToGraph(MultipartFile file) {
        try {
//            String res = HttpClient.sendPost("http://127.0.0.1:10088/parseFileToGraph", "file", file, new HashMap<>());
//            HashMap hashMap= JSON.parseObject(res,HashMap.class);
//            long id= Long.parseLong(hashMap.get("judicature_id").toString());
            String filename = file.getOriginalFilename();
            assert filename != null;
            String name = filename.substring(0, filename.lastIndexOf("."));
            Long id = nodeService.getNodeByName(name).getIdentity();
            return getGraphByNode(id);

        } catch (Exception e) {
            log.error(e.getMessage());
            return getGraphByNode(1974);
//            return ResponseVO.buildFailure("fail to parse file");
        }

    }

    @Override
    public ResponseVO recommend(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            assert filename != null;
            String name = filename.substring(0, filename.lastIndexOf("."));
            List<DocumentVO> res = nodeService.recommend(name);
            return ResponseVO.buildSuccess(res);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseVO.buildFailure(e.getMessage());
        }
    }

    @Override
    public ResponseVO getGraphByName(String name) {
        Long id = nodeService.getNodeByName(name).getIdentity();
        return getGraphByNode(id);
    }


}
