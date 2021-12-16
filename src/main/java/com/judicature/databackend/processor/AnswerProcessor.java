package com.judicature.databackend.processor;

import com.judicature.databackend.config.SemanticConfig;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.io.*;
import java.util.*;

/**
 * Spark贝叶斯分类器 + HanLP分词器 + 实现问题语句的抽象+模板匹配+关键性语句还原
 */
public class AnswerProcessor {

    /**分类标签号和问句模板对应表*/
    private Map<Integer, String> questionsPattern;

    /**关键字与其词性的map键值对集合 == 句子抽象*/
    private Map<String, List<String>> abstractMap;

    /** 分类模板索引*/
    int modelIndex = 0;

    /** 抽象词性数组 */
    private ArrayList<String> natureList;

    private ArrayList<ArrayList<String>> questionList = new ArrayList<>();

    public static double threshold = 0.001;

    public AnswerProcessor() throws Exception{
//        CustomDictionary.reload();
         //加载问题模板
        questionsPattern = loadQuestionTemplates();

        //加载问题总列表
        questionList = loadQuestionList();

        /**
         * 问题模板里涉及到的词性
         * nzd：发病机制；cure：治疗；med：药；def：定义；eat：饮食；taboo：禁忌；infect：传染性
         * sym：症状；tramed：中药；belong：科室；easyGet：易感人群；check：检查；pre：预防；peo：人群；place：地点；belong：科室
         * easyGet：易感人群；mask：口罩；appear：症状
         */

        natureList = new ArrayList<>(Arrays.asList("nhd","nzd","cure","med","def","pre","eat",
                "taboo","infect","sym","tramed","belong","easyGet","check","peo","place",
                "easyGet","mask","appear","hi"));

    }

    /**
     * 提取关键词 + 词性抽象 + 确定模板
     * @param querySentence 问句
     * @return 结果集合（问题模板索引、关键word数组）
     */
    public List<String> analysis(String querySentence) throws Exception {

        /**原始问句*/
        System.out.println("原始句子："+querySentence);

        /**抽象句子，利用HanPL提取关键字，并进行词性抽象*/
        ArrayList<String> abstractStr = queryAbstract(querySentence);
        System.out.println("句子抽象化结果："+abstractStr);

        /** 计算HanLP提取出来的关键词数组和questionList问题关键词模板数组的语义距离，最小的即为modelIndex*/

        int tmpIndex = 0;
        double maxSimilar = Double.MIN_VALUE;
        for(int i = 0;i < questionList.size();i++){
            double semanticDist = getSimilarity(questionList.get(i),abstractStr);
            if(semanticDist > maxSimilar){
                maxSimilar = semanticDist;
                tmpIndex = i;
            }
        }
        modelIndex = tmpIndex;
        System.err.println(modelIndex);
        List<String> resultList = new ArrayList<>();
        resultList.add(String.valueOf(modelIndex));

        String pattern = questionsPattern.get(modelIndex);
        String[] pList = pattern.split(" ");
        if (abstractMap != null && resultList != null && !abstractMap.isEmpty()&&abstractMap.get(pList[0])!=null){
            resultList.addAll(abstractMap.get(pList[0]));
        }
        if (resultList == null && abstractMap != null && !abstractMap.isEmpty() && abstractMap.get(pList[0]) != null){
            resultList.addAll(abstractMap.get(pList[0]));
        }

        /**
         * resultList包括：modelIndex，问题模板类型
         * 若是疫情防控措施，还有peo或place对应内容
         */
        for(int i = 1;i < pList.length;i++){
            resultList.add(pList[i]);
        }
        for (Map.Entry<String,List<String>> entry:abstractMap.entrySet()){
            if (entry.getKey().equals("peo") || entry.getKey().equals("place")){
                resultList.addAll(entry.getValue());
                break;
            }
        }
        return resultList;
    }

