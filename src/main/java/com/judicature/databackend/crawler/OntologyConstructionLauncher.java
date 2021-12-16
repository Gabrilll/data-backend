package com.judicature.databackend.crawler;

import com.judicature.databackend.bl.semantic.WordSegmentationService;
import com.judicature.databackend.config.SemanticConfig;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import java.io.File;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * 爬虫
 * @author Gabri
 */
@Service
@Component
public class OntologyConstructionLauncher {

    /**
     * 已爬取页面的储量
     */
    public static Long pageCount = 0L;

    static SemanticConfig semanticConfig=SemanticConfig.getInstance();

    @Autowired
    WordSegmentationService wordSegmentationService;

    /**
     * hanlp自定义用户词典存储路径
     */
    /*服务器*/
//    public static final String DICT_PATH ="/home/ubuntu/sec/data/dictionary/custom/dict.txt";
    /*本地*/
    public static final String DICT_PATH="src/main/resources/data/dictionary/custom/dict.txt";

    public static boolean isConstructing=false;

    private Spider spider;

    /**
     * 手动爬虫入口
     */
    public static void main(String[] args) {
//        System.out.println(System.getProperty("java.class.path"));

        OntologyConstructionPageProcessor constructionPageProcessor = new OntologyConstructionPageProcessor();
        OntologyConstructionPipeline constructionPipeline = new OntologyConstructionPipeline();
        semanticConfig.setPageNum(100L);
        Spider.create(constructionPageProcessor)
                //start from 新冠肺炎
                .addUrl("https://baike.baidu.com/item/%E6%96%B0%E5%9E%8B%E5%86%A0%E7%8A%B6%E7%97%85%E6%AF%92%E8%82%BA%E7%82%8E/24282529.htm")
//                .addUrl("https://baike.baidu.com/item/%E7%A9%BA%E9%97%B4%E7%AB%99/415680?fr=aladdin")
                .addPipeline(constructionPipeline)
                //开启 5 个线程抓取
                /*TODO 多个线程需要加锁，否则会出错*/
                .thread(1)
                //启动爬虫
                .run();

//        System.out.println(FuzzySearch.ratio("新冠","新冠肺炎"));
//        System.out.println(FuzzySearch.ratio("新冠","冠军"));

    }


