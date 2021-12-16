package com.judicature.databackend.data;


import com.judicature.databackend.po.Node;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Rollback
@Transactional(transactionManager = "transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GraphRepositoryTest {

    @Autowired
    GraphRepository graphRepository;


    @Test
    public void testGetAllIds() {
        List<Long> ids = graphRepository.getAllIds();
        log.info(String.valueOf(ids.size()));
    }

    @Test
    public void testExportXml() {
        boolean res = graphRepository.exportXml();
        log.info(String.valueOf(res));
    }

    @Test
    public void testFilterByNodeLabels() {
        List<String> labels = Arrays.asList("裁判文书", "Person");
        List<Node> nodes = graphRepository.filterByNodeLabels(labels);
        log.info(String.valueOf(nodes.size()));
    }

    @Test
    public void testGetNodeNum() {
        Long num = graphRepository.getNodeNum();
        log.info(String.valueOf(num));
    }

    @Test
    public void testGetRelationNum() {
        Long num = graphRepository.getRelationNum();
        log.info(String.valueOf(num));
    }

}
