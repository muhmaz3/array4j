package net.lunglet.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Ignore;
import org.junit.Test;

import com.googlecode.array4j.DenseFloatMatrix;
import com.googlecode.array4j.DenseFloatVector;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;

public final class KMeansTest {
    @Ignore
    public void test() throws Exception {
//        BrokerService broker = new BrokerService();
//        broker.setManagementContext(new ManagementContext(ManagementFactory.getPlatformMBeanServer()));
//        broker.setPersistent(false);
//        broker.setUseLocalHostBrokerName(true);
//        broker.setUseJmx(true);
//        broker.setUseShutdownHook(false);
//        broker.addConnector("tcp://localhost:61616");
//        broker.start();
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

        final Connection connection = connectionFactory.createConnection();
        Queue workQueue = new ActiveMQQueue("work");
        Queue resultQueue = new ActiveMQQueue("results");

        int nThreads = 1;
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(nThreads);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, queue);
        executor.prestartAllCoreThreads();
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        JMSWorker worker = new JMSWorker(executor, connection, workQueue, resultQueue);

        connection.start();

        // KMeans over JMS code starts here
        JMSCompletionService<FloatVector<?>> completionService =
            new JMSCompletionService<FloatVector<?>>(connection, workQueue, resultQueue);
        KMeansTaskFactory<String> taskFactory = new KMeansTaskFactory<String>() {
            @Override
            public KMeansTask2 createTask(String data, FloatMatrix<?, ?> centroids) {
                return new KMeansJMSTask(data, centroids);
            }
        };
        KMeans2<String> kmeans = new KMeans2<String>(completionService, taskFactory);
        DenseFloatMatrix initialCentroids = new DenseFloatMatrix(5, 10);
        String[] data = new String[]{"file1", "file2", "file3"};
        FloatMatrix<?, ?> centroids = kmeans.train(initialCentroids, data);
        // KMeans over JMS code ends here

        System.out.println("CLEANUP!!!!!!!!!!!!!!!!!!!!!");
        worker.close();
        executor.awaitTermination(0L, TimeUnit.MILLISECONDS);
        connection.close();

//        broker.stop();
    }

    @Test
    public void testBasic() throws InterruptedException, ExecutionException {
        KMeans2<FloatMatrix<?, ?>> kmeans = KMeans2.create();
        DenseFloatMatrix data = new DenseFloatMatrix(5, 100);
        // TODO let kmeans choose the centroids using the kmeans++ method
        DenseFloatMatrix initialCentroids = new DenseFloatMatrix(5, 10);
        FloatMatrix<?, ?> centroids = kmeans.train(initialCentroids, data);
    }
}
