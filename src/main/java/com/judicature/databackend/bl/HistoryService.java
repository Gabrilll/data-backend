package com.judicature.databackend.bl;

import com.judicature.databackend.vo.HistoryVO;
import com.judicature.databackend.vo.ResponseVO;

public interface HistoryService {
    /**
     * 添加历史记录
     *
     * @return
     */
    boolean addHistory(HistoryVO historyVO);

    /**
     * 获取历史记录
     *
     * @return
     */

    ResponseVO getHistory();
}
