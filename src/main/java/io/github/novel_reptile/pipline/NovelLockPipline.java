package io.github.novel_reptile.pipline;

import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 翁丞健
 * @Date 2022/5/28 17:17
 * @Version 1.0.0
 */
public abstract class NovelLockPipline<T> implements PageModelPipeline<T> {

    private final Object lock = new Object();

    /**
     * ChapterId -> Lock
     */
    private final Map<Long,Object> locksMap = new ConcurrentHashMap<>();

    /**
     * 双重检验
     * @param id
     */
    public void createLock(Long id){
        if(locksMap.get(id)==null){
            synchronized (lock){
                // 创建锁对象
                locksMap.computeIfAbsent(id, k -> new Object());
            }
        }
    }

    public abstract Long getKey(T obj);

    public abstract void doLockProcess(T obj);

    @Override
    public void process(T obj, Task task) {

        Long key = getKey(obj);
        createLock(key);
        lockProcess(key,obj);
    }

    public void lockProcess(Long key,T obj){
        synchronized (locksMap.get(key)){
            doLockProcess(obj);
        }
    }
}
