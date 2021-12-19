package com.judicature.databackend.data;

import com.judicature.databackend.po.Node;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NodeRepository extends Neo4jRepository<Node, Long>, NodeOperations {
    @Query("MATCH (n) WHERE id(n) = $identity DETACH DELETE n")
    void deleteNode(@Param("identity") Long identity);

}
