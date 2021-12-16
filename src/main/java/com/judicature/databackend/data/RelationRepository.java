package com.judicature.databackend.data;

import com.judicature.databackend.po.Relation;
import com.judicature.databackend.vo.RelationVO;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationRepository extends Neo4jRepository<Relation,Long>,RelationOperations {
    @Query("MATCH ()-[r]->() WHERE id(r)=$identity DELETE r")
    void deleteRelation(@Param("identity") Long id);
//    @Query("return 1")
//    Relation updateRelation(Relation relation);
//    @Query("return 1")
//    Relation getRelationById(long identity);
//    @Query("return 1")
//    List<Relation> getAllRe();
//    @Query("return 1")
//    List<Relation> getSubRe(List<Long> identity);
//    @Query("MATCH (x)-[r]->(y) WHERE id(x)=$startId and id(y)=$endId and r.name=\"$type\"\n" +
//            "        RETURN id(r)")
//    long getRelationByTriplet(Long startId,long endId,String type);
}
