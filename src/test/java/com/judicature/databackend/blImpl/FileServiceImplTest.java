package com.judicature.databackend.blImpl;

import com.judicature.databackend.bl.FileService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 已测试：json2Dao方法，覆盖率全为100%
 */
//配置回滚，不写入数据库
@Rollback
@Transactional(transactionManager="transactionManager")

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceImplTest {
    @Autowired
    FileService fileService;

    /**
     * 测试读取文件并解析后，存入DB——文件非空，且解析成功
     */
    @Test
    public void json2DaoTest1() throws IOException {
        File file = new File("src/main/resources/testFile.json");
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = new CommonsMultipartFile(item);
        int res = fileService.json2Dao(multipartFile);
        Assert.assertEquals(1,res);
    }

    /**
     * 测试读取文件并解析后，存入DB——文件空，解析失败
     */
    @Test
    public void testJson2Dao2(){
        File file = new File("src/main/resources/testFile2.json");
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = new CommonsMultipartFile(item);
        int res = fileService.json2Dao(multipartFile);
        Assert.assertEquals(-1,res);
    }
}
