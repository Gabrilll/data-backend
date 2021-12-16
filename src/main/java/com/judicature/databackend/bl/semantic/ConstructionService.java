package com.judicature.databackend.bl.semantic;

import com.judicature.databackend.model.BaikePage;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface ConstructionService {

    /**
     * @param baikePage
     * @return
     */
    public boolean construction(BaikePage baikePage) throws Exception;
}
