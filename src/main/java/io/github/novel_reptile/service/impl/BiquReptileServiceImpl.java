package io.github.novel_reptile.service.impl;

import io.github.novel_reptile.service.ReptileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 21:00
 * @Version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BiquReptileServiceImpl implements ReptileService {

    @Qualifier("novelSpider")
    private final Spider novelSpider;

    @Override
    public void run() {
        log.info("正在执行爬虫服务");
        novelSpider.run();
    }
}
