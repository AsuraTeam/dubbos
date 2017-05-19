/*
 * Copyright 1999-2012 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.cache.support.jcache;

import com.alibaba.dubbo.common.URL;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

/**
 * JCache
 *
 * @author william.liangf
 */
public class JCache implements com.alibaba.dubbo.cache.Cache {

    private final Cache<Object, Object> store;

    public JCache(URL url) {
        String type = url.getParameter("jcache");
        CachingProvider cachingProvider = type == null || type.length() == 0 ? Caching.getCachingProvider() : Caching.getCachingProvider(type);
        CacheManager cacheManager = cachingProvider.getCacheManager();
        Cache<Object, Object> cache = cacheManager.getCache(url.getServiceKey());
        this.store = cache;
    }

    public void put(Object key, Object value) {
        store.put(key, value);
    }

    public Object get(Object key) {
        return store.get(key);
    }

}
