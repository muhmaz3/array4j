package net.lunglet.concurrent;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;

public final class KMeans {
    public void train(final String[] data, final CompletionService<Object> cs) throws InterruptedException,
            ExecutionException {
        for (int j = 0; j < 10000; j++) {
            for (int i = 0; i < data.length; i++) {
                System.out.println(j + " " + i + " submitting " + data[i]);
                cs.submit(new KMeansTask(data[i]));
            }

            // TODO this loop assumes that all the tasks submitted to the
            // CompletionService will actually complete
            for (int i = 0; i < data.length; i++) {
                Object distances = cs.take().get();
                System.out.println(j + " " + i + " " + distances);
            }
        }
    }
}
