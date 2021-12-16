package com.judicature.databackend.crawler;

import com.judicature.databackend.config.SemanticConfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabri
 */
@Service
@Component
public class OntologyConstructionPageProcessor implements PageProcessor {

    private final Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    SemanticConfig semanticConfig=SemanticConfig.getInstance();

    @Override
    public void process(Page page) {
        // 词条图片
        String picSrc = page.getHtml().xpath("//div[@class='summary-pic']//img/@src").toString();
        if (picSrc == null) {
            picSrc = page.getHtml().xpath("//dl[@class='lemmaWgt-albumList-poster']//img/@src").toString();
        }
        // 词条图片
        page.putField("picSrc", picSrc);

        // 内容一：词条标题
        String lemmaTitle = page.getHtml().xpath("//dd[@class='lemmaWgt-lemmaTitle-title']/h1/allText()").toString();
        page.putField("lemmaTitle", lemmaTitle);

        //        System.out.println(lemmaTitle);

        // 内容二：词条概括（词条描述）
        String lemmaSummary = page.getHtml().xpath("//div[@class='lemma-summary']/allText()").toString();
        page.putField("lemmaSummary", lemmaSummary);

        // 内容三：所有同名实体
        List<String> polysemants = page.getHtml().xpath("//ul[@class='polysemantList-wrapper']/allText()").replace("▪", "").all();

        /*获取所有别名和简称*/
        String content = page.getHtml().getFirstSourceText();
        List<String> alias = new ArrayList<>();
        if (content.contains("简称")) {
            int start = content.indexOf("简称") + 2;
            getAlias(lemmaTitle, content, alias, start);
        }

        if (content.contains("别名")) {
            int start = content.indexOf("别名") + 2;
            getAlias(lemmaTitle, content, alias, start);
        }

        page.putField("alias", alias);

        // 当前实体的同名说明
        String polysemantExplain = page.getHtml().xpath("//ul[@class='polysemantList-wrapper']/li[@class='item']/span[@class='selected']/allText()").toString();
        page.putField("polysemants", polysemants);
        page.putField("polysemantExplain", polysemantExplain);

        // 内容四：关系
        //        List<String> relationNames = page.getHtml().xpath("//div[@class='star-info-block relations']//div[@id='slider_relations']//div[@class='name']/text(1)").all();
        //        List<String> relationValues = page.getHtml().xpath("//div[@class='star-info-block relations']//div[@id='slider_relations']//div[@class='name']/em/text(1)").all();
        //        List<String> relationUrls = page.getHtml().xpath("//div[@class='star-info-block relations']//div[@id='slider_relations']//a/@href").all();
        //        page.putField("relationNames", relationNames);
        //        page.putField("relationValues", relationValues);
        //        page.putField("relationUrls", relationUrls);

        // 内容五：词条属性
        List<String> parameterNames = page.getHtml().xpath("div//dt[@class='basicInfo-item name']/allText()").all();
        List<String> parameterValues = page.getHtml().xpath("div//dd[@class='basicInfo-item value']/allText()").all();
        List<String> parameterHasUrlValues = page.getHtml().xpath("div//dd[@class='basicInfo-item value']/a/allText()").all();
        List<String> parameterHasUrl = page.getHtml().xpath("div//dd[@class='basicInfo-item value']/a/@href").all();
        page.putField("parameterNames", parameterNames);
        page.putField("parameterValues", parameterValues);
        page.putField("parameterHasUrlValues", parameterHasUrlValues);
        page.putField("parameterHasUrl", parameterHasUrl);


        List<String> categories = page.getHtml().xpath("div[@class='catalog-list']/ol/li[@class='level1']/span[@class='text']/a/allText()").all();

        page.putField("categories",categories);
        //        System.out.println("categories:"+categories);
        // 对象属性URL

        // 词条主题和词条概括都为空则跳过 避免抓取http://baike.baidu.com/m#download百科客户端下载页之类的无关页面
        if (page.getResultItems().get("lemmaTitle") == null && page.getResultItems().get("lemmaSummary") == null) {
            // skip this page
            page.setSkip(true);
        }

        if (page.getResultItems().getRequest().getUrl().split("#").length >= 2) {
            page.setSkip(true);
        }

        if (lemmaTitle!=null &&(lemmaTitle.contains("百度") || lemmaTitle.contains("秒懂") || lemmaTitle.contains("百科"))) {
            page.setSkip(true);
        }

        // 按照自增长抓取百科页面
        //        /*
        ++OntologyConstructionLauncher.pageCount;
        //        System.out.println(lemmaTitle);
        //        page.addTargetRequest("http://baike.baidu.com/view/" + OntologyConstructionLauncher.pageCount);
        for (String url : parameterHasUrl) {
            page.addTargetRequest(url);
        }

        List<String> relatedUrls = page.getHtml().xpath("div[@class='para']/a/@href").all();
        //        System.out.println(relatedUrls);
        for (String url : relatedUrls) {
            page.addTargetRequest(url);
        }
        if (OntologyConstructionLauncher.pageCount % 50 == 0) {
            System.out.println("已经爬取百科页面数量:" + OntologyConstructionLauncher.pageCount);
        }
        //        */
        if (semanticConfig.getPageNum() == null) {
            // 默认处理10个页面
            semanticConfig.setPageNum(10L);
        } else if (OntologyConstructionLauncher.pageCount < semanticConfig.getPageNum()) {
            page.addTargetRequests(page.getHtml().links().regex("http://baike\\.baidu\\.com/.*").all());
        }

        //        page.addTargetRequest("http://baike.baidu.com/item/火影忍者"); // 火影忍者
    }

    /**
     * 获取别名或简称
     */
    private void getAlias(String lemmaTitle, String content, List<String> alias, int start) {
        for (int i = start; i < start + 2 * lemmaTitle.length(); i++) {
            char c = content.charAt(i);
            if (c == ',' || c == ';' || c == '.' || c == ')' || c == '，' || c == '。' || c == '；' || c == '）') {
                String a=content.substring(start, i);
                a=a.replaceAll(" ","_");
                if(a.length()>0){
                    alias.add(a);
                }
                break;
            }

        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
