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

import java.util.List;

/**
 * @author Gabri
 */
@Service
@Component
public class OthersConstructionServiceImpl implements ConstructionService {


    ConstructionDAO constructionDAO = new ConstructionDAOImpl();

    SemanticConfig semanticConfig=SemanticConfig.getInstance();

    @Autowired
    ConstructionDealingService constructionDealingService;


    @Override
    public boolean construction(BaikePage baikePage) throws Exception {
        // 词条标题（实体名）
        String individualName = baikePage.getLemmaTitle();
        String polysemantExplain = baikePage.getPolysemantExplain();
        String url = baikePage.getUrl();
        Individual othersIndividual = null;
        constructionDAO.addDict(individualName);

        List<String> parameterNamesFilter = baikePage.getParameterNames();
        List<String> parameterValuesFilter = baikePage.getParameterValues();

        // 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
        othersIndividual = constructionDealingService.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.OTHERS,parameterNamesFilter,parameterValuesFilter).getIndividual();
        constructionDAO.addObjectProperty(othersIndividual, "是", othersIndividual);

        // 添加数据属性（描述和歧义说明）
        String lemmaSummary = baikePage.getLemmaSummary();
        String picSrc = baikePage.getPicSrc();
        if (picSrc != null) {
            // 取得当前时间
//            long times = System.currentTimeMillis();
//            // 生成0-1000的随机数
//            int random = (int) (Math.random() * 1000);
//            // 扩展名称
//            String newPicName = times + "" + random + ".jpg";
//            PictureDownloader.picDownload(picSrc, newPicName, semanticConfig.getPicSavePath() + File.separator + OntologyClassEnum.OTHERS.getName());
            constructionDAO.addDataProperty(othersIndividual, "picSrc", picSrc);
        }

        constructionDAO.addDataProperty(othersIndividual, "URL信息来源", url);
//        constructionDAO.addDataProperty(othersIndividual, "picSrc", picSrc);
        constructionDAO.addDataProperty(othersIndividual, "描述", lemmaSummary);
        constructionDAO.addDataProperty(othersIndividual, "歧义说明", polysemantExplain);

        // 添加基本信息
        constructionDAO.addDataProperties(othersIndividual, parameterNamesFilter, parameterValuesFilter);
        constructionDAO.addDicts(parameterNamesFilter);

        List<String> aliasFilter=baikePage.getAlias();
        constructionDAO.addAlias(individualName,aliasFilter);

        constructionDealingService.dealProperty(othersIndividual,baikePage,true,parameterNamesFilter,OntologyClassEnum.OTHERS);
        return false;
    }

}
