package net.lunglet.concurrent;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;

public final class KMeans2<M extends FloatMatrix<M, ?>, T> {
    public static <M extends FloatMatrix<M, ?>> KMeans2<M, M> create() {
        KMeansTaskFactory<M> taskFactory = new KMeansTaskFactory<M>() {
            @Override
            public KMeansTask2 createTask(final M data, final FloatMatrix<?, ?> centroids) {
                return new KMeansTask2() {
                    @Override
                    public FloatVector<?> call() throws Exception {
                        return KMeans2.doTask(this);
                    }

                    @Override
                    public FloatMatrix<?, ?> getCentroids() {
                        return centroids;
                    }

                    @Override
                    public M getData() {
                        return data;
                    }
                };
            }
        };
        CompletionService<FloatVector<?>> completionService =
            new ExecutorCompletionService<FloatVector<?>>(Executors.newFixedThreadPool(4));
        return new KMeans2<M, M>(completionService, taskFactory);
    }

    public static FloatVector<?> doTask(final KMeansTask2 task) {
        FloatMatrix<?, ?> data = task.getData();
        FloatMatrix<?, ?> centroids = task.getCentroids();
        // TODO create a matrix from centroids or data with dimensions centroids x data
        FloatMatrix<?, ?> distances = null;
        int i = 0;
        for (FloatVector<?> centroid : centroids.columnsIterator()) {
            int j = 0;
            for (FloatVector<?> x : data.columnsIterator()) {
//                distances.set(i++, j++, 0.0f);
            }
        }
        return null;
    }

    private CompletionService<FloatVector<?>> cs;

    private KMeansTaskFactory<T> taskFactory;

    public KMeans2(final CompletionService<FloatVector<?>> cs, final KMeansTaskFactory<T> taskFactory) {
        this.cs = cs;
        this.taskFactory = taskFactory;
    }

    public M train(final M initialCentroids, final T... data) throws InterruptedException, ExecutionException {
        // TODO copy the centroids so that we don't modify our argument
        M centroids = initialCentroids;
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < data.length; i++) {
//                System.out.println(j + " " + i + " submitting " + data[i]);
                KMeansTask2 task = taskFactory.createTask(data[i], centroids);
                cs.submit(task);
            }
            for (int i = 0; i < data.length; i++) {
                FloatVector<?> distances = cs.take().get();
//                System.out.println(j + " " + i + " " + distances);
            }
        }
        return centroids;
    }
}
