package com.judicature.databackend.controller;

import com.judicature.databackend.bl.RelationService;
import com.judicature.databackend.vo.RelationVO;
import com.judicature.databackend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lhy, Gabri
 */
@RestController()
@RequestMapping("/api/relation")
public class RelationController {
    RelationService relationService;

    @Autowired
    RelationController(RelationService relationService) {
        this.relationService = relationService;
    }

    @PostMapping("/addRelation")
    public ResponseVO addRelation(@RequestBody RelationVO relationVO,@RequestParam int graphId) {
        return relationService.addRelation(relationVO,graphId);
    }

    @GetMapping("/deleteRelation")
    public ResponseVO deleteRelation(@RequestParam Long identity,@RequestParam int graphId) {
        return relationService.deleteRelation(identity,graphId);
    }

    @PostMapping("/updateRelation")
    public ResponseVO updateRelation(@RequestBody RelationVO relationVO,@RequestParam int graphId) {
        return relationService.updateRelation(relationVO,graphId);
    }

    //    //test
    //    @GetMapping("/getAllRe")
    //    public List<RelationVO> getAllRe(){return relationService.getAllRe();}
}
