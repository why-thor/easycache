package org.galileo.easycache.core.core.config;

import org.galileo.easycache.common.constants.CacheConstants;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存实例配置
 * 用于创建一个缓存实例, 也就是 Cache 对象
 */
public class NamespaceConfig extends InheritableConfig<EasyCacheConfig> {

    /**
     * 当前namespace名
     */
    private String namespace = CacheConstants.DEFAULT_NAMESPACE;

    /**
     * cacheName配置
     */
    private Map<String, CacheNameConfig> cacheName = new HashMap<>();

    /**
     * 缓存组件类型
     */
    private String type;

    private int database = 0;

    private String url;

    private String host;

    private String password;

    private int port = 6379;

    private boolean ssl;

    private Duration timeout;

    private String clientName;

    private Sentinel sentinel;

    private Cluster cluster;

    private final Jedis jedis = new Jedis();

    private final Lettuce lettuce = new Lettuce();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Map<String, CacheNameConfig> getCacheName() {
        return cacheName;
    }

    public void setCacheName(Map<String, CacheNameConfig> cacheName) {
        this.cacheName = cacheName;
    }

    public int getDatabase() {
        return this.database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return this.ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Duration getTimeout() {
        return this.timeout;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Sentinel getSentinel() {
        return this.sentinel;
    }

    public void setSentinel(Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Jedis getJedis() {
        return this.jedis;
    }

    public Lettuce getLettuce() {
        return this.lettuce;
    }

    public static class Pool {

        private int maxIdle = 8;

        private int minIdle = 0;

        private int maxActive = 8;

        private Duration maxWait = Duration.ofMillis(-1);

        private Duration timeBetweenEvictionRuns;

        public int getMaxIdle() {
            return this.maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return this.minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxActive() {
            return this.maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public Duration getMaxWait() {
            return this.maxWait;
        }

        public void setMaxWait(Duration maxWait) {
            this.maxWait = maxWait;
        }

        public Duration getTimeBetweenEvictionRuns() {
            return this.timeBetweenEvictionRuns;
        }

        public void setTimeBetweenEvictionRuns(Duration timeBetweenEvictionRuns) {
            this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
        }

    }

    public static class Cluster {

        private List<String> nodes;

        private Integer maxRedirects;

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

        public Integer getMaxRedirects() {
            return this.maxRedirects;
        }

        public void setMaxRedirects(Integer maxRedirects) {
            this.maxRedirects = maxRedirects;
        }

    }

    public static class Sentinel {

        private String master;

        private List<String> nodes;

        private String password;

        public String getMaster() {
            return this.master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

    public static class Jedis {

        private Pool pool;

        public Pool getPool() {
            return this.pool;
        }

        public void setPool(Pool pool) {
            this.pool = pool;
        }

    }

    public static class Lettuce {

        private Duration shutdownTimeout = Duration.ofMillis(100);

        private Pool pool;

        private final Cluster cluster = new Cluster();

        public Duration getShutdownTimeout() {
            return this.shutdownTimeout;
        }

        public void setShutdownTimeout(Duration shutdownTimeout) {
            this.shutdownTimeout = shutdownTimeout;
        }

        public Pool getPool() {
            return this.pool;
        }

        public void setPool(Pool pool) {
            this.pool = pool;
        }

        public Cluster getCluster() {
            return this.cluster;
        }

        public static class Cluster {

            private final Refresh refresh = new Refresh();

            public Refresh getRefresh() {
                return this.refresh;
            }

            public static class Refresh {

                /**
                 * Cluster topology refresh period.
                 */
                private Duration period;

                /**
                 * Whether adaptive topology refreshing using all available refresh
                 * triggers should be used.
                 */
                private boolean adaptive;

                public Duration getPeriod() {
                    return this.period;
                }

                public void setPeriod(Duration period) {
                    this.period = period;
                }

                public boolean isAdaptive() {
                    return this.adaptive;
                }

                public void setAdaptive(boolean adaptive) {
                    this.adaptive = adaptive;
                }

            }

        }

    }
}
