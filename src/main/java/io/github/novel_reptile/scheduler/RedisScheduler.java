package io.github.novel_reptile.scheduler;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

/**
 * @Author 翁丞健
 * @Date 2022/5/27 15:20
 * @Version 1.0.0
 */
public class RedisScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, DuplicateRemover {
    protected RedisTemplate<String,Object> template;

    private static final String QUEUE_PREFIX = "queue_";

    private static final String SET_PREFIX = "set_";

    private static final String ITEM_PREFIX = "item_";


    public RedisScheduler(RedisTemplate<String,Object> template ) {
        this.template = template;
        setDuplicateRemover(this);
    }

    @Override
    public void resetDuplicateCheck(Task task) {
            template.delete(getSetKey(task));
    }

    @Override
    public boolean isDuplicate(Request request, Task task) {
        SetOperations<String, Object> setOperation = template.opsForSet();
        return Boolean.TRUE.equals(setOperation.isMember(getSetKey(task), request.getUrl()));
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        ListOperations<String, Object> listOperations = template.opsForList();

        HashOperations<String, Object, Object> hashOperation = template.opsForHash();

        listOperations.rightPush(getQueueKey(task), request.getUrl());

        if (checkForAdditionalInfo(request)) {
            String field = DigestUtils.shaHex(request.getUrl());
            String value = JSON.toJSONString(request);
            hashOperation.put((ITEM_PREFIX + task.getUUID()), field, value);
        }


    }

    private boolean checkForAdditionalInfo(Request request) {
        if (request == null) {
            return false;
        }

        if (!request.getHeaders().isEmpty() || !request.getCookies().isEmpty()) {
            return true;
        }

        if (StringUtils.isNotBlank(request.getCharset()) || StringUtils.isNotBlank(request.getMethod())) {
            return true;
        }

        if (request.isBinaryContent() || request.getRequestBody() != null) {
            return true;
        }

        if (request.getExtras() != null && !request.getExtras().isEmpty()) {
            return true;
        }
        if (request.getPriority() != 0L) {
            return true;
        }

        return false;
    }

    @Override
    public synchronized Request poll(Task task) {
        ListOperations<String, Object> listOperations = template.opsForList();

        HashOperations<String, String, String> hashOperation = template.opsForHash();

        String url = (String) listOperations.leftPop(getQueueKey(task));

        if (url == null) {
            return null;
        }
        String key = ITEM_PREFIX + task.getUUID();
        String field = DigestUtils.shaHex(url);
        String str =  hashOperation.get(key, field);
        if (str != null) {
            Request o = JSON.parseObject(str, Request.class);
            return o;
        }
        Request request = new Request(url);
        return request;

    }

    protected String getSetKey(Task task) {
        return SET_PREFIX + task.getUUID();
    }

    protected String getQueueKey(Task task) {
        return QUEUE_PREFIX + task.getUUID();
    }

    protected String getItemKey(Task task) {
        return ITEM_PREFIX + task.getUUID();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        ListOperations<String, Object> listOperations = template.opsForList();
        Long size = listOperations.size(getQueueKey(task));
        return size.intValue();

    }

    @Override
    public int getTotalRequestsCount(Task task) {
        SetOperations<String, Object> setOperations = template.opsForSet();
        Long size = setOperations.size(getSetKey(task));
        return size.intValue();

    }
}
