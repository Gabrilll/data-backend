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
public class OrganizationConstructionServiceImpl implements ConstructionService {

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
        Individual instituteIndividual = null;
        constructionDAO.addDict(individualName);

        // 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
        instituteIndividual = constructionDealingService.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.INSTITUTE).getIndividual();
        constructionDAO.addObjectProperty(instituteIndividual, "是", instituteIndividual);

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
//            PictureDownloader.picDownload(picSrc, newPicName, semanticConfig.getPicSavePath() + File.separator + OntologyClassEnum.INSTITUTE.getName());
            constructionDAO.addDataProperty(instituteIndividual, "picSrc", picSrc);
        }
        constructionDAO.addDataProperty(instituteIndividual, "URL信息来源", url);
        constructionDAO.addDataProperty(instituteIndividual, "描述", lemmaSummary);
        constructionDAO.addDataProperty(instituteIndividual, "歧义说明", polysemantExplain);

        // 添加基本信息
        List<String> parameterNamesFilter = baikePage.getParameterNames();
        List<String> parameterValuesFilter = baikePage.getParameterValues();
        constructionDAO.addDataProperties(instituteIndividual, parameterNamesFilter, parameterValuesFilter);
        constructionDAO.addDicts(parameterNamesFilter);

        List<String> aliasFilter=baikePage.getAlias();
        constructionDAO.addAlias(individualName,aliasFilter);

        //机构-人物
        String[] person={"人物","校友","老师","学生","教授"};
        constructionDealingService.dealProperty(instituteIndividual,baikePage,true, Arrays.asList(person),OntologyClassEnum.PERSON);

        //机构-成立时间
        String[] time={"成立","建立"};
        constructionDealingService.dealProperty(instituteIndividual,baikePage,true,Arrays.asList(time),OntologyClassEnum.TIME);

        //机构-位置
        String[] location={"位置","位于"};
        constructionDealingService.dealProperty(instituteIndividual,baikePage,true,Arrays.asList(location),OntologyClassEnum.LOCATION);
        return false;
    }

}
