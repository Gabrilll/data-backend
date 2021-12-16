package com.judicature.databackend.blImpl.semantic.ontology;

import com.judicature.databackend.bl.semantic.ConstructionDealingService;
import com.judicature.databackend.bl.semantic.ConstructionService;
import com.judicature.databackend.config.SemanticConfig;
import com.judicature.databackend.data.semantic.ConstructionDAO;
import com.judicature.databackend.data.semantic.ConstructionDAOImpl;
import com.judicature.databackend.enums.OntologyClassEnum;
import com.judicature.databackend.model.BaikePage;
import org.apache.jena.ontology.Individual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author Gabri
 */
@Service
@Component
public class DiseaseConstructionServiceImpl implements ConstructionService {

    private ConstructionDAO constructionDAO = new ConstructionDAOImpl();

    @Autowired
    ConstructionDealingService constructionDealingService;

    SemanticConfig semanticConfig=SemanticConfig.getInstance();

    @Override
    public boolean construction(BaikePage baikePage) throws Exception {
        // 词条标题（实体名）
        String individualName = baikePage.getLemmaTitle();
        /*当前实体对应的的同名词解释*/
        String polysemantExplain = baikePage.getPolysemantExplain();
        String url = baikePage.getUrl();
        Individual diseaseIndividual = null;
        constructionDAO.addDict(individualName);

        /*属性*/
        List<String> parameterNamesFilter = baikePage.getParameterNames();
        List<String> parameterValuesFilter = baikePage.getParameterValues();

        // 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
        diseaseIndividual = constructionDealingService.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.DISEASE,parameterNamesFilter,parameterValuesFilter).getIndividual();
        constructionDAO.addObjectProperty(diseaseIndividual, "是", diseaseIndividual);

        // 添加数据属性（描述和歧义说明）
        String lemmaSummary = baikePage.getLemmaSummary();
        String picSrc = baikePage.getPicSrc();
        if (picSrc != null) {
//            // 取得当前时间
//            long times = System.currentTimeMillis();
//            // 生成0-1000的随机数
//            int random = (int) (Math.random() * 1000);
//            // 扩展名称
//            String newPicName = times + "" + random + ".jpg";
//            PictureDownloader.picDownload(picSrc, newPicName, semanticConfig.getPicSavePath() + File.separator + OntologyClassEnum.DOCUMENT.getName());
            constructionDAO.addDataProperty(diseaseIndividual, "picSrc", picSrc);
        }
        constructionDAO.addDataProperty(diseaseIndividual, "URL信息来源", url);
        constructionDAO.addDataProperty(diseaseIndividual, "描述", lemmaSummary);
        constructionDAO.addDataProperty(diseaseIndividual, "歧义说明", polysemantExplain);

        // 添加基本信息
        constructionDAO.addDataProperties(diseaseIndividual, parameterNamesFilter, parameterValuesFilter);
        constructionDAO.addDicts(parameterNamesFilter);

        List<String> aliasFilter=baikePage.getAlias();
        constructionDAO.addAlias(individualName,aliasFilter);

        // 处理疾病-传播对象（疾病传播手段）
        String[] propagations={"传播","传染","感染"};
        constructionDealingService.dealProperty(diseaseIndividual,baikePage,true,Arrays.asList(propagations),OntologyClassEnum.PROPAGATION);

        // 处理疾病-治疗对象（治疗手段）
        String[] treat={"治疗","治愈","痊愈"};
        constructionDealingService.dealProperty(diseaseIndividual,baikePage,true,Arrays.asList(treat),OntologyClassEnum.TREAT);

        // 处理疾病-预防对象（预防方式）
        String[] prevention={"预防","防治","防止","防护"};
        constructionDealingService.dealProperty(diseaseIndividual,baikePage,true,Arrays.asList(prevention),OntologyClassEnum.PREVENTION);

        //处理疾病-药物对象
        String[] drugs={"药"};
        constructionDealingService.dealProperty(diseaseIndividual,baikePage,true,Arrays.asList(drugs),OntologyClassEnum.DRUG);

        //疾病-症状
        String[] symptom={"症状","病症"};
        constructionDealingService.dealProperty(diseaseIndividual,baikePage,true,Arrays.asList(symptom),OntologyClassEnum.SYMPTOM);
        return false;
    }

}
