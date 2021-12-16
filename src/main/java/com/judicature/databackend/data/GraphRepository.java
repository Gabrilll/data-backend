package com.judicature.databackend.data;

import com.judicature.databackend.po.Node;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Gabri
 */
@Repository
public interface GraphRepository extends Neo4jRepository<Node,Long>,GraphOperations{
    /**
     * 导出xml
     * @return boolean
     */
    @Query("MATCH (x)-[r]->(y)\n" +
            "        WITH collect(DISTINCT x) as xs,collect(DISTINCT y) as ys, collect(r) as rs LIMIT 300\n" +
            "        CALL apoc.export.graphml.data(xs+ys,rs,\"test.graphml\", {}) YIELD done RETURN done")
    boolean exportXml();

    /**
     * 获取节点数量
     * @return num
     */
    @Query("MATCH (n) RETURN count(*)")
    long getNodeNum();


    /**
     * 获取关系数量
     * @return num
     */
    @Query("MATCH (n)-[r]->(m) RETURN count(*)")
    long getRelationNum();

    @Query("MATCH (n) return id(n)")
    List<Long> getAllIds();

}
