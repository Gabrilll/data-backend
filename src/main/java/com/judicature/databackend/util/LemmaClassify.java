package com.judicature.databackend.util;

import com.judicature.databackend.bl.semantic.ConstructionService;
import com.judicature.databackend.blImpl.semantic.ontology.*;
import com.judicature.databackend.enums.OntologyClassEnum;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Gabri
 */
@Component
public class LemmaClassify {

    private static DiseaseConstructionServiceImpl diseaseConstructionService;

    private static DocumentConstructionServiceImpl documentConstructionService;

    private static DrugConstructionServiceImpl drugConstructionService;

    private static EventConstructionServiceImpl eventConstructionService;

    private static HospitalConstructionServiceImpl hospitalConstructionService;

    private static LivingConstructionServiceImpl livingConstructionService;

    private static OrganizationConstructionServiceImpl organizationConstructionService;

    private static OthersConstructionServiceImpl othersConstructionService;

    private static PersonConstructionServiceImpl personConstructionService;

    @Autowired
    private DiseaseConstructionServiceImpl diseaseConstructionService0;

    @Autowired
    private DocumentConstructionServiceImpl documentConstructionService0;

    @Autowired
    private DrugConstructionServiceImpl drugConstructionService0;

    @Autowired
    private EventConstructionServiceImpl eventConstructionService0;

    @Autowired
    private HospitalConstructionServiceImpl hospitalConstructionService0;

    @Autowired
    private LivingConstructionServiceImpl livingConstructionService0;

    @Autowired
    private OrganizationConstructionServiceImpl organizationConstructionService0;

    @Autowired
    private OthersConstructionServiceImpl othersConstructionService0;

    @Autowired
    private PersonConstructionServiceImpl personConstructionService0;



    /**
     * 对词条进行分类 返回为类型枚举
     * TODO 对关键属性的是否应该使用枚举
     *
     * @return 实体类型
     */
    public static OntologyClassEnum classify(List<String> catalogue) {
        // 如果该页面没有实体属性 则将该页面分类到其他类别中
        if (catalogue == null) {
            return OntologyClassEnum.OTHERS;
        }
        for (String cat : catalogue) {
            if (cat.contains("传播")||cat.contains("治疗")||cat.contains("病因")||cat.contains("诊断")||cat.contains("检查")||cat.contains("症状")||cat.contains("预防")||cat.contains("用药")) {
                return OntologyClassEnum.DISEASE;
            }
            if (cat.contains("组织")||cat.contains("机构")) {
                return OntologyClassEnum.ORGANIZATION;
            }
            if (cat.contains("人物")) {
                return OntologyClassEnum.PERSON;
            }
            if (cat.contains("修订")) {
                return OntologyClassEnum.DOCUMENT;
            }
            if (cat.contains("校长")||cat.contains("校训")||cat.contains("校徽")||cat.contains("校名")||cat.contains("教学")||cat.contains("学院")||cat.contains("图书馆")||cat.contains("校区")||cat.contains("校友")||cat.contains("教授")) {
                return OntologyClassEnum.INSTITUTE;
            }
            if (cat.contains("医院")||cat.contains("医生")||cat.contains("医疗设施")) {
                return OntologyClassEnum.HOSPITAL;
            }
            if (cat.contains("繁殖") || cat.contains("运动") || cat.contains("生物")) {
                return OntologyClassEnum.LIVING;
            }
            if (cat.contains("损伤")) {
                return OntologyClassEnum.HARM;
            }
        }
        return OntologyClassEnum.OTHERS;
    }

    /**
     * 对词条进行分类 返回为类型枚举
     *
     * @return
     */
    public static ConstructionService classify(OntologyClassEnum ontologyClassEnum) {
        switch (ontologyClassEnum) {
            case DISEASE:
                return diseaseConstructionService;
            case ORGANIZATION:
                return organizationConstructionService;
            case PERSON:
                return personConstructionService;
            case DOCUMENT:
                return documentConstructionService;
            case INSTITUTE:
                return organizationConstructionService;
            case HOSPITAL:
                return hospitalConstructionService;
            case LIVING:
                return livingConstructionService;
            case DRUG:
                return drugConstructionService;
            default:
                return othersConstructionService;
        }
    }

    @PostConstruct
    @Autowired
    public void init(){
        diseaseConstructionService=this.diseaseConstructionService0;
        documentConstructionService=this.documentConstructionService0;
        drugConstructionService=this.drugConstructionService0;
        eventConstructionService=this.eventConstructionService0;
        hospitalConstructionService=this.hospitalConstructionService0;
        livingConstructionService=this.livingConstructionService0;
        organizationConstructionService=this.organizationConstructionService0;
        personConstructionService=this.personConstructionService0;
        othersConstructionService=this.othersConstructionService0;
    }

    public static OntologyClassEnum proClassify(String pro){
        // 如果该页面没有实体属性 则将该页面分类到其他类别中
      return fuzzyMatch(pro);
    }

    public static OntologyClassEnum fuzzyMatch(String word){
        OntologyClassEnum res=OntologyClassEnum.OTHERS;
        int score=0;
        for(OntologyClassEnum ontologyClassEnum:OntologyClassEnum.values()){
            int tmp= FuzzySearch.ratio(word,ontologyClassEnum.getName());
            if(tmp>score && tmp>60){
                score=tmp;
                res=ontologyClassEnum;
            }
        }

        return res;
    }
}
