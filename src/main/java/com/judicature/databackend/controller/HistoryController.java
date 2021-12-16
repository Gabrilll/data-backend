package com.judicature.databackend.controller;

import com.judicature.databackend.bl.HistoryService;
import com.judicature.databackend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/history")
public class HistoryController {
    @Autowired
    HistoryService historyService;

    @GetMapping("getHistory")
    public ResponseVO getHistory(){
        return historyService.getHistory();
    }
}
