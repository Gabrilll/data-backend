package com.judicature.databackend.data;

import com.judicature.databackend.po.Relation;

import java.util.List;

public interface RelationOperations {
    Long addRelation(Relation relation);
    Relation updateRelation(Relation relation);
    Relation getRelationById(long identity);
    List<Relation> getAllRe();
    List<Relation> getSubRe(List<Long> identity);
    long getRelationByTriplet(Long startId,long endId,String type);
}
