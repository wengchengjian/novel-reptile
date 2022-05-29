package io.github.novel_reptile.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import us.codecraft.webmagic.model.annotation.*;

import java.util.Date;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 22:15
 * @Version 1.0.0
 */
@Data
@TableName("novel_chapter")
@TargetUrl("https://www.xbiquwx.la/\\d+_\\d+/\\d+.html")
public class NovelChapter {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("chapter_id")
    @Formatter(value = "",formatter = Novel.StringToLongTemplateFormatter.class)
    @ExtractByUrl("https://www.xbiquwx.la/\\d+_\\d+/(\\d+).html")
    private Long chapterId;

    @TableField("novel_id")
    @Formatter(value = "",formatter = Novel.StringToLongTemplateFormatter.class)
    @ExtractByUrl("https://www.xbiquwx.la/(\\d+_\\d+)/")
    private Long novelId;

    @TableField("chapter_name")
    @ExtractBy("//div[@class='bookname']/h1/text()")
    private String chapterName;

    @TableField("chapter_content")
    @ExtractBy("//div[@id='content']/text()")
    private String chapterContent;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;


}
