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
 * 人物本体构建
 *
 * @author Gabri
 */
@Service
@Component
public class PersonConstructionServiceImpl implements ConstructionService {

    private final ConstructionDAO constructionDAO = new ConstructionDAOImpl();
    SemanticConfig semanticConfig=SemanticConfig.getInstance();

    @Autowired
    ConstructionDealingService constructionDealingService;

    @Override
    public boolean construction(BaikePage baikePage) throws Exception {
        // 词条标题（实体名）
        String individualName = baikePage.getLemmaTitle();
        String polysemantExplain = baikePage.getPolysemantExplain();
        String url = baikePage.getUrl();
        Individual personIndividual = null;
        constructionDAO.addDict(individualName);

        // 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
        personIndividual = constructionDealingService.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.PERSON).getIndividual();
        constructionDAO.addObjectProperty(personIndividual, "是", personIndividual);

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
//            PictureDownloader.picDownload(picSrc, newPicName, semanticConfig.getPicSavePath() + File.separator + OntologyClassEnum.DISEASE.getName());
            constructionDAO.addDataProperty(personIndividual, "picSrc", picSrc);
        }
        constructionDAO.addDataProperty(personIndividual, "URL信息来源", url);
        constructionDAO.addDataProperty(personIndividual, "描述", lemmaSummary);
        constructionDAO.addDataProperty(personIndividual, "歧义说明", polysemantExplain);

        // 添加关系
		/*List<String> relationNames = baikePage.getRelationNames();
		List<String> relationValues = baikePage.getParameterValues();
		List<String> relationUrls = baikePage.getRelationUrls();*/
        if (baikePage.getRelationNames()!=null&&baikePage.getRelationValues()!=null&&baikePage.getRelationValues().size()
                !=0&& baikePage.getRelationValues().size() != 0) {
            constructionDealingService.dealProperty(personIndividual,baikePage,true, baikePage.getRelationNames(),OntologyClassEnum.HABIT);
        }

        // 添加基本信息
        List<String> parameterNamesFilter = baikePage.getParameterNames();
        List<String> parameterValuesFilter = baikePage.getParameterValues();
        constructionDAO.addDataProperties(personIndividual, parameterNamesFilter, parameterValuesFilter);
        constructionDAO.addDicts(parameterNamesFilter);

        List<String> aliasFilter=baikePage.getAlias();
        constructionDAO.addAlias(individualName,aliasFilter);

        //人物-人物
        String[] person={"人物"};
        constructionDealingService.dealProperty(personIndividual,baikePage,true,Arrays.asList(person),OntologyClassEnum.PERSON);

        //人物-事件
        String[] event={"事件"};
        constructionDealingService.dealProperty(personIndividual,baikePage,true,Arrays.asList(event),OntologyClassEnum.EVENT);


        //人物-机构
        String[] organization={"组织"};
        constructionDealingService.dealProperty(personIndividual,baikePage,true,Arrays.asList(organization),OntologyClassEnum.ORGANIZATION);


        return false;
    }

}