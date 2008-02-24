package net.lunglet.cluster;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class CompletionServiceFactory {
    private static final ExecutorService THREAD_POOL;

    static {
        THREAD_POOL = Executors.newFixedThreadPool(4);
    }

    public static <T> CompletionService<T> createCompletionService() {
        return new ExecutorCompletionService<T>(THREAD_POOL);
    }

    public static void shutdown() {
        THREAD_POOL.shutdown();
    }

    private CompletionServiceFactory() {
    }
}
