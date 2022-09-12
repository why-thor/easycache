package org.galileo.easycache.core.core;


import org.galileo.easycache.common.CacheBuilder;
import org.galileo.easycache.common.CacheProxy;
import org.apache.commons.lang3.StringUtils;
import org.galileo.easycache.core.core.config.RemoteConfig;
import org.galileo.easycache.core.filter.AbsInvokeFilter;
import org.galileo.easycache.core.filter.HeadFilter;
import org.galileo.easycache.core.filter.TailFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存缓存实例构建者
 * 
 * @param <T>
 */
public abstract class AbstractCacheBuilder<T extends AbstractCacheBuilder<T>> implements CacheBuilder, Cloneable {

    protected RemoteConfig remoteConfig;

    private final List<AbsInvokeFilter> cacheFilterList = new ArrayList<>();
    /**
     * 默第一个filter
     */
    protected AbsInvokeFilter first;
    /**
     * 最后一个filter
     */
    protected AbsInvokeFilter last;

    protected AbstractCacheBuilder(RemoteConfig remoteConfig) {
        this.remoteConfig = remoteConfig;
    }

    public T addFilters(AbsInvokeFilter... filters) {
        for (AbsInvokeFilter filter : filters) {
            if (StringUtils.isEmpty(filter.getForNamespace()) && remoteConfig != null) {
                filter.setForNamespace(remoteConfig.getNamespace());
            }
            cacheFilterList.add(filter);
        }
        return (T) this;
    }

    protected void addHeadFilter(CacheProxy cache) {
        this.first = new HeadFilter(cache);
        if (remoteConfig != null) {
            first.setForNamespace(remoteConfig.getNamespace());
        }
    }

    protected void addTailFilter(CacheProxy cache) {
        this.last = new TailFilter(cache);
        if (remoteConfig != null) {
            last.setForNamespace(remoteConfig.getNamespace());
        }
    }

    /**
     * 创建缓存实例, 并添加缓存实例的filter
     * 
     * @param cache
     * @return
     */
    protected CacheProxy createCacheProxy(CacheProxy cache) {
        ((AbsCache) cache).init();
        AbsInvokeFilter firstFilter = null;
        if (first != null) {
            firstFilter = AbsInvokeFilter.buildFilter(cache, first);
        }

        preBuild();

        if (!this.cacheFilterList.isEmpty()) {
            for (AbsInvokeFilter filter : cacheFilterList) {
                EasyCacheManager.addFilter(filter);
                if (firstFilter == null) {
                    firstFilter = AbsInvokeFilter.buildFilter(cache, filter);
                } else {
                    firstFilter = AbsInvokeFilter.addFilterLast(firstFilter, filter);
                }
            }
        }

        if (last != null) {
            if (firstFilter != null) {
                AbsInvokeFilter.addFilterLast(firstFilter, last);
            } else {
                firstFilter = AbsInvokeFilter.buildFilter(cache, last);
            }
        }

        CacheProxyFactory cacheProxyFactory = new CacheProxyFactory(firstFilter);
        cacheFilterList.clear();
        return cacheProxyFactory.createCacheProxy(cache);
    }

    protected void preBuild() {

    }

}
