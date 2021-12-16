package com.judicature.databackend.blImpl.semantic;

import com.judicature.databackend.bl.semantic.WordSegmentationService;
import com.judicature.databackend.config.SemanticConfig;
import com.judicature.databackend.enums.OntologyClassEnum;
import com.judicature.databackend.model.PolysemantNamedEntity;
import com.judicature.databackend.model.Word;
import com.judicature.databackend.model.WordSegmentResult;
import com.judicature.databackend.util.Alias;
import com.judicature.databackend.util.Dict;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Gabri
 */
@Service
public class WordSegmentationServiceImpl implements WordSegmentationService {

    SemanticConfig semanticConfig=SemanticConfig.getInstance();


    List<String> dict= Dict.getDict();
    Map<String,String> alias= Alias.getAlias();

    /*以省内存的方式读取 Answer_Dict 词典*/
    LinkedList<String> dictIndividualList=IOUtil.readLineListWithLessMemory(semanticConfig.getQuery_individualDictPath());






    private volatile static WordSegmentationService singleInstance;

    /**
     * 获取单例
     */
    public static WordSegmentationService getInstance() {
        if (singleInstance == null) {
            synchronized (WordSegmentationServiceImpl.class) {
                if (singleInstance == null) {
                    singleInstance = new WordSegmentationServiceImpl();
                }
            }
        }
        return singleInstance;
    }

    /**
     * HanLP 分词以及命名实体识别
     */
    @Override
    public WordSegmentResult wordSegmentation(String question) {
        // 命名实体
        List<PolysemantNamedEntity> polysemantNamedEntities = new ArrayList<>();
        for (String dictRow : dictIndividualList) {
            // 覆盖模式插入
            if(dictRow.split("_").length>1){
                CustomDictionary.insert(dictRow.split("_")[1], "n 2048");
            }
        }

        // 本次分词主要为命名实体识别
        List<Term> terms = HanLP.segment(question);
        for(Term t:terms) {
            t.word = fuzzyMatch(t.word);
        }

        List<Integer> identified=new ArrayList<>();

        for (String dictRow : dictIndividualList) {
            String[] fieldsDict = dictRow.split("_");
            if(fieldsDict.length<6){
                continue;
            }
            // UUID
            String dictIndividualUUID = fieldsDict[0];
            // 实体名
            String dictIndividualName = fieldsDict[1];
            // 歧义说明
            String dictPolysemantExplain = fieldsDict[2];
            // 实体百科页面URL
            String dictIndividualURL = fieldsDict[3];
            // 是否是本名
            String dictIsAliasesWrite = fieldsDict[4];
            // 实体所属类型
            int dictIndividualClass = Integer.parseInt(fieldsDict[5]);
            int id = 1;
            int idx=0;
            for (Term term : terms) {
                if (term.word.equals(dictIndividualName)) {
                    identified.add(idx);
                    PolysemantNamedEntity polysemantNamedEntitiy = new PolysemantNamedEntity();
                    polysemantNamedEntitiy.setUUID(dictIndividualUUID);
                    polysemantNamedEntitiy.setEntityName(dictIndividualName);
                    polysemantNamedEntitiy.setPolysemantExplain(dictPolysemantExplain);
                    polysemantNamedEntitiy.setEntityUrl(dictIndividualURL);
                    polysemantNamedEntitiy.setIsAliases(dictIsAliasesWrite);
                    // 默认均为未激活状态
                    polysemantNamedEntitiy.setActive(false);
                    if (dictIndividualClass == OntologyClassEnum.DISEASE.getIndex()) {
                        polysemantNamedEntitiy.setOntClass(OntologyClassEnum.DISEASE.getName());
                    } else if (dictIndividualClass == OntologyClassEnum.ORGANIZATION.getIndex()) {
                        polysemantNamedEntitiy.setOntClass(OntologyClassEnum.ORGANIZATION.getName());
                    } else if (dictIndividualClass == OntologyClassEnum.DOCUMENT.getIndex()) {
                        polysemantNamedEntitiy.setOntClass(OntologyClassEnum.DOCUMENT.getName());
                    } else if (dictIndividualClass == OntologyClassEnum.HARM.getIndex()) {
                        polysemantNamedEntitiy.setOntClass(OntologyClassEnum.HARM.getName());
                    } else if (dictIndividualClass == OntologyClassEnum.HOSPITAL.getIndex()) {
                        polysemantNamedEntitiy.setOntClass(OntologyClassEnum.HOSPITAL.getName());
                    } else if (dictIndividualClass == OntologyClassEnum.INSTITUTE.getIndex()) {
                        polysemantNamedEntitiy.setOntClass(OntologyClassEnum.INSTITUTE.getName());
                    } else if (dictIndividualClass == OntologyClassEnum.LIVING.getIndex()) {
                        polysemantNamedEntitiy.setOntClass(OntologyClassEnum.LIVING.getName());
                    } else if (dictIndividualClass == OntologyClassEnum.PERSON.getIndex()) {
                        polysemantNamedEntitiy.setOntClass(OntologyClassEnum.PERSON.getName());
                    } else if (dictIndividualClass == OntologyClassEnum.OTHERS.getIndex()) {
                        polysemantNamedEntitiy.setOntClass(OntologyClassEnum.OTHERS.getName());
                    }
                    polysemantNamedEntitiy.setPosition(id);
                    polysemantNamedEntities.add(polysemantNamedEntitiy);
                }
                ++id;
                ++idx;
            }
        }
        // 加载用户词典后的分词
        List<Term> termList = HanLP.segment(question);

        for(int i:identified){
            termList.get(i).word=terms.get(i).word;
        }

        int index = 1;
        List<Word> words = new ArrayList<>();
        for (Term term : terms) {
            Word word = new Word();
            word.setPosition(index);
            word.setName(term.word);
            word.setCpostag(term.nature.toString());
            word.setPostag(term.nature.toString());
            List<PolysemantNamedEntity> wordPolysemantNamedEntities = new ArrayList<>();
            for (PolysemantNamedEntity polysemantNamedEntity : polysemantNamedEntities) {
                if (polysemantNamedEntity.getPosition() == index) {
                    wordPolysemantNamedEntities.add(polysemantNamedEntity);
                }
            }
            word.setPolysemantNamedEntities(wordPolysemantNamedEntities);
            words.add(word);
            ++index;
        }
        // 分词结果
        WordSegmentResult wordSegmentResult = new WordSegmentResult();
        wordSegmentResult.setTerms(termList);
        wordSegmentResult.setPolysemantEntities(polysemantNamedEntities);
        wordSegmentResult.setWords(words);
        return wordSegmentResult;
    }

    public String fuzzyMatch(String word){
        if(word.contains("什么")||word.contains("啥")||word.contains("咋")||word.contains("怎么")){
            return word;
        }
        if(dict.contains(word)){
            return word;
        }

        if(alias.containsKey(word)){
            return alias.get(word);
        }

        String res=word;
        int score=0;
        for(String d:dict){
            int tmp= FuzzySearch.ratio(word,d);
            if(tmp>score){
                score=tmp;
                res=d;
            }
        }

        for(Map.Entry<String,String> e:alias.entrySet()){
            String key=e.getKey();
            String value=e.getValue();
            int tmp=FuzzySearch.ratio(word,key);
            if(tmp>score){
                score=tmp;
                res=value;
            }
        }

        if(score>70){
            alias.put(word,res);
        }

        return res;
    }

    @Override
    public void reload(){
        dictIndividualList=IOUtil.readLineListWithLessMemory(semanticConfig.getQuery_individualDictPath());
    }
}
