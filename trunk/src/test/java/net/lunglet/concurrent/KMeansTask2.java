package net.lunglet.concurrent;

import java.util.concurrent.Callable;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;

public interface KMeansTask2 extends Callable<FloatVector<?>> {
    FloatMatrix<?, ?> getCentroids();

    FloatMatrix<?, ?> getData();
}
