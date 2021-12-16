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
public class DocumentConstructionServiceImpl implements ConstructionService {

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
        Individual documentIndividual = null;
        constructionDAO.addDict(individualName);

        List<String> aliasFilter=baikePage.getAlias();
        constructionDAO.addAlias(individualName,aliasFilter);

        // 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
        documentIndividual = constructionDealingService.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.DOCUMENT).getIndividual();
        constructionDAO.addObjectProperty(documentIndividual, "是", documentIndividual);

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
            constructionDAO.addDataProperty(documentIndividual, "picSrc", picSrc);
        }
        constructionDAO.addDataProperty(documentIndividual, "URL信息来源", url);
        constructionDAO.addDataProperty(documentIndividual, "描述", lemmaSummary);
        constructionDAO.addDataProperty(documentIndividual, "歧义说明", polysemantExplain);

        // 添加基本信息
        List<String> parameterNamesFilter = baikePage.getParameterNames();
        List<String> parameterValuesFilter = baikePage.getParameterValues();
        constructionDAO.addDataProperties(documentIndividual, parameterNamesFilter, parameterValuesFilter);
        constructionDAO.addDicts(parameterNamesFilter);

        // 处理文件-发布机构对象
        String[] organization={"机构","组织"};
        constructionDealingService.dealProperty(documentIndividual,baikePage,true, Arrays.asList(organization),OntologyClassEnum.ORGANIZATION);

        // 处理文件-发布时间对象
        String[] time={"时间"};
        constructionDealingService.dealProperty(documentIndividual,baikePage,true,Arrays.asList(time),OntologyClassEnum.TIME);

        return false;
    }


}
