package org.galileo.easycache.springboot.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.galileo.easycache.anno.CacheRemove;
import org.galileo.easycache.anno.CacheRemoveAll;
import org.galileo.easycache.anno.CacheUpdate;
import org.galileo.easycache.anno.Cached;
import org.galileo.easycache.common.constants.CacheConstants;
import org.galileo.easycache.core.core.config.EasyCacheConfig;
import org.galileo.easycache.springboot.aop.EasyCacheAdvisor;
import org.galileo.easycache.springboot.aop.EasyCacheInterceptor;
import org.galileo.easycache.springboot.processor.EasyCacheConfigParentProcessor;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@ComponentScan(EasyCacheAutoConfiguration.BASE_PACKAGE)
@EnableAspectJAutoProxy
@Configuration
@ConditionalOnProperty(value = "easycache.enabled", havingValue = "true", matchIfMissing = false)
public class EasyCacheAutoConfiguration {

    public static final String BASE_PACKAGE = "org.galileo.easycache";
    public static final String CACHE_ADVISOR_BEAN_NAME = "easyCacheAdvisor";
    public static final String CACHE_TRANSACTION_ADVISOR_BEAN_NAME = "easyCacheTransactionAdvisor";
    public static final String CACHE_INTERCEPTOR_BEAN_NAME = "easyCacheInterceptor";

    @Bean(CacheConstants.CACHE_CONFIG)
    @ConfigurationProperties("easycache")
    public EasyCacheConfig cacheConfig() {
        return new EasyCacheConfig();
    }

    @Bean
    public EasyCacheConfigParentProcessor easyCacheConfigProcessor() {
        return new EasyCacheConfigParentProcessor();
    }

    @Bean(CACHE_INTERCEPTOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public MethodInterceptor easyCacheInterceptor() {
        return new EasyCacheInterceptor();
    }

    @Bean(CACHE_ADVISOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Order(Ordered.LOWEST_PRECEDENCE - 10)
    public PointcutAdvisor cachedAdvisor(@Qualifier(CACHE_INTERCEPTOR_BEAN_NAME) MethodInterceptor cacheInterceptor) {
        EasyCacheAdvisor advisor = new EasyCacheAdvisor();
        advisor.setBasePackages(new String[]{BASE_PACKAGE});
        advisor.setScanAnnotations(Cached.class);
        advisor.setAdvice(cacheInterceptor);
        return advisor;
    }

    @Bean(CACHE_TRANSACTION_ADVISOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public PointcutAdvisor cachedTransactionAdvisor(@Qualifier(CACHE_INTERCEPTOR_BEAN_NAME) MethodInterceptor cacheInterceptor) {
        EasyCacheAdvisor advisor = new EasyCacheAdvisor();
        advisor.setBasePackages(new String[]{BASE_PACKAGE});
        advisor.setScanAnnotations(CacheRemove.class, CacheUpdate.class, CacheRemoveAll.class);
        advisor.setAdvice(cacheInterceptor);
        return advisor;
    }
}
