package io.github.novel_reptile.pipline;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import io.github.novel_reptile.model.Novel;
import io.github.novel_reptile.model.NovelChapter;
import io.github.novel_reptile.service.NovelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;

import java.sql.SQLIntegrityConstraintViolationException;

import static io.github.novel_reptile.utils.SiteUtil.getDefaultSite;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 23:04
 * @Version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class MysqlNovelModelPipline extends NovelLockPipline<Novel> {

    private final NovelService novelService;

    private final TaskExecutor taskExecutor;

    private final PageModelPipeline<NovelChapter> mysqlNovelChapterModelPipline;

    private static final Integer DEFAULT_NOVEL_CHAPTER_THREAD_NUM  = 1;


    @Override
    public Long getKey(Novel obj) {
        return obj.getNovelId();
    }

    @Override
    public void doLockProcess(Novel novel) {
        try{
            Novel oldNovel = novelService.getOne(new LambdaQueryWrapper<Novel>().eq(Novel::getNovelId, novel.getNovelId()));

            if(oldNovel==null || oldNovel.getUpdateTime().before(novel.getUpdateTime())){
                // 更新小说章节
                taskExecutor.execute(OOSpider.create(getDefaultSite())
                        .addPageModel(mysqlNovelChapterModelPipline, NovelChapter.class)
                        .thread(DEFAULT_NOVEL_CHAPTER_THREAD_NUM).addUrl(novel.getSourceUrl()));
                if(oldNovel!=null){
                    novel.setId(oldNovel.getId());
                    novelService.updateById(novel);
                }else{
                    novelService.save(novel);
                }
            }
        }catch (Exception e){
            log.error("保存小说{}失败,原因:{}",novel,e.getMessage());
        }
    }

}
