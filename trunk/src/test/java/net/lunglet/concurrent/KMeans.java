package net.lunglet.concurrent;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;

public final class KMeans {
    // TODO abstractkmeanstask, that operates on a iterable of vectors or something

    // KMeans#train takes some generic thing to describe "data"... kmeans can be
    // generified on this thing

    // KMeans is constructed with a task factory that makes tasks that
    // understand how to turn that data description into what the abstract task
    // needs

    // inject task factory and completion service at construction time
    // e.g. will inject KMeansJMSTaskFactory and JMScompletionservice


    // JMSKMeansModule
    // bind completionservice<v> aan jmscompletionservice<v>
    // bind KMeansTaskFactory aan ... iets

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
