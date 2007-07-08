package net.lunglet.concurrent;

import net.sf.ehcache.Ehcache;

import com.googlecode.array4j.FloatVector;

public abstract class CacheTask<V> extends JMSXGroupTask<V> {
    private final String cacheName;

    private final String id;

    public CacheTask(final String id, final String cacheName) {
        super(id);
        this.id = id;
        this.cacheName = cacheName;
    }

    @Override
    public abstract V call() throws Exception;

    protected final Ehcache getCache() {
        return TaskCacheManager.getHadoopCache(cacheName);
    }

    protected final FloatVector<?> getFromCache() {
        return (FloatVector<?>) getCache().get((Object) id).getObjectValue();
    }
}
