package net.lunglet.concurrent;

import net.sf.ehcache.Ehcache;

public abstract class CacheTask<V> extends JMSXGroupTask<V> {
    private final String cacheName;

    public CacheTask(final String id, final String cacheName) {
        super(id);
        this.cacheName = cacheName;
    }

    @Override
    public abstract V call() throws Exception;

    protected final Ehcache getCache() {
        return TaskCacheManager.getHadoopCache(cacheName);
    }
}
