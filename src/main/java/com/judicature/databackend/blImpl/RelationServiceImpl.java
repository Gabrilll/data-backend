package com.judicature.databackend.blImpl;

import com.judicature.databackend.bl.HistoryService;
import com.judicature.databackend.bl.RelationService;
import com.judicature.databackend.data.RelationRepository;
import com.judicature.databackend.enums.OperationType;
import com.judicature.databackend.po.Relation;
import com.judicature.databackend.util.VO2PO;
import com.judicature.databackend.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lhy, Gabri
 */
@Slf4j
@Service
public class RelationServiceImpl implements RelationService {
    @Autowired
    RelationRepository relationRepository;
    @Autowired
    HistoryService historyService;

    /**
     * 添加关系
     *
     * @param relationVO relation
     * @return response
     */
    @Override
    public ResponseVO addRelation(RelationVO relationVO, int graphId) {
        Relation relation = VO2PO.toRelation(relationVO);
        try {
            long id = relationRepository.addRelation(relation);
            HistoryVO history = new HistoryVO(OperationType.AddRelation, id);
            historyService.addHistory(history);
            relationVO.setIdentity(id);
            GraphList.getGraph(graphId).addRelation(relationVO);
            return ResponseVO.buildSuccess(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("关系添加失败");
        }
    }

    /**
     * 删除关系
     *
     * @param id id
     * @return response
     */
    @Override
    public ResponseVO deleteRelation(Long id, int graphId) {
        HistoryVO historyVO = new HistoryVO(OperationType.DeleteRelation, id);
        try {
            relationRepository.deleteRelation(id);
        } catch (Exception e) {
            log.error("关系删除失败,id: " + id);
            return ResponseVO.buildFailure("关系删除失败");
        }

        historyService.addHistory(historyVO);

        GraphList.getGraph(graphId).deleteRelation(id);
        return ResponseVO.buildSuccess(true);

    }

    /**
     * 更新关系
     *
     * @param relationVO relation
     * @return response
     */
    @Override
    public ResponseVO updateRelation(RelationVO relationVO, int graphId) {
        Relation relation = VO2PO.toRelation(relationVO);
        HistoryVO historyVO = new HistoryVO(OperationType.UpdateRelation, relationVO.getIdentity(), relationVO.getProperties());
        try {
            long oldId = relationVO.getIdentity();
            Relation re = relationRepository.updateRelation(relation);
            RelationVO newRel = VO2PO.toRelationVO(re);
            historyService.addHistory(historyVO);
            GraphList.getGraph(graphId).updateRelation(oldId, newRel);
            return ResponseVO.buildSuccess(newRel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("关系修改失败");
        }
    }

    /**
     * 获取所有关系
     *
     * @return relationVOs
     */
    @Override
    public List<RelationVO> getAllRe() {
        try {
            List<Relation> relations = relationRepository.getAllRe();
            return relations.stream().map(VO2PO::toRelationVO).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 获取特定节点的所有关系
     *
     * @param identity id
     * @return relationVOs
     */
    @Override
    public List<RelationVO> getSubRe(List<Long> identity) {
        try {
            List<Relation> relations = relationRepository.getSubRe(identity);
            return relations.stream().map(VO2PO::toRelationVO).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
