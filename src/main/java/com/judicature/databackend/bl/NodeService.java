package com.judicature.databackend.bl;

import com.judicature.databackend.vo.NodeVO;
import com.judicature.databackend.vo.ResponseVO;

import java.util.List;

/**
 * @author Gabri
 */
public interface NodeService {
    /**
     * 添加实体
     * @param nodeVO
     * @return
     */
    ResponseVO addNode(NodeVO nodeVO,int graphId);

    /**
     * 删除实体
     * @param identity
     * @return
     */
    ResponseVO deleteNode(Long identity,int graphId);

    /**
     * 更新实体信息
     * @param nodeVO
     * @return
     */
    ResponseVO updateNode(NodeVO nodeVO,int graphId);


    /**
     * 获取所有节点
     * @param
     * @return
     */
    List<NodeVO> getAllNodes();

    /**
     * 获取与指定节点相关的节点
     * @param identity
     * @return
     */
    List<NodeVO> getSubNodes(Long identity);

    /**
     * @param id
     * @return
     */
    NodeVO getNodeById(long id);

    /**
     * 根据节点名获取节点
     */
    NodeVO getNodeByName(String name);

    /**
     * 获取层级目录
     */
    ResponseVO getNodesList();

    /**
     * 根据标签获取节点列表
     */
    List<NodeVO> getNodesByLabel(String label);



    /**
     * 获取全局搜索的节点列表
     */
    ResponseVO getSearchNodes();
}
