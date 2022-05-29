package io.github.novel_reptile.task;

import io.github.novel_reptile.model.Novel;
import io.github.novel_reptile.model.NovelChapter;
import io.github.novel_reptile.pipline.MysqlNovelChapterModelPipline;
import io.github.novel_reptile.pipline.MysqlNovelModelPipline;
import io.github.novel_reptile.service.NovelService;
import io.github.novel_reptile.service.ReptileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;

import java.util.List;

import static io.github.novel_reptile.utils.SiteUtil.getDefaultSite;

/**
 * @Author 翁丞健
 * @Date 2022/5/27 16:43
 * @Version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BiquReptile {

    private final ReptileService reptileService;


    /**
     *  更新小说信息
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateNovel(){
        reptileService.run();
    }

}
