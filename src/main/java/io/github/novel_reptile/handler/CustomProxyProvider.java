package io.github.novel_reptile.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.novel_reptile.common.Result;
import io.github.novel_reptile.config.NovelProperties;
import io.github.novel_reptile.model.IpAgentModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static io.github.novel_reptile.utils.IpUtil.checkProxyIp;

/**
 * @Author 翁丞健
 * @Date 2022/5/30 19:50
 * @Version 1.0.0
 */
@Slf4j
@Data
@RequiredArgsConstructor
public class CustomProxyProvider implements ProxyProvider {

    private final NovelProperties properties;

    private final RestTemplate restTemplate;

    private  AtomicInteger pointer;

    /**
     * 代理地址
     */
    private List<Proxy> proxies = new CopyOnWriteArrayList<>();


    private  AtomicBoolean enabled = new AtomicBoolean(false);

    /**
     * 是否是远程地址
     */
    private boolean remote;

    /**
     * 获取代理的远程地址
     */
    private String address;

    /**
     * 需要认证吗
     */
    private boolean needAuth;

    /**
     * 获取代理的超时时间
     */
    private Integer timeout;

    private Integer mulit = 2;

    private long recoveryTime = 0L;

    private  AtomicInteger failNum = new AtomicInteger(0);


    @PostConstruct
    public void init(){
        NovelProperties.Reptile reptile = properties.getReptile();
        NovelProperties.Reptile.ProxyConfig proxyConfig = reptile.getProxyConfig();
        enabled = new AtomicBoolean(proxyConfig.isEnabled());
        address = proxyConfig.getAddress();
        remote = proxyConfig.isRemote();
        needAuth = proxyConfig.isNeedAuth();
        timeout = proxyConfig.getTimeout();
    }


    @Override
    public void returnProxy(Proxy proxy, us.codecraft.webmagic.Page page, Task task) {
        return;
    }

    public boolean recovering() {
        // 判断是否关闭了代理且是否开启了代理自动恢复
        if(!enabled.get() && properties.getReptile().getProxyConfig().isAutoRecovery()){
            long spendTime = System.currentTimeMillis() - recoveryTime;
            if(spendTime / 1000 >= (long) timeout * mulit * failNum.get()){
                log.info("proxy mode recovering...");
                /**
                 * 三种情况
                 * 1. 刚进来，就有其他线程已经cas了，所以双重检测一下，成功直接跳过cas
                 * 2. 当前线程去cas了，且cas成功，直接就进行下一步去取proxy的流程
                 * 3. cas失败直接返回空，防止上下逻辑不一致的情况
                 */
                if(enabled.get()){
                    log.info("proxy mode recovered success...");
                }else if(enabled.compareAndSet(false,true)){
                    log.info("proxy mode recovered success...");
                }else{
                    // 恢复失败后，直接返回null,防止上下不一致的情况
                    log.info("proxy mode recovered failed...");
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public Proxy tryAcquireProxy(){
        // 尝试获取proxy
        log.info("it's try to acquire a proxy");
        if(enabled.get()){
            while (true){
                // 判断代理模式是否开启，如果在超时时compareAndSet失败后也没有关系，会在下一次循环时正确退出
                if(!enabled.get()){
                    break;
                }
                // 判断是否出书画从指定接口初始化proxy数组
                if(proxies==null && proxies.size()==0) {
                    initProxies();
                }
                // 获得一个可以使用的代理
                Proxy proxy = null;
                // 记录当前时间，用于超时判断
                long startTime = System.currentTimeMillis();
                int index = incrForLoop();
                proxy = proxies.get(index);
                // 检测代理是否可以使用,最多一秒的延迟
                if(checkProxyIp(proxy)){
                    return proxy;
                }else{
                    // 删除超时的proxy
                    proxies.remove(index);
                    // 超时处理
                    if(isTimeout(startTime)){
                        break;
                    }
                }
            }
        }
        return null;
    }


    @Override
    public Proxy getProxy(Task task) {
        // 如果设置了autoRecovery则将尝试恢复代理模式
        if(!recovering()) {
            return null;
        }
        return tryAcquireProxy();
    }

    /**
     * 处理超时的逻辑
     */
    public boolean isTimeout(long startTime){
        // 移除当前集合中的代理
        long spendTime = System.currentTimeMillis() - startTime;
        // 如果在这里耗费了超过设置的时间，则将自动退出代理模式
        if(spendTime / 1000 > timeout){
            // 退出代理模式
            if(quitProxyMode()){
                return true;
            }
        }
        return false;
    }

    public boolean quitProxyMode(){
        if(enabled.compareAndSet(true,false)){
            // 记录自动恢复时间
            recoveryTime = System.currentTimeMillis();
            // 记录失败次数
            int num = failNum.incrementAndGet();
            log.info("获取代理第{}次超时,下次将在{}秒后恢复代理模式",num,timeout * mulit * num);
            return true;
        }
        return false;
    }

    public  void initProxies() {
        Result<Page<IpAgentModel>> result = restTemplate.getForObject(address, Result.class);

        // 如果值是正确的
        if (result != null && result.getSuccess()) {
            List<IpAgentModel> records = result.getData().getRecords();

            List<Proxy> collectProxies = records.stream().map((item) -> new Proxy(item.getIp(), item.getPort())).collect(Collectors.toList());

            proxies.addAll(collectProxies);
        }
    }

    private int incrForLoop() {
        if(proxies!=null){
            proxies = new CopyOnWriteArrayList<>();
        }
        int p = pointer.incrementAndGet();
        int size = proxies.size();
        if (p < size) {
            return p;
        }
        while (!pointer.compareAndSet(p, p % size)) {
            p = pointer.get();
        }
        return p % size;
    }
}
