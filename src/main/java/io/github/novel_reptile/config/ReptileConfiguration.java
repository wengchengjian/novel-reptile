package io.github.novel_reptile.config;

import io.github.novel_reptile.handler.CustomProxyProvider;
import io.github.novel_reptile.model.Novel;
import io.github.novel_reptile.model.NovelChapter;
import io.github.novel_reptile.pipline.MysqlNovelChapterModelPipline;
import io.github.novel_reptile.pipline.MysqlNovelModelPipline;
import io.github.novel_reptile.scheduler.RedisScheduler;
import io.github.novel_reptile.service.NovelChapterService;
import io.github.novel_reptile.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;

import static io.github.novel_reptile.utils.SiteUtil.getDefaultSite;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 23:21
 * @Version 1.0.0
 */
@Configuration
public class ReptileConfiguration {

    private static final String BI_QU = "https://www.xbiquwx.la/";

    private static final Integer DEFAULT_NOVEL_THREAD_NUM  = 5;

    @Bean
    public RedisScheduler redisScheduler(@Qualifier("redisTemplate") RedisTemplate<String,Object> template){
        return new RedisScheduler(template);
    }
    @Bean
    public ProxyProvider proxyProvider(NovelProperties properties, RestTemplate restTemplate){
        return new CustomProxyProvider(properties,restTemplate);
    }


    @Bean("novelSpider")
    public Spider novelSpider(@Qualifier("mysqlNovelModelPipline") PageModelPipeline mysqlModelPipline,
                              RedisScheduler redisScheduler){
        return OOSpider.create(getDefaultSite())
                .addPageModel(mysqlModelPipline,Novel.class)
                .addUrl(BI_QU).thread(DEFAULT_NOVEL_THREAD_NUM).setScheduler(redisScheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(10000000)));
    }


    @Bean("mysqlNovelModelPipline")
    public PageModelPipeline<Novel> mysqlModelPipline(NovelService novelService,
                                                      @Qualifier("asyncTaskExcutor")TaskExecutor taskExecutor,
                                                      @Qualifier("mysqlNovelChapterModelPipline") PageModelPipeline<NovelChapter> mysqlNovelChapterModelPipline ){
        return new MysqlNovelModelPipline(novelService,taskExecutor,mysqlNovelChapterModelPipline);
    }

    @Bean("mysqlNovelChapterModelPipline")
    public PageModelPipeline<NovelChapter> mysqlNovelChapterModelPipline(NovelChapterService novelChapterService){
        return new MysqlNovelChapterModelPipline(novelChapterService);
    }
}
