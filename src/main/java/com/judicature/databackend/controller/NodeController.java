package com.judicature.databackend.controller;

import com.judicature.databackend.bl.NodeService;
import com.judicature.databackend.vo.NodeVO;
import com.judicature.databackend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/node")
public class NodeController {
    @Autowired
    NodeService nodeService;

    @PostMapping("/addNode")
    public ResponseVO addNode(@RequestBody NodeVO nodeVO,@RequestParam int graphId){
        return nodeService.addNode(nodeVO,graphId);
    }

    @GetMapping("/deleteNode")
    public ResponseVO deleteNode(@RequestParam Long identity,@RequestParam int graphId){
        return nodeService.deleteNode(identity,graphId);
    }

    @PostMapping("/updateNode")
    public ResponseVO updateNode(@RequestBody NodeVO nodeVO,@RequestParam int graphId){
        return nodeService.updateNode(nodeVO,graphId);
    }

    @GetMapping("/getNodesList")
    public ResponseVO getNodesList(){return nodeService.getNodesList();}

    @GetMapping("getSearchNodes")
    public ResponseVO getSearchNodes(){return nodeService.getSearchNodes();}
//    //test
//    @GetMapping("/getAllNodes")
//    public List<NodeVO> getAllNodes(){return nodeService.getAllNodes();}
}
