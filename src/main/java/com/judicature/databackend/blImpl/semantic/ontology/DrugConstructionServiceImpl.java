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
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author Gabri
 */
@Service
public class DrugConstructionServiceImpl implements ConstructionService {

    private ConstructionDAO constructionDAO = new ConstructionDAOImpl();

    SemanticConfig semanticConfig=SemanticConfig.getInstance();

    @Autowired
    ConstructionDealingService constructionDealingService;

    @Override
    public boolean construction(BaikePage baikePage) throws Exception {
        // 词条标题（实体名）
        String individualName = baikePage.getLemmaTitle();
        String polysemantExplain = baikePage.getPolysemantExplain();
        String url = baikePage.getUrl();
        Individual drugIndividual = null;
        constructionDAO.addDict(individualName);

        // 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
        drugIndividual = constructionDealingService.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.HARM).getIndividual();
        constructionDAO.addObjectProperty(drugIndividual, "是", drugIndividual);

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
//            PictureDownloader.picDownload(picSrc, newPicName, semanticConfig.getPicSavePath() + File.separator + OntologyClassEnum.ORGANIZATION.getName());
            constructionDAO.addDataProperty(drugIndividual, "picSrc", picSrc);
        }

        constructionDAO.addDataProperty(drugIndividual, "URL信息来源", url);
        constructionDAO.addDataProperty(drugIndividual, "描述", lemmaSummary);
        constructionDAO.addDataProperty(drugIndividual, "歧义说明", polysemantExplain);

        // 添加基本信息
        List<String> parameterNamesFilter = baikePage.getParameterNames();
        List<String> parameterValuesFilter = baikePage.getParameterValues();
        constructionDAO.addDataProperties(drugIndividual, parameterNamesFilter, parameterValuesFilter);
        constructionDAO.addDicts(parameterNamesFilter);

        List<String> aliasFilter=baikePage.getAlias();
        constructionDAO.addAlias(individualName,aliasFilter);

        //药物-治疗疾病
        String[] disease={"治疗","疾病","治愈"};
        constructionDealingService.dealProperty(drugIndividual,baikePage,true, Arrays.asList(disease),OntologyClassEnum.DISEASE);

        //药物-禁忌
        String[] forbid={"禁","忌"};
        constructionDealingService.dealProperty(drugIndividual,baikePage,true,Arrays.asList(forbid),OntologyClassEnum.FORBID);

        //药物-用法用量
        String[] usage={"使用","用法","用量"};
        constructionDealingService.dealProperty(drugIndividual,baikePage,true,Arrays.asList(usage),OntologyClassEnum.SYMPTOM);

        //药物-副作用
        String[] sideEffect={"副作用","不良反应"};
        constructionDealingService.dealProperty(drugIndividual,baikePage,true,Arrays.asList(sideEffect),OntologyClassEnum.SIDE_EFFECT);

        return false;
    }
}
