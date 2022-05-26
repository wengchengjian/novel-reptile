package io.github.novel_reptile.pipline;

import io.github.novel_reptile.model.Novel;
import io.github.novel_reptile.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 23:04
 * @Version 1.0.0
 */
@RequiredArgsConstructor
public class MysqlModelPipline implements PageModelPipeline<Novel> {

    private final NovelService novelService;

    @Override
    public void process(Novel novel, Task task) {
        novelService.save(novel);
    }
}
