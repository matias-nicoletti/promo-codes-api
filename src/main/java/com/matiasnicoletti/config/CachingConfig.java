package com.matiasnicoletti.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfig {

	public static final String PROMO_CODES_CACHE = "promo-codes";

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager(PROMO_CODES_CACHE);
	}
}