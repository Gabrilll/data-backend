package com.judicature.databackend.blImpl;

import com.judicature.databackend.bl.HistoryService;
import com.judicature.databackend.mongodb.HistoryRepository;
import com.judicature.databackend.util.VO2PO;
import com.judicature.databackend.vo.HistoryVO;
import com.judicature.databackend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Gabri
 */
@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    HistoryRepository historyRepository;

    @Override
    public boolean addHistory(HistoryVO historyVO) {
        try {
            historyRepository.save(VO2PO.toHistory(historyVO));
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public ResponseVO getHistory() {
        try {
            List<HistoryVO> histories=VO2PO.toHistoryVOs(historyRepository.findAll(Sort.by(Sort.Direction.DESC,"date")));
            return ResponseVO.buildSuccess(histories.subList(0, Math.min(histories.size(), 200)));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure("获取历史操作失败");
        }



    }
}
