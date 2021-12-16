package com.judicature.databackend.util;

import com.judicature.databackend.config.SemanticConfig;
import com.hankcs.hanlp.corpus.io.IOUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gabri
 */
public class Alias {
    private static Map<String,String> alias=null;

    static SemanticConfig semanticConfig=SemanticConfig.getInstance();

    private Alias(){}

    public static Map<String, String> getAlias() {
        if(alias==null){
            alias=new HashMap<>();
            List<String> content= IOUtil.readLineListWithLessMemory(semanticConfig.getQuery_aliasPath());
            for(String row :content){
                String[] ss=row.split(" ");
                alias.put(ss[0],ss[1]);
            }
        }
        return alias;
    }

    public static Map<String,String> reload(){
        alias=new HashMap<>();
        List<String> content= IOUtil.readLineListWithLessMemory(semanticConfig.getQuery_aliasPath());
        for(String row :content){
            String[] ss=row.split(" ");
            alias.put(ss[0],ss[1]);
        }
        return alias;
    }
}
