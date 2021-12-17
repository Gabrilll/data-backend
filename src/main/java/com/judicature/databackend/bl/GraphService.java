package com.judicature.databackend.bl;

import com.judicature.databackend.vo.ResponseVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GraphService {
    /**
     * 获取图
     * @return response
     */
    ResponseVO getGraph();

    /**
     * 导出xml
     * @return response
     */

    ResponseVO exportXml();


    /**
     * 根据节点类型对节点进行过滤
     * @param labels labels
     * @return response
     */
    ResponseVO filterByNodeLabels(List<String> labels,int graphId);

    /**
     * 获取图谱统计信息
     * @return response
     */
    ResponseVO getStatistics();

    /**
     * 获取所有图
     * @return graphList
     */
    ResponseVO getGraphList();


    /**
     * 根据id获取图
     * @param id graph_id
     * @return graph
     */
    ResponseVO getGraphById(int id);

    /**
     * 获取当前图的数量
     * @return graphNum
     */
    ResponseVO getGraphNum();

    /**
     * 删除一张图
     * @param id
     * @return
     */
    ResponseVO removeGraph(int id);

    /**
     * 根据节点获取图
     * @param id
     * @return
     */
    ResponseVO getGraphByNode(long id);

    /**
     * 自动构建知识图谱
     * @param startUrl 起始页面url
     * @param pageNum 爬取页面数量
     * @return 当前图的id
     */
    ResponseVO constructGraph(String startUrl,long pageNum);

    /**
     * 获取当前构建信息
     * @return 当前爬取的页面数量
     */
    ResponseVO getConstructionDetail();

    /**
     * 停止自动构建知识图谱
     * @return
     */
    ResponseVO stopConstruction();


    /**
     * 通过图的id获取标签
     * @return
     */
    ResponseVO getLabelsByGraphId(int graphId);

    /**
     * 获取当前正在构建的图
     * @return
     */
    ResponseVO getConstructionGraph();
    ResponseVO fileToGraph(MultipartFile file);
}
