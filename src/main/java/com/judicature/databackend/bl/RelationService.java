package com.judicature.databackend.bl;

import com.judicature.databackend.vo.RelationVO;
import com.judicature.databackend.vo.ResponseVO;

import java.util.List;

public interface RelationService {
    /**
     * 添加关系
     * @param relationVO
     * @return
     */
    ResponseVO addRelation(RelationVO relationVO,int graphId);

    /**
     * 删除关系
     * @param id
     * @return
     */
    ResponseVO deleteRelation(Long id,int graphId);

    /**
     * 修改关系
     * @param relationVO
     * @return
     */
    ResponseVO updateRelation(RelationVO relationVO,int graphId);

    /**
     * 获取所有关系
     * @param
     * @return
     */
    List<RelationVO> getAllRe();

    /**
     * 获取与指定节点相关的关系
     * @param identity
     * @return
     */
    List<RelationVO> getSubRe(List<Long> identity);
}
