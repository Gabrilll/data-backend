package com.judicature.databackend.util;

import com.judicature.databackend.config.SemanticConfig;
import com.hankcs.hanlp.corpus.io.IOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabri
 */
public class Dict {

    private static List<String> dict=null;
    static SemanticConfig semanticConfig=SemanticConfig.getInstance();

    private Dict(){}

    public static List<String> getDict() {
        if(dict==null){
            dict=new ArrayList<>();
            List<String> content= IOUtil.readLineListWithLessMemory(semanticConfig.getDictPath());
            for(String row :content){
                dict.add(row.split(" ")[0]);
            }
        }
        return dict;
    }

    public static void reload(){

        dict=new ArrayList<>();
        List<String> content= IOUtil.readLineListWithLessMemory(semanticConfig.getDictPath());
        for(String row :content){
            dict.add(row.split(" ")[0]);
        }

    }
}
