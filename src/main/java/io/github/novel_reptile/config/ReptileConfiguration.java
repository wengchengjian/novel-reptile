package io.github.novel_reptile.config;

import io.github.novel_reptile.model.Novel;
import io.github.novel_reptile.pipline.MysqlModelPipline;
import io.github.novel_reptile.service.NovelService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import static io.github.novel_reptile.utils.SiteUtil.getDefaultSite;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 23:21
 * @Version 1.0.0
 */
@Configuration
public class ReptileConfiguration {

    private static final String BI_QU = "https://www.qbiqu.com/";

    private static final Integer DEFAULT_THREAD_NUM  = 5;

    @Bean("novelSpider")
    public Spider novelSpider(@Qualifier("mysqlModelPipline") PageModelPipeline mysqlModelPipline){
        return OOSpider.create(getDefaultSite())
                .addPageModel(mysqlModelPipline,Novel.class)
                .addUrl(BI_QU).thread(DEFAULT_THREAD_NUM);
    }

    @Bean("mysqlModelPipline")
    public PageModelPipeline<Novel> mysqlModelPipline(NovelService novelService){
        return new MysqlModelPipline(novelService);
    }
}
