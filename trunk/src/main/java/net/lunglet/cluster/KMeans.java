package net.lunglet.cluster;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseUtils;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;

public final class KMeans<T> {
    public interface KMeansTask extends Callable<KMeansTaskResult> {
        FloatMatrix getCentroids();

        Iterable<? extends FloatVector> getData();
    }

    public interface KMeansTaskFactory<T> {
        KMeansTask createTask(T data, FloatMatrix centroids);

        KMeansTask createTask2(T data, FloatMatrix centroids);
    }

    public static final class KMeansTaskResult {
        private final FloatMatrix centroids;

        private final long[] n;

        private final double totalDistortion;

        public KMeansTaskResult(final FloatMatrix centroids, final long[] n, final double totalDistortion) {
            this.centroids = centroids;
            this.n = n;
            this.totalDistortion = totalDistortion;
        }
    }

    public static KMeans<FloatMatrix> create() {
        KMeansTaskFactory<FloatMatrix> taskFactory = new KMeansTaskFactory<FloatMatrix>() {
            @Override
            public KMeansTask createTask(final FloatMatrix data, final FloatMatrix centroids) {
                return new KMeansTask() {
                    @Override
                    public KMeansTaskResult call() throws Exception {
                        return KMeans.doTask(this);
                    }

                    @Override
                    public FloatMatrix getCentroids() {
                        return centroids;
                    }

                    @Override
                    public Iterable<? extends FloatVector> getData() {
                        return data.columnsIterator();
                    }
                };
            }

            @Override
            public KMeansTask createTask2(final FloatMatrix data, final FloatMatrix centroids) {
                return new KMeansTask() {
                    @Override
                    public KMeansTaskResult call() throws Exception {
                        return KMeans.doTask2(this);
                    }

                    @Override
                    public FloatMatrix getCentroids() {
                        return centroids;
                    }

                    @Override
                    public Iterable<? extends FloatVector> getData() {
                        return data.columnsIterator();
                    }
                };
            }
        };
        CompletionService<KMeansTaskResult> completionService = CompletionServiceFactory.createCompletionService();
        return new KMeans<FloatMatrix>(completionService, taskFactory);
    }

    public static KMeansTaskResult doTask(final KMeansTask task) {
        // TODO decorate this iterator with something that checks that it
        // returns the same number of elements each time
        Iterable<? extends FloatVector> data = task.getData();
        FloatMatrix centroids = task.getCentroids();
        FloatDenseMatrix newCentroids = FloatMatrixUtils.zerosLike(centroids);
        long[] n = new long[newCentroids.columns()];
        double totalDistortion = 0.0;
        for (FloatVector x : data) {
            int nearestIndex = -1;
            double nearestDistance = Double.POSITIVE_INFINITY;
            int i = 0;
            for (FloatVector centroid : centroids.columnsIterator()) {
                double distance = FloatMatrixUtils.euclideanDistance(x, centroid);
                if (distance < nearestDistance) {
                    nearestIndex = i;
                    nearestDistance = distance;
                }
                i++;
            }
            n[nearestIndex]++;
            FloatVector nearestNewCentroid = newCentroids.column(nearestIndex);
//            FloatVector delta = x.minus(nearestNewCentroid);
//            delta.timesEquals(1.0f / n[nearestIndex]);
//            nearestNewCentroid.plusEquals(delta);
            totalDistortion += nearestDistance;
        }
        return new KMeansTaskResult(newCentroids, n, totalDistortion);
    }

    public static KMeansTaskResult doTask2(final KMeansTask task) {
        Iterable<? extends FloatVector> data = task.getData();
        FloatMatrix centroids = task.getCentroids();
        FloatVector furthestElement = null;
        double maxDistance = Double.NEGATIVE_INFINITY;
        for (FloatVector x : data) {
            if (centroids != null) {
                // distance between data and nearest centroid
                double minDistance = Double.POSITIVE_INFINITY;
                for (FloatVector centroid : centroids.columnsIterator()) {
                    double distance = FloatMatrixUtils.euclideanDistance(x, centroid);
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                }
                // check if data is further from nearest centroid than any
                // previous data and centroid
                if (minDistance > maxDistance) {
                    maxDistance = minDistance;
                    furthestElement = x;
                }
            } else {
                furthestElement = x;
                maxDistance = 0.0;
                break;
            }
        }
        return new KMeansTaskResult(furthestElement, null, maxDistance);
    }

