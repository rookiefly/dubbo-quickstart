package com.rookiefly.quickstart.dubbo.ratelimiter;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

public class RateLimiter {

    private Logger logger = LoggerFactory.getLogger(RateLimiter.class);

    public static final String RATE_LIMITER_KEY_FORMAT = "rate_limiter:%s:%s";

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisScript<Boolean> rateLimiterClientLua;

    private final String appName;

    private final String resourceName;

    private final Integer maxQps;

    private final Integer timeWindow;

    public RateLimiter(StringRedisTemplate stringRedisTemplate,
                       RedisScript<Boolean> rateLimiterClientLua,
                       String appName,
                       String resourceName, Integer maxQps, Integer timeWindow) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.rateLimiterClientLua = rateLimiterClientLua;
        this.appName = appName;
        this.resourceName = resourceName;
        this.maxQps = maxQps;
        this.timeWindow = timeWindow;
    }

    /**
     * 获取令牌，访问redis异常算做成功
     * 默认的permits为1
     *
     * @return
     */
    public boolean acquire() {
        Token token = acquireToken(appName, resourceName);
        return token.isPass() || token.isAccessRedisFail();
    }

    /**
     * 获取{@link Token}
     * 默认的permits为1
     *
     * @param appName
     * @param resourceName
     * @return
     */
    public Token acquireToken(String appName, String resourceName) {
        return acquireToken(appName, resourceName, maxQps, timeWindow);
    }

    /**
     * 获取{@link Token}
     *
     * @param appName
     * @param resourceName
     * @return
     */
    public Token acquireToken(String appName, String resourceName, Integer maxQps, Integer timeWindow) {
        Token token;
        try {
            boolean acquire = stringRedisTemplate.execute(rateLimiterClientLua, ImmutableList.of(getKey(appName, resourceName)), maxQps.toString(), timeWindow.toString());
            token = acquire ? Token.PASS : Token.FAIL;
        } catch (Exception e) {
            logger.error("get rate limit token from redis error, appName={}, resourceName={}, exception={}", appName, resourceName, e);
            token = Token.ACCESS_REDIS_ERROR;
        }
        return token;
    }

    private String getKey(String appName, String resourceName) {
        return String.format(RATE_LIMITER_KEY_FORMAT, appName, resourceName);
    }
}