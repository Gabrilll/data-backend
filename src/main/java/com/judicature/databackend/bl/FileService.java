package com.judicature.databackend.bl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {

    /**
     * 存储上传文件
     */
//    ResponseVO storageFile(MultipartFile file, HttpServletRequest httpServletRequest) throws IOException;

    int json2Dao(MultipartFile mfile);
}
