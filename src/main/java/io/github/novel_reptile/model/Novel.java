package io.github.novel_reptile.model;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.*;
import io.github.novel_reptile.utils.NumberUtil;
import lombok.Data;
import org.springframework.util.NumberUtils;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.*;
import us.codecraft.webmagic.model.formatter.ObjectFormatter;

import java.util.Date;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 21:07
 * @Version 1.0.0
 */
@Data
@TableName("novel")
@TargetUrl("https://www.qbiqu.com/\\d+_\\d+/")
@HelpUrl("https://www.qbiqu.com/")
public class Novel {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("novel_id")
    @Formatter(value = "",formatter = StringToLongTemplateFormatter.class)
    @ExtractByUrl("https://www.qbiqu.com/(\\d+_\\d+)/")
    private Long novelId;

    @TableField("novel_name")
    @ExtractBy("//div[@id='maininfo']/div[@id='info']/h1/text()")
    private String novelName;

    @TableField("author")
    @Formatter(value = "",formatter = StringToAuthorTemplateFormatter.class)
    @ExtractBy("//div[@id='maininfo']/div[@id='info']/p[1]/text()")
    private String author;

    @TableField("update_time")
    @Formatter(value = "",formatter = StringToDateTemplateFormatter.class)
    @ExtractBy("//div[@id='maininfo']/div[@id='info']/p[3]/text()")
    private Date updateTime;


    @TableField(value = "create_time")
    private Date createTime;

    @TableField("description")
    @ExtractBy("//div[@id='maininfo']/div[@id='intro']/p[1]/text()")
    private String description;


    public static class StringToLongTemplateFormatter implements  ObjectFormatter<Long>{

        @Override
        public Long format(String s) throws Exception {
            return NumberUtil.parseSpec(s);
        }

        @Override
        public Class<Long> clazz() {
            return Long.class;
        }

        @Override
        public void initParam(String[] strings) {

        }
    }

    public static class StringToDateTemplateFormatter implements ObjectFormatter<Date>{

        @Override
        public Date format(String raw) throws Exception {
            int index = raw.indexOf("：");

            String time = raw.substring(index+1).trim();
            return DateUtil.parse(time);
        }

        @Override
        public Class<Date> clazz() {
            return Date.class;
        }

        @Override
        public void initParam(String[] extra) {

        }
    }

    public static class StringToAuthorTemplateFormatter implements ObjectFormatter<String>{

        @Override
        public String format(String s) throws Exception {
            String[] split = s.split("：");

            String author = split[split.length-1].trim();

            return author;
        }

        @Override
        public Class<String> clazz() {
            return String.class;
        }

        @Override
        public void initParam(String[] strings) {

        }
    }
}
