package io.github.novel_reptile.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.novel_reptile.mapper.NovelMapper;
import io.github.novel_reptile.model.Novel;
import io.github.novel_reptile.service.NovelService;
import org.springframework.stereotype.Service;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 23:18
 * @Version 1.0.0
 */
@Service
public class NovelServiceImpl extends ServiceImpl<NovelMapper, Novel> implements NovelService {

}
