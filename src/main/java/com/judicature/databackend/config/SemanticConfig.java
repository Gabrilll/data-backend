package com.judicature.databackend.config;

import com.judicature.databackend.util.Alias;
import com.judicature.databackend.util.Configuration;
import com.judicature.databackend.util.Dict;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

/**
 * @author Gabri
 */
public class SemanticConfig {

    private static SemanticConfig semanticConfig;

    public final String PROPERTY_FILE = "ontology-construction.properties";

    public Properties properties;

    public String pizzaNs;

    public String rootPath;

    public String query_ontologyPath;

    public String query_individualDictPath;

    public String construct_ontologyPath;

    public String construct_individualDictPath;

    public String query_aliasPath;

    public String construct_aliasPath;

    public String picSavePath;

    public Long pageNum;

    public String dictPath;

    public OntModel model;

    public Model loadModel;

    public int graphId;

    public static SemanticConfig getInstance(){
        if(semanticConfig==null){
            semanticConfig=new SemanticConfig();
        }
        return semanticConfig;
    }

    private SemanticConfig() {
        try {
            setProperties(Configuration.propertiesLoader(getPropertyFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPizzaNs(getProperties().getProperty("pizzaNs"));

        setRootPath(getProperties().getProperty("rootPath"));
        setQuery_ontologyPath(toAbsolutePath(getRootPath() + getProperties().get("query_ontologyPath").toString()));
        setQuery_individualDictPath(toAbsolutePath(getRootPath() + getProperties().get("query_individualDictPath").toString()));
        setConstruct_ontologyPath(toAbsolutePath(getRootPath() + getProperties().get("construct_ontologyPath").toString()));
        setConstruct_individualDictPath(toAbsolutePath(getRootPath() + getProperties().get("construct_individualDictPath").toString()));
        setQuery_aliasPath(toAbsolutePath(getRootPath() + getProperties().get("query_aliasPath").toString()));
        setConstruct_aliasPath(toAbsolutePath(getRootPath() + getProperties().get("construct_aliasPath").toString()));
        setPicSavePath(toAbsolutePath(getRootPath() + getProperties().getProperty("picSavePath")));
        setDictPath(toAbsolutePath(getRootPath() + getProperties().getProperty("dictPath")));
        setPageNum(Long.parseLong(getProperties().getProperty("pageNum")));

        setModel(ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM));
        setLoadModel(FileManager.get().readModel(getModel(), getQuery_ontologyPath()));
    }


    /**
     * 配置类 不需要生成实例
     */


    public void setPageNum(Long num) {
        pageNum = num;
    }

    /**
     * 配置文件名
     */
    public String getPropertyFile() {
        return PROPERTY_FILE;
    }

    /**
     * 配置
     */
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * 本体标识
     */
    public String getPizzaNs() {
        return pizzaNs;
    }

    public void setPizzaNs(String pizzaNs) {
        this.pizzaNs = pizzaNs;
    }

    /**
     * 根路径
     */
    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * 用于查询的本体文件路径
     */
    public String getQuery_ontologyPath() {
        return query_ontologyPath;
    }

    public void setQuery_ontologyPath(String query_ontologyPath) {
        this.query_ontologyPath = query_ontologyPath;
    }

    /**
     * 用于查询的实体词典路径
     */
    public String getQuery_individualDictPath() {
        return query_individualDictPath;
    }

    public void setQuery_individualDictPath(String query_individualDictPath) {
        this.query_individualDictPath = query_individualDictPath;
    }

    /**
     * 用于构建的本体文件路径
     */
    public String getConstruct_ontologyPath() {
        return construct_ontologyPath;
    }

    public void setConstruct_ontologyPath(String construct_ontologyPath) {
        this.construct_ontologyPath = construct_ontologyPath;
    }

    /**
     * 用于构建的实体词典路径
     */
    public String getConstruct_individualDictPath() {
        return construct_individualDictPath;
    }

    public void setConstruct_individualDictPath(String construct_individualDictPath) {
        this.construct_individualDictPath = construct_individualDictPath;
    }

    /**
     * 用于查询别名和简称的地址
     */
    public String getQuery_aliasPath() {
        return query_aliasPath;
    }

    public void setQuery_aliasPath(String query_aliasPath) {
        this.query_aliasPath = query_aliasPath;
    }

    /**
     * 用于构建别名的地址
     */
    public String getConstruct_aliasPath() {
        return construct_aliasPath;
    }

    public void setConstruct_aliasPath(String construct_aliasPath) {
        this.construct_aliasPath = construct_aliasPath;
    }

    /**
     * 图片保存地址
     */
    public String getPicSavePath() {
        return picSavePath;
    }

    public void setPicSavePath(String picSavePath) {
        this.picSavePath = picSavePath;
    }

    /**
     * 要抓取的页面数量
     */
    public Long getPageNum() {
        return pageNum;
    }

    public String getDictPath() {
        return dictPath;
    }

    public void setDictPath(String dictPath) {
        this.dictPath = dictPath;
    }

    public OntModel getModel() {
        return model;
    }

    public void setModel(OntModel model) {
        this.model = model;
    }

    public Model getLoadModel() {
        return loadModel;
    }

    public void setLoadModel(Model loadModel) {
        this.loadModel = loadModel;
    }

    public int getGraphId() {
        return graphId;
    }

    public void setGraphId(int graphId) {
        this.graphId = graphId;
    }

    public String toAbsolutePath(String path){
        File file=new File(path);
        String absolutePath=file.getAbsolutePath();
        if(absolutePath.startsWith("src")){
            absolutePath="/var/lib/jenkins/workspace/Backend-TrillionCOIN/"+absolutePath;
        }else if(absolutePath.startsWith("/src")){
            absolutePath="/var/lib/jenkins/workspace/Backend-TrillionCOIN"+absolutePath;
        }
        return absolutePath;
    }

    public void reloadModel(){
        System.out.println("reload model");
        setModel(ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM));
        File file=new File(getQuery_aliasPath());
        FileLock fileLock=getfilelock(file);
        while (true){
            try {
                setLoadModel(FileManager.get().readModel(getModel(), getConstruct_ontologyPath()));
                break;
            }catch (Exception ignored){
                long startTime =  System.currentTimeMillis();
                long endTime =  System.currentTimeMillis();
                long usedTime = (endTime-startTime)/1000;
                while(usedTime<5){
                    endTime=System.currentTimeMillis();
                    usedTime=(endTime-startTime)/1000;
                }
                File toFile=new File(semanticConfig.getQuery_ontologyPath());
                File fromFile=new File(semanticConfig.getConstruct_ontologyPath());
                try {
                    Files.copy(fromFile.toPath(),toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        Dict.reload();
        Alias.reload();


        if(fileLock!=null){
            try {
                fileLock.release();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static FileLock getfilelock(File file)  {
//        FileLock fileLock;
//        try{
//            RandomAccessFile fi = new RandomAccessFile(file, "rws");
//
//            FileChannel fc = fi.getChannel();
//
//
//            while (true){
//                try {
//                    fileLock=fc.lock(0, Long.MAX_VALUE,true);
//                    break;
//                }catch (Exception ignored){
//
//                }
//            }
//            return fileLock;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return null;
    }

}