    /**
     * 将HanLp分词后的关键word，用抽象词性xx替换
     * @param querySentence 查询句子
     * @return
     */
    public ArrayList<String> queryAbstract(String querySentence) {

        // 句子抽象化
        Segment segment = new NShortSegment().enableCustomDictionary(true).enableCustomDictionaryForcing(true).enablePlaceRecognize(true).enableOrganizationRecognize(true);
//        Segment segment = HanLP.newSegment().enableCustomDictionary(true);
        List<Term> terms = segment.seg(querySentence);

        ArrayList<String> abstractQuery = new ArrayList<>();
        abstractMap = new HashMap<>();

        /** 暂时只写了正向查询*/
        for (Term term : terms) {
            //word：原词；tag：词性
            String word = term.word;
            String tag = term.nature.toString();

            if(natureList.contains(tag)){
                if (!abstractQuery.contains(tag)){
                    abstractQuery.add(tag);
                }
                List<String> temp = abstractMap.get(tag);
                if (temp == null){
                    List<String> words = new ArrayList<>();
                    words.add(word);
                    abstractMap.put(tag,words);
                }
                else {
                    temp.add(word);
                    abstractMap.put(tag,temp);
                }

            }
            else {
                abstractQuery.add(word);
            }
        }
        System.out.println("========HanLP关键词提取结束========");
        return abstractQuery;
    }

    /**
     * 计算关键词数组余弦相似度，又称为余弦相似性，是通过计算两个向量的夹角余弦值来评估他们的相似度,越接近1越相似。
     */
    public static double getSimilarity(ArrayList<String> T1, ArrayList<String> T2) throws Exception {
        int size = 0 , size2 = 0 ;
        if ( T1 != null && ( size = T1.size() ) > 0 && T2 != null && ( size2 = T2.size() ) > 0 ) {

            Map<String, double[]> T = new HashMap<String, double[]>();

            //T1和T2的并集T
            String index = null ;
            for ( int i = 0 ; i < size ; i++ ) {
                index = T1.get(i) ;
                if( index != null){
                    double[] c = T.get(index);
                    c = new double[2];
                    c[0] = 1;	//T1的语义分数Ci
                    c[1] = threshold;//T2的语义分数Ci
                    T.put( index, c );
                }
            }

            for ( int i = 0; i < size2 ; i++ ) {
                index = T2.get(i) ;
                if( index != null ){
                    double[] c = T.get( index );
                    if( c != null && c.length == 2 ){
                        c[1] = 1; //T2中也存在，T2的语义分数=1
                    }else {
                        c = new double[2];
                        c[0] = threshold; //T1的语义分数Ci
                        c[1] = 1; //T2的语义分数Ci
                        T.put( index , c );
                    }
                }
            }

            //开始计算，百分比
            Iterator<String> it = T.keySet().iterator();
            double s1 = 0 , s2 = 0, Ssum = 0;  //S1、S2
            while( it.hasNext() ){
                double[] c = T.get( it.next() );
                Ssum += c[0]*c[1];
                s1 += c[0]*c[0];
                s2 += c[1]*c[1];
            }
            //百分比
            return Ssum / Math.sqrt( s1*s2 );
        } else {
            throw new Exception("相似度计算工具类传入参数有问题！");
        }
    }

    /**
     * 加载问题总列表
     */
    public ArrayList<ArrayList<String>> loadQuestionList(){
        ArrayList<ArrayList<String>> QList = new ArrayList<>();
        /** 打开questionPatternList.txt */
        File file = new File(SemanticConfig.getInstance().toAbsolutePath("src/main/resources/statics/questionPatternList.txt"));
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e1) {
            System.err.println("文件不存在");
            e1.printStackTrace();
        }

        try {
            while ((line = br.readLine()) != null) {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(line.split(",")));
                System.out.println(list);
                QList.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return QList;
    }


    /**
     * 加载问题模板 == 分类器标签
     * @return Map<Double, String> == 序号，问题分类
     */
    public Map<Integer,String> loadQuestionTemplates() throws IOException {
        System.out.println("开始加载问题模板");
        Map<Integer, String> questionsPattern = new HashMap<>(16);
        File file = new File(SemanticConfig.getInstance().toAbsolutePath("src/main/resources/statics/question_classification.txt"));
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            System.err.println("OK");
        } catch (FileNotFoundException e1) {
            System.err.println("文件不存在");
            e1.printStackTrace();
        }
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                Integer index = Integer.valueOf(tokens[0].replace("\uFEFF",""));
                String pattern = tokens[1];
                questionsPattern.put(index, pattern);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(questionsPattern);
        br.close();
        return questionsPattern;
    }

}
