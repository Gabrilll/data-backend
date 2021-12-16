package com.judicature.databackend.mongodb;

import com.judicature.databackend.po.History;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Gabri
 */
public interface HistoryRepository extends MongoRepository<History,String> {
}
