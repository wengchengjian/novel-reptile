package io.github.novel_reptile.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 23:20
 * @Version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class ReptileRunner implements ApplicationRunner {

    @Qualifier("novelSpider")
    private final Spider novelSpider;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        novelSpider.run();
    }
}
