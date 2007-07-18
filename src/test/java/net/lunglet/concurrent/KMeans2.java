package net.lunglet.concurrent;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;

public final class KMeans2<T> {
    public static KMeans2<FloatMatrix<?, ?>> create() {
        KMeansTaskFactory<FloatMatrix<?, ?>> taskFactory = new KMeansTaskFactory<FloatMatrix<?, ?>>() {
            @Override
            public KMeansTask2 createTask(final FloatMatrix<?, ?> data, final FloatMatrix<?, ?> centroids) {
                return new KMeansTask2() {
                    @Override
                    public List<FloatVector<?>> call() throws Exception {
                        return KMeans2.doTask(this);
                    }

                    @Override
                    public FloatMatrix<?, ?> getCentroids() {
                        return centroids;
                    }

                    @Override
                    public Iterable<? extends FloatVector<?>> getData() {
                        return data.columnsIterator();
                    }
                };
            }
        };
        CompletionService<List<FloatVector<?>>> completionService =
            new ExecutorCompletionService<List<FloatVector<?>>>(Executors.newFixedThreadPool(4));
        return new KMeans2<FloatMatrix<?, ?>>(completionService, taskFactory);
    }

    public static List<FloatVector<?>> doTask(final KMeansTask2 task) {
        // TODO decorate this iterator with something that checks that it
        // returns the same number of elements each time
        Iterable<? extends FloatVector<?>> data = task.getData();
        FloatMatrix<?, ?> centroids = task.getCentroids();
        FloatMatrix<?, ?> newCentroids = null;
        long[] counts = new long[newCentroids.columns()];
        float totalDistortion = 0.0f;
        for (FloatVector<?> x : data) {
            int nearestIndex = -1;
            float nearestDistance = Float.POSITIVE_INFINITY;
            int i = 0;
            for (FloatVector<?> centroid : centroids.columnsIterator()) {
                // TODO calculate distance between x and centroid
                float distance = 0.0f;
                if (distance < nearestDistance) {
                    nearestIndex = i;
                }
                i++;
            }
            counts[nearestIndex]++;
            FloatVector<?> nearestNewCentroid = newCentroids.column(nearestIndex);
            FloatVector<?> delta = x.minus(nearestNewCentroid);
            delta.timesEquals(1.0f / counts[nearestIndex]);
            nearestNewCentroid.plusEquals(delta);
            totalDistortion += nearestDistance;
        }
        // TODO return newCentroids, counts, totalDistortion
        return null;
    }

    private CompletionService<List<FloatVector<?>>> cs;

    private KMeansTaskFactory<T> taskFactory;

    public KMeans2(final CompletionService<List<FloatVector<?>>> cs, final KMeansTaskFactory<T> taskFactory) {
        this.cs = cs;
        this.taskFactory = taskFactory;
    }

    public FloatMatrix<?, ?> train(final FloatMatrix<?, ?> initialCentroids, final T... data)
            throws InterruptedException, ExecutionException {
        // TODO copy the centroids so that we don't modify our argument
        FloatMatrix<?, ?> centroids = initialCentroids;
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < data.length; i++) {
                KMeansTask2 task = taskFactory.createTask(data[i], centroids);
                cs.submit(task);
            }
            for (int i = 0; i < data.length; i++) {
                List<FloatVector<?>> distances = cs.take().get();
            }
        }
        return centroids;
    }
}
