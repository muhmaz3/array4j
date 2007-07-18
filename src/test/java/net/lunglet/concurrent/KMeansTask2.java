package net.lunglet.concurrent;

import java.util.List;
import java.util.concurrent.Callable;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;

public interface KMeansTask2 extends Callable<List<FloatVector<?>>> {
    FloatMatrix<?, ?> getCentroids();

    Iterable<? extends FloatVector<?>> getData();
}
