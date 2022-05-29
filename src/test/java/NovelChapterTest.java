import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import io.github.novel_reptile.model.NovelChapter;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.Test;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 翁丞健
 * @Date 2022/5/27 13:38
 * @Version 1.0.0
 */
public class NovelChapterTest {

    @Test
    public void test(){
        String url = "http[s]?://*.*.*/\\d+_\\d+/";
        Pattern compile = Pattern.compile(url.replace(".", "\\.").replace("*", "[^\"'#]*"));
        Matcher daweae = compile.matcher("http://www.b5200.org/1_1/");
        System.out.println(daweae.find());
        for(int i =0;i<1000;i++){
            System.out.println(HttpRequest.get("https://www.mayiwxw.com/chuanyuexiaoshuo/").execute());
        }
    }

    public static class ConsolePipline implements PageModelPipeline<NovelChapter>{

        @Override
        public void process(NovelChapter novelChapter, Task task) {
            System.out.println(novelChapter);
        }
    }
}
