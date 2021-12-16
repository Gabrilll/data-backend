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
public class LivingConstructionServiceImpl implements ConstructionService {

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
        Individual livingIndividual = null;
        constructionDAO.addDict(individualName);

        // 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
        livingIndividual = constructionDealingService.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.LIVING).getIndividual();
        constructionDAO.addObjectProperty(livingIndividual, "是", livingIndividual);

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
//            PictureDownloader.picDownload(picSrc, newPicName, semanticConfig.getPicSavePath() + File.separator + OntologyClassEnum.LIVING.getName());
            constructionDAO.addDataProperty(livingIndividual, "picSrc", picSrc);
        }

        constructionDAO.addDataProperty(livingIndividual, "URL信息来源", url);
        constructionDAO.addDataProperty(livingIndividual, "描述", lemmaSummary);
        constructionDAO.addDataProperty(livingIndividual, "歧义说明", polysemantExplain);

        // 添加基本信息
        List<String> parameterNamesFilter = baikePage.getParameterNames();
        List<String> parameterValuesFilter = baikePage.getParameterValues();
        constructionDAO.addDataProperties(livingIndividual, parameterNamesFilter, parameterValuesFilter);
        constructionDAO.addDicts(parameterNamesFilter);

        List<String> aliasFilter=baikePage.getAlias();
        constructionDAO.addAlias(individualName,aliasFilter);

        //生物-科属种
        String[] category={"科","属","种"};
        constructionDealingService.dealProperty(livingIndividual,baikePage,true, Arrays.asList(category),OntologyClassEnum.CATEGORY);

        //生物-位置
        String[] location={"位于","位置","领地","地方"};
        constructionDealingService.dealProperty(livingIndividual,baikePage,true,Arrays.asList(location),OntologyClassEnum.LOCATION);

        //生物-习性
        String[] habit={"习性"};
        constructionDealingService.dealProperty(livingIndividual,baikePage,true,Arrays.asList(habit),OntologyClassEnum.HABIT);

        return false;
    }
}