    private final CompletionService<KMeansTaskResult> cs;

    private final KMeansTaskFactory<T> taskFactory;

    public KMeans(final CompletionService<KMeansTaskResult> cs, final KMeansTaskFactory<T> taskFactory) {
        this.cs = cs;
        this.taskFactory = taskFactory;
    }

    /**
     * Choose centroids according to a variant of the k-means++ algorithm.
     */
    public FloatDenseMatrix chooseCentroids(final int maxCentroids, final T... data) throws InterruptedException,
            ExecutionException {
        FloatDenseMatrix centroids = null;
        for (int iter = 0; iter < maxCentroids; iter++) {
            for (int i = 0; i < data.length; i++) {
                FloatDenseMatrix currentCentroids = null;
                if (centroids != null) {
                    currentCentroids = FloatDenseUtils.subMatrixColumns(centroids, 0, iter);
                }
                KMeansTask task = taskFactory.createTask2(data[i], currentCentroids);
                cs.submit(task);
            }
            FloatVector newCentroid = null;
            double maxDistance = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < data.length; i++) {
                KMeansTaskResult result = cs.take().get();
                if (result.totalDistortion > maxDistance) {
                    newCentroid = result.centroids.column(0);
                }
            }
            if (newCentroid == null) {
                throw new RuntimeException("no new centroid found");
            }
            if (centroids == null) {
//                centroids = new FloatDenseMatrix(newCentroid.rows(), maxCentroids);
                centroids = null;
            }
            centroids.setColumn(iter, newCentroid);
        }
        return centroids;
    }

    private double train(final FloatMatrix centroids, final T... data) throws InterruptedException, ExecutionException {
//        FloatDenseMatrix centroidsCopy = new FloatDenseMatrix(centroids);
        FloatDenseMatrix centroidsCopy = null;
        for (int i = 0; i < data.length; i++) {
            // TODO task should probably copy its arguments, not depend on who
            // creates it to do so
            KMeansTask task = taskFactory.createTask(data[i], centroidsCopy);
            cs.submit(task);
        }
        FloatMatrixUtils.fill(centroids, 0.0f);
        long[] n = new long[centroids.columns()];
        double totalDistortion = 0.0;
        for (int i = 0; i < data.length; i++) {
            KMeansTaskResult result = cs.take().get();
            for (int j = 0; j < centroids.columns(); j++) {
                FloatVector centroid = centroids.column(j);
                if (result.n[j] == 0) {
                    // If this centroid wasn't the nearest centroid to any
                    // of the data or there was no data, continue without
                    // doing anything more.
                    continue;
                }
                n[j] += result.n[j];
//                FloatVector delta = result.centroids.column(j).minus(centroid);
//                delta.timesEquals(result.n[j] / (float) n[j]);
//                centroid.plusEquals(delta);
            }
            totalDistortion += result.totalDistortion;
        }

        // check that all clusters still contain some data
        for (int i = 0; i < n.length; i++) {
            if (n[i] == 0) {
                throw new RuntimeException("cluster " + i + " became emtpy");
            }
        }

        return totalDistortion;
    }

    public FloatDenseMatrix train(final int iterations, final FloatMatrix initialCentroids, final T... data)
            throws InterruptedException, ExecutionException {
//        FloatDenseMatrix centroids = new FloatDenseMatrix(initialCentroids);
        FloatDenseMatrix centroids = null;
        FloatDenseMatrix bestCentroids = null;
        double bestDistortion = Double.POSITIVE_INFINITY;
        for (int iter = 0; iter < iterations; iter++) {
//            FloatDenseMatrix oldCentroids = new FloatDenseMatrix(centroids);
            FloatDenseMatrix oldCentroids = null;
            // this returns the distortion of the old centroids
            double distortion = train(centroids, data);
            if (distortion < bestDistortion) {
                bestCentroids = oldCentroids;
                bestDistortion = distortion;
            }
        }
        return bestCentroids;
    }
}
