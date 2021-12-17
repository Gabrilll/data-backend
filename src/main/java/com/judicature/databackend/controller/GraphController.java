package com.judicature.databackend.controller;

import com.judicature.databackend.bl.FileService;
import com.judicature.databackend.bl.GraphService;
import com.judicature.databackend.vo.FilterLabelsVO;
import com.judicature.databackend.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/api/graph")
public class GraphController {
    private Logger logger = LoggerFactory.getLogger(GraphController.class);

    @Autowired
    GraphService graphService;

    @Autowired
    FileService fileService;

    /*接口保留*/
    @GetMapping("/getGraph")
    public ResponseVO getGraph() {
        return graphService.getGraph();
    }

    @GetMapping("/exportXml")
    public ResponseVO exportXml() {
        return graphService.exportXml();
    }


    @PostMapping("/filterByNodeLabels")
    public ResponseVO filterByNodeLabels(@RequestBody FilterLabelsVO labels,@RequestParam int graphId) {
        return graphService.filterByNodeLabels(labels.getLabels(),graphId);
    }

    @PostMapping("/uploadFile")
    public int singleFileUpload(MultipartFile mFile) {
        return fileService.json2Dao(mFile);

    }

    @GetMapping("/getStatistics")
    public ResponseVO getStatistics() {
        return graphService.getStatistics();
    }

    @GetMapping("/getGraphList")
    public ResponseVO getGraphList() {
        return graphService.getGraphList();
    }

    @GetMapping("/getGraphById")
    public ResponseVO getGraphById(@RequestParam int id) {
        return graphService.getGraphById(id);
    }

    @GetMapping("/getGraphNum")
    public ResponseVO getGraphNum() {
        return graphService.getGraphNum();
    }

    @GetMapping("/removeGraph")
    public ResponseVO removeGraph(@RequestParam int id) {
        return graphService.removeGraph(id);
    }

    @GetMapping("/getGraphByNode")
    public ResponseVO getGraphByNode(@RequestParam long id) {
        return graphService.getGraphByNode(id);
    }

    @GetMapping("/constructGraph")
    public ResponseVO constructGraph(@RequestParam String startUrl, @RequestParam long pageNum) {
        return graphService.constructGraph(startUrl,pageNum);
    }

    @GetMapping("/getConstructionDetail")
    public ResponseVO getConstructionDetail() {
        return graphService.getConstructionDetail();
    }

    @GetMapping("/stopConstruction")
    public ResponseVO stopConstruction(){
        return graphService.stopConstruction();
    }

    @GetMapping("/getLabelsByGraphId")
    public ResponseVO getLabelsByGraphId(@RequestParam int graphId){
        return graphService.getLabelsByGraphId(graphId);
    }

    @GetMapping("/getConstructionGraph")
    public ResponseVO getConstructionGraph(){
        return graphService.getConstructionGraph();
    }

    @PostMapping("/fileToGraph")
    public ResponseVO fileToGraph(@RequestParam("file") MultipartFile file) {
        return graphService.fileToGraph(file);
    }
}
