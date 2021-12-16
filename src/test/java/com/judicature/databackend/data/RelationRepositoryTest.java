package com.judicature.databackend.data;

import com.judicature.databackend.po.Node;
import com.judicature.databackend.po.Relation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Rollback
@Transactional(transactionManager = "transactionManager")

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RelationRepositoryTest {
    @Autowired
    RelationRepository relationRepository;
    @Autowired
    NodeRepository nodeRepository;

    @Test
    public void testUpdateRelation() {
        long id1 = nodeRepository.addNode(new Node());
        long id2 = nodeRepository.addNode(new Node());

        Relation relation = new Relation();
        relation.setStart(id1);
        relation.setEnd(id2);
        relation.setType("testType");

        long relId = relationRepository.addRelation(relation);

        relation.setIdentity(relId);
        relation.setType("updateTest");

        Relation newRel = relationRepository.updateRelation(relation);

        Assert.assertEquals("updateTest", newRel.getType());
    }

    @Test
    public void testAddRelation() {

        long start = nodeRepository.addNode(new Node());
        long end = nodeRepository.addNode(new Node());
        Relation relation = new Relation();
        relation.setStart(start);
        relation.setEnd(end);
//        relation.setType("国籍");
        Object o = relationRepository.addRelation(relation);
        long id;
        if (o != null) {
            id = (long) o;
        } else {
            id = -1;
        }
        System.out.println(id);
    }

    @Test
    public void testGetRelationByTriplet() {
        long id = relationRepository.getRelationByTriplet(1331L, 1326L, "对");
        System.out.println(id);
    }

    @Test
    public void testDeleteRelation() {
        relationRepository.deleteRelation(175073L);
        log.info("success");
    }

    @Test
    public void getRelationById() {
        Relation relation = relationRepository.getRelationById(2230L);
        Assert.assertEquals(175072L, (long) relation.getIdentity());
    }

    @Test
    public void getAllRes() {
        List<Relation> relationList = relationRepository.getAllRe();
        log.info(String.valueOf(relationList.size()));
    }

    @Test
    public void getSubRe() {
        List<Relation> relations = relationRepository.getSubRe(List.of(898L, 894L));
        log.info(String.valueOf(relations.size()));
    }
}
