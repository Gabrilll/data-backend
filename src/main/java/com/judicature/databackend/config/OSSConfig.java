package com.judicature.databackend.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.FileInputStream;
import java.io.InputStream;

public class OSSConfig {
    private static final String accessKeySecret = "KrNGP0D12K20YoLtdOHlgZJ2RpY6Ok";
    private static final String accessKeyId = "LTAI5t8No3YMoQ8DYysmFVdj";
    private static final String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
    private static final String bucketName="sec123";
    private static String objectName;


    public static void setObjectName(String name){
        objectName=name;
    }

    public static int upload(String path) {

        if(objectName==null || path==null ){return -1;}
        System.out.println(path);
        try {
            OSS ossClient=new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
            InputStream inputStream=new FileInputStream(path);
            ossClient.putObject(bucketName,objectName,inputStream);
            ossClient.shutdown();
        }catch (Exception e){
            objectName=null;
            return -1;
        }

        objectName=null;
        return 1;
    }

//    private static byte[] InputStream2ByteArray(String filePath) throws IOException {
//
//        InputStream in = new FileInputStream(filePath);
//        byte[] data = toByteArray(in);
//        in.close();
//
//        return data;
//    }
//
//
//    private static byte[] toByteArray(InputStream in) throws IOException {
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024 * 4];
//        int n = 0;
//        while ((n = in.read(buffer)) != -1) {
//            out.write(buffer, 0, n);
//        }
//        return out.toByteArray();
//    }

//    public static void main(String[] args){
//        OSSConfig.setObjectName("export.xml");
//        String path=System.getenv("NEO4J_HOME");
//        path+="/import/test.graphml";
//        int res=OSSConfig.upload(path);
//        System.out.println(res);
//    }

}
