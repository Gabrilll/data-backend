package com.judicature.databackend.bl;

import com.judicature.databackend.vo.QAndR;
import com.judicature.databackend.vo.ResponseVO;

import java.util.List;

public interface QuestionService {
    /**
     * 智能问答
     * @return
     */
    QAndR answer(String question) throws Exception;

    /**
     * 个性化推荐
     */
    List<String> recommendQue(String question) throws Exception;

    /**
     * 语义搜索
     * @param question question
     * @return answer
     */
    ResponseVO semanticSearch(String question);
}
