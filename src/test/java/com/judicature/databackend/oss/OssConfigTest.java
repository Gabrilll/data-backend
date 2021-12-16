package com.judicature.databackend.oss;

import com.judicature.databackend.config.OSSConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OssConfigTest {
    @Test
    public void testOssConfig() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OSSConfig.setObjectName("testName");
        OSSConfig.upload(null);
        String path=System.getenv("NEO4J_HOME");
        path+="/import/test.graphml";
//        OSSConfig.upload(path);
        //这个path必须要是绝对路径，我不好测试
//        Method InputStream2ByteArray = ossConfig.getClass().getDeclaredMethod("InputStream2ByteArray",String.class);
//        InputStream2ByteArray.setAccessible(true);
//        InputStream2ByteArray.invoke(ossConfig,"test.txt");

        //这个老奇怪了，我的识别不到这个toByteArray()方法，但是上面的InputStream2ByteArray()可以，你可以在你那儿试试
//        Method toByteArray = ossConfig.getClass().getDeclaredMethod("toByteArray",String.class);
//        toByteArray.setAccessible(true);
//        toByteArray.invoke(ossConfig,"test");
    }
}
