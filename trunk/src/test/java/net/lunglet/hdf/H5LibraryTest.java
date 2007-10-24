package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.DenseFactory;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.sun.jna.ptr.IntByReference;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

// TODO move testHyperslabReadWrite and refactor to not use array4j classes

public final class H5LibraryTest {
    @Test
    public void testH5get_libversion() {
        IntByReference pmajnum = new IntByReference();
        IntByReference pminnum = new IntByReference();
        IntByReference prelnum = new IntByReference();
        int err = H5Library.INSTANCE.H5get_libversion(pmajnum, pminnum, prelnum);
        assertEquals(0, err);
        assertEquals(1, pmajnum.getValue());
        assertEquals(6, pminnum.getValue());
        assertEquals(6, prelnum.getValue());
    }

    @Test
    public void testHyperslabReadWrite() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5 = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();

        FloatDenseMatrix data = DenseFactory.createFloatMatrix(5, 6, Order.ROW, Storage.DIRECT);
        for (int j = 0; j < data.rows(); j++) {
            for (int i = 0; i < data.columns(); i++) {
                data.set(j, i, (float) i + j);
            }
        }
        Group root = h5.getRootGroup();
        DataType dtype = FloatType.IEEE_F32LE;
        DataSpace dataspace = new DataSpace(data.rows(), data.columns());
        String name = "FloatArray";
        DataSet dataset = root.createDataSet(name, dtype, dataspace);
        dataset.write(data.data(), FloatType.IEEE_F32LE);
        dataspace.close();
        dataset.close();

        DataSet dataset2 = root.openDataSet(name);
        DataSpace dataspace2 = dataset2.getSpace();
        long[] start = {1, 2};
        long[] count = {3, 4};
        dataspace2.selectHyperslab(SelectionOperator.SET, start, count);
        FloatDenseMatrix out = DenseFactory.createFloatMatrix(data.rows(), data.columns());
        dataset2.read(out.data(), dtype, DataSpace.ALL, dataspace2);
        dataspace2.close();
        dataset2.close();

        h5.close();
    }

    @Test
    public void testMultipleThreads() throws InterruptedException, ExecutionException {
        int nThreads = 4;
        int loops = 10 * nThreads;
        ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
        CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(threadPool);
        for (int i = 0; i < loops; i++) {
            completionService.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.gc();
                    FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
                    FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
                    String name = UUID.randomUUID().toString();
                    H5File h5 = new H5File(name, fcpl, fapl);
                    Group group = h5.getRootGroup().createGroup("/foo");
                    for (int j = 0; j < 4; j++) {
                        group.createAttribute("attr" + j, "value" + j);
                        System.gc();
                    }
                    DataSpace.createScalar();
                    DataSpace space = new DataSpace(4, 2, 1, 3);
                    space.selectElements(SelectionOperator.SET, new Point(0, 0, 0, 0));
                    return Boolean.TRUE;
                }
            });
        }
        threadPool.shutdown();
        threadPool.awaitTermination(0L, TimeUnit.MILLISECONDS);
        for (int i = 0; i < loops; i++) {
            Future<Boolean> future = completionService.take();
            assertTrue(future.get());
        }
    }
}
