package com.judicature.databackend.bl.semantic;

import com.judicature.databackend.model.WordSegmentResult;
import org.springframework.stereotype.Service;

/**
 * @author Gabri
 */
@Service
public interface WordSegmentationService {

    /**
     * 分词处理以及命名实体识别
     *
     * @param question
     * @return
     */
    public WordSegmentResult wordSegmentation(String question);

    public void reload();

}