    /**
     *
     * @param url
     * @param pageNum
     * @return
     */
    public boolean construct(String url,long pageNum){
        isConstructing=true;
        OntologyConstructionPageProcessor constructionPageProcessor=new OntologyConstructionPageProcessor();
        OntologyConstructionPipeline constructionPipeline=new OntologyConstructionPipeline();
        semanticConfig.setPageNum(pageNum);

        try {
            spider=Spider.create(constructionPageProcessor);
            spider.addUrl(url);
            spider.addPipeline(constructionPipeline);
//            spider.setExitWhenComplete(false);
            spider.thread(1);
            spider.start();
            System.out.println("start constructing!");
            long cnt=spider.getPageCount();
            long startTime =  System.currentTimeMillis();
            long endTime =  System.currentTimeMillis();
            long usedTime = (endTime-startTime)/1000;
            while(usedTime<8){
                endTime=System.currentTimeMillis();
                usedTime=(endTime-startTime)/1000;
            }
            startTime=endTime;
            usedTime= 0;
            while (spider!=null && OntologyConstructionLauncher.pageCount<pageNum &&OntologyConstructionLauncher.pageCount<20000 ){
//                System.out.println(cnt);
//                System.out.println(spider.getPageCount());
                cnt=spider.getPageCount();
                while(usedTime<10){
                    endTime=System.currentTimeMillis();
                    usedTime=(endTime-startTime)/1000;
//                    System.out.println(cnt);
//                    System.out.println(spider.getPageCount());
                }
                startTime=endTime;
                usedTime=0;
            }
//            System.out.println(cnt);
//            System.out.println(spider.getPageCount());
            if(spider!=null){
                System.out.println("stop constructing! 1");
                spider.clearPipeline();
                spider.stop();
                spider.clearPipeline();
            }
//            Spider.create(constructionPageProcessor)
//                    //start from 新冠肺炎
//                    //                .addUrl("https://baike.baidu.com/item/%E6%96%B0%E5%9E%8B%E5%86%A0%E7%8A%B6%E7%97%85%E6%AF%92%E8%82%BA%E7%82%8E/24282529.htm")
//                    .addUrl(url)
//                    .addPipeline(constructionPipeline)
//                    //开启 5 个线程抓取
//                    /*TODO 多个线程需要加锁，否则会出错*/
//                    .thread(1)
//                    //启动爬虫
//                    .run();
            }catch (Exception e){
                e.printStackTrace();
                isConstructing=false;
                pageCount=0L;
                return false;
            }

        if(spider==null){
            return true;
        }

        spider=null;

        System.out.println("reload!");
        /*热加载自定义词典*/

        File toFile=new File(DICT_PATH);
        File fromFile=new File(semanticConfig.getDictPath());
        try{
            Files.copy(fromFile.toPath(),toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            e.printStackTrace();
            isConstructing=false;
            pageCount=0L;
            return false;
        }

        try {
            deleteBin();
            CustomDictionary.reload();
        }catch (Exception e){
            e.printStackTrace();
            isConstructing=false;
            pageCount=0L;
            return false;
        }


        /*重新加载构建后的实体文件*/
        toFile=new File(semanticConfig.getQuery_ontologyPath());
        fromFile=new File(semanticConfig.getConstruct_ontologyPath());
        try {
            FileLock fileLock=SemanticConfig.getfilelock(fromFile);
            Files.copy(fromFile.toPath(),toFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
            if(fileLock!=null){
                fileLock.release();
            }
        }catch (Exception e){
            e.printStackTrace();
            isConstructing=false;
            pageCount=0L;
            return false;
        }


        /*重新加载构建后的实体词典*/
        toFile=new File(semanticConfig.getQuery_individualDictPath());
        fromFile=new File(semanticConfig.getConstruct_individualDictPath());
        try {
            Files.copy(fromFile.toPath(),toFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            e.printStackTrace();
            isConstructing=false;
            pageCount=0L;
            return false;
        }

        /*重新加载同义词词典*/
        toFile=new File(semanticConfig.getQuery_aliasPath());
        fromFile=new File(semanticConfig.getConstruct_aliasPath());
        try {
            Files.copy(fromFile.toPath(),toFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            e.printStackTrace();
            isConstructing=false;
            pageCount=0L;
            return false;
        }

        try {
            SemanticConfig.getInstance().reloadModel();
        }catch (Exception e){
            e.printStackTrace();
            isConstructing=false;
            pageCount=0L;
            return false;
        }



        isConstructing=false;
        pageCount=0L;
        return true;
    }

    public void stopConstruction(){
        if(isConstructing && spider!=null){
            System.out.println("stop constructing! 2");
            spider.clearPipeline();
            spider.stop();
            spider=null;

            long startTime =  System.currentTimeMillis();
            long endTime =  System.currentTimeMillis();
            long usedTime = (endTime-startTime)/1000;
            while(usedTime<5){
                endTime=System.currentTimeMillis();
                usedTime=(endTime-startTime)/1000;
            }

            System.out.println("reload!");
            /*热加载自定义词典*/
            File toFile=new File(DICT_PATH);
            File fromFile=new File(semanticConfig.getDictPath());
            try{
                Files.copy(fromFile.toPath(),toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                deleteBin();
                CustomDictionary.reload();
            }catch (Exception e){
                e.printStackTrace();
            }


            /*重新加载构建后的实体文件*/
            toFile=new File(semanticConfig.getQuery_ontologyPath());
            fromFile=new File(semanticConfig.getConstruct_ontologyPath());
            try {
                FileLock fileLock=SemanticConfig.getfilelock(fromFile);
                Files.copy(fromFile.toPath(),toFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
                if(fileLock!=null){
                    fileLock.release();
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            /*重新加载构建后的实体词典*/
            toFile=new File(semanticConfig.getQuery_individualDictPath());
            fromFile=new File(semanticConfig.getConstruct_individualDictPath());
            try {
                Files.copy(fromFile.toPath(),toFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception e){
                e.printStackTrace();
            }

            /*重新加载同义词词典*/
            toFile=new File(semanticConfig.getQuery_aliasPath());
            fromFile=new File(semanticConfig.getConstruct_aliasPath());
            try {
                Files.copy(fromFile.toPath(),toFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                SemanticConfig.getInstance().reloadModel();
            }catch (Exception e){
                e.printStackTrace();
                isConstructing=false;
                pageCount=0L;
            }

            wordSegmentationService.reload();

            isConstructing=false;
            pageCount=0L;
        }
        pageCount=0L;
    }

    public void deleteBin() {
        // 里面输入特定目录
        File file = new File(DICT_PATH);
        file=new File(file.getParent());
        File temp = null;
        File[] filelist = file.listFiles();

        if(filelist==null){
            return;
        }

        for (File value : filelist) {
            temp = value;
            // 获得文件名，如果后缀为“”，这个你自己写，就删除文件
            if (temp.getName().endsWith(".bin")) {
                // 删除文件
                temp.delete();
            }
        }
    }

}
