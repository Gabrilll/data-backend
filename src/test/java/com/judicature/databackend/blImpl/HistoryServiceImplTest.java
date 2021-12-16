package com.judicature.databackend.blImpl;

import com.judicature.databackend.bl.HistoryService;
import com.judicature.databackend.enums.OperationType;
import com.judicature.databackend.vo.HistoryVO;
import com.judicature.databackend.vo.ResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 覆盖率：72%
 */
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class HistoryServiceImplTest {
    @Autowired
    HistoryService historyService;

    @Test
    public void testAddHistory() {
        Map<String, String> properties = new HashMap<>();
        properties.put("p1", "guess");
        properties.put("p2", "what");
        HistoryVO historyVO = new HistoryVO(OperationType.AddNode, (long) 123, properties);
        historyService.addHistory(historyVO);
    }

    @Test
    public void testAddHistory2(){
        HistoryVO historyVO = null;
        historyService.addHistory(historyVO);
    }


    @Test
    public void testGetHistory() {
        ResponseVO responseVO = historyService.getHistory();
        Object content = responseVO.getContent();
        List<HistoryVO> historyVOS = new ArrayList<>();
        if (content instanceof ArrayList<?>) {
            for (Object o : (List<?>) content) {
                historyVOS.add((HistoryVO) o);
            }
        }

        System.out.println(historyVOS.size());
    }

}
