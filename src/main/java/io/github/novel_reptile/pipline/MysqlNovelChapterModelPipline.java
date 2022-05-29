package io.github.novel_reptile.pipline;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.novel_reptile.model.NovelChapter;
import io.github.novel_reptile.service.NovelChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 23:04
 * @Version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class MysqlNovelChapterModelPipline extends NovelLockPipline<NovelChapter> {

    private final NovelChapterService novelChapterService;


    @Override
    public Long getKey(NovelChapter obj) {
        return obj.getChapterId();
    }

    @Override
    public void doLockProcess(NovelChapter obj) {
        try {
            NovelChapter oldnovelChapter = novelChapterService.getOne(new LambdaQueryWrapper<NovelChapter>().eq(NovelChapter::getChapterId, obj.getChapterId()));

            if(oldnovelChapter!=null){
                obj.setId(oldnovelChapter.getId());
                novelChapterService.updateById(obj);
            }else{
                novelChapterService.save(obj);
            }
        }catch (Exception e){
            log.error("保存小说章节{}失败,原因:{}", obj,e.getMessage());
        }
    }
}
