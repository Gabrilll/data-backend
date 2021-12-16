package com.judicature.databackend.controller;

import com.judicature.databackend.bl.QuestionService;
import com.judicature.databackend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhy, Gabri
 */
@RestController()
@RequestMapping("/api/question")
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @GetMapping("/answerQuestion")
    public ResponseVO getAnswer(@RequestParam String question) throws Exception { return ResponseVO.buildSuccess(questionService.answer(question).getAnswer()); }

    @GetMapping("recommendQue")
    public ResponseVO getRecommend(@RequestParam String question) throws Exception{ return ResponseVO.buildSuccess(questionService.answer(question).getRecommend());}

    @GetMapping("/semanticSearch")
    public ResponseVO semanticSearch(@RequestParam String question){
        return questionService.semanticSearch(question);
    }
}
