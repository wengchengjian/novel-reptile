package io.github.novel_reptile.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.novel_reptile.mapper.NovelChapterMapper;
import io.github.novel_reptile.mapper.NovelMapper;
import io.github.novel_reptile.model.Novel;
import io.github.novel_reptile.model.NovelChapter;
import io.github.novel_reptile.service.NovelChapterService;
import io.github.novel_reptile.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 23:18
 * @Version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class NovelChapterServiceImpl extends ServiceImpl<NovelChapterMapper, NovelChapter> implements NovelChapterService {

//    private static final String NOVEL_CHAPTER_PREFIX = "novel_chapter_id";
//
//    private final RedisTemplate<String,Object> redisTemplate;
//
//    public boolean isExist(Serializable id){
//        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
//
//        return Boolean.TRUE.equals(setOperations.isMember(NOVEL_CHAPTER_PREFIX, id));
//    }
//
//    @Override
//    public boolean save(NovelChapter entity) {
//        if(!isExist(entity.getNovelId())){
//            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
//
//            setOperations.add(NOVEL_CHAPTER_PREFIX,entity.getChapterId());
//
//            return super.save(entity);
//        }
//        return false;
//    }
}
