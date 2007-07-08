package net.lunglet.concurrent;

import com.googlecode.array4j.FloatMatrix;

public interface KMeansTaskFactory<T> {
    KMeansTask2 createTask(T data, FloatMatrix<?, ?> centroids);
}
