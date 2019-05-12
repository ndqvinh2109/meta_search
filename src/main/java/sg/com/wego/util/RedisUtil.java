package sg.com.wego.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class RedisUtil<T> {

    private RedisTemplate<String, T> redisTemplate;
    private ListOperations<String, T> listOperation;

    @Autowired
    public RedisUtil(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        this.listOperation = redisTemplate.opsForList();

    }

    public void setExpire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public void putList(String key, T data) {
        listOperation.leftPush(key, data);
    }

    public void putListAll(String key, List<T> list) {
        listOperation.leftPushAll(key, list);
    }

    public List<T> getListRange(String key, long i, long j) {
        return listOperation.range(key, i, j);
    }

    public long getListSize(String key) {
        return listOperation.size(key);
    }

}
