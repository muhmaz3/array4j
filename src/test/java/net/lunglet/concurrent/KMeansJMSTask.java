package net.lunglet.concurrent;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;

public final class KMeansJMSTask extends CacheTask<FloatVector<?>> implements KMeansTask2 {
    private static final String CACHE_NAME = String.format("%s.CACHE", KMeansTask2.class.getName());

    private static final long serialVersionUID = 1L;

    private final FloatMatrix<?, ?> centroids;

    private final String data;

    public KMeansJMSTask(final String data, final FloatMatrix<?, ?> centroids) {
        super(data, CACHE_NAME);
        this.data = data;
        this.centroids = centroids;
    }

    @Override
    public FloatVector<?> call() throws Exception {
        return KMeans2.doTask(this);
    }

    @Override
    public FloatMatrix<?, ?> getCentroids() {
        return centroids;
    }

    @Override
    public FloatMatrix<?, ?> getData() {
        return getFromCache();
    }
}
