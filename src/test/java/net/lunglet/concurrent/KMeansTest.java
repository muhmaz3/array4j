package net.lunglet.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;

public final class KMeansTest {
    @Test
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

        JMSCompletionService<Object> completionService =
            new JMSCompletionService<Object>(connection, workQueue, resultQueue);

        KMeans kmeans = new KMeans();
        kmeans.train(new String[]{"file1", "file2", "file3"}, completionService);

        System.out.println("CLEANUP!!!!!!!!!!!!!!!!!!!!!");
        worker.close();
        executor.awaitTermination(0L, TimeUnit.MILLISECONDS);
        connection.close();

//        broker.stop();
    }
}
