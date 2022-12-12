package com.romankliuiev.socialnetwork.service;

import com.romankliuiev.socialnetwork.data.InactiveToken;
import com.romankliuiev.socialnetwork.repo.InactiveTokenRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "inactiveTokens")

public class InactiveTokenService {

    private final InactiveTokenRepo repo;
    private final Cache cache;

    public InactiveTokenService(InactiveTokenRepo repo, CacheManager cacheManager) {
        this.repo = repo;
        this.cache = cacheManager.getCache("inactiveTokens");
    }

    @PostConstruct
    public void init() {
        ScheduledExecutorService threadPool = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

        repo.findAll().forEach(token -> {
            long ttl = token.getExpiration().toInstant().toEpochMilli() - OffsetDateTime.now().toInstant().toEpochMilli();

            cache.put(token.getToken(), token);
            threadPool.schedule(() -> deleteToken(token.getToken()), ttl, TimeUnit.MILLISECONDS);
        });

        threadPool.shutdown();
    }

    @Transactional
    @CachePut(key = "#result.token")
    public InactiveToken createInactiveToken(InactiveToken token) {
        ScheduledExecutorService threadPool = new ScheduledThreadPoolExecutor(1);
        long ttl = token.getExpiration().toInstant().toEpochMilli() - OffsetDateTime.now().toInstant().toEpochMilli();

        repo.save(token);
        threadPool.schedule(() -> deleteToken(token.getToken()), ttl, TimeUnit.MILLISECONDS);

        threadPool.shutdown();
        return token;
    }

    @Transactional
    @CacheEvict(key = "#token")
    public void deleteToken(String token) {
        repo.deleteById(token);
    }

    public InactiveToken getInactiveTokenFromCache(String token) {
        return cache.get(token, InactiveToken.class);
    }
}
