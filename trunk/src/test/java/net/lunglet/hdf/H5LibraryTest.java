package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.sun.jna.ptr.IntByReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.hdf.H5Library.H5E_error_t;
import org.junit.Test;

// TODO move testHyperslabReadWrite and refactor to not use array4j classes

// TODO test thread error stacks

public final class H5LibraryTest {
    private static final class MTTask implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            System.gc();
            FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
            FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
            String name = UUID.randomUUID().toString();
            H5File h5 = new H5File(name, fcpl, fapl);
            Group fooGroup = h5.getRootGroup().createGroup("/foo");
            for (int j = 0; j < 4; j++) {
                fooGroup.createAttribute("attr" + j, "value" + j);
                System.gc();
            }
            DataSpace.createScalar();
            DataSpace space = new DataSpace(4, 2, 1, 3);
            space.selectElements(SelectionOperator.SET, new Point(0, 0, 0, 0));
            for (int i = 0; i < 5; i++) {
                String groupName = UUID.randomUUID().toString();
                Group group = h5.getRootGroup().createGroup(groupName);
                assertEquals("/" + groupName, group.getName());
                group.close();
                String datasetName = UUID.randomUUID().toString();
                long[] dims = {3, 4, 5};
                DataSet dataset = h5.getRootGroup().createDataSet(datasetName, FloatType.IEEE_F64LE, dims);
                assertEquals("/" + datasetName, dataset.getName());
                dataset.close();
            }
            for (Group group : h5.getRootGroup().getGroups()) {
                group.getName();
            }
            for (DataSet dataset : h5.getRootGroup().getDataSets()) {
                dataset.getName();
            }
            h5.close();
            return Boolean.TRUE;
        }
    }

    @Test
    public void testH5E_error_t() {
        Field[] fields = H5E_error_t.class.getDeclaredFields();
        String[] names = {"maj_num", "min_num", "func_name", "file_name", "line", "desc"};
        assertEquals(names.length, fields.length);
        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], fields[i].getName());
        }
    }

    @Test
    public void testH5get_libversion() {
        IntByReference pmajnum = new IntByReference();
        IntByReference pminnum = new IntByReference();
        IntByReference prelnum = new IntByReference();
        int err = H5Library.INSTANCE.H5get_libversion(pmajnum, pminnum, prelnum);
        assertEquals(0, err);
        assertEquals(1, pmajnum.getValue());
        assertEquals(6, pminnum.getValue());
        assertEquals(7, prelnum.getValue());
    }

    @Test
    public void testHyperslabReadWrite() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5 = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();

        FloatDenseMatrix data = DenseFactory.floatMatrix(5, 6, Order.ROW, Storage.DIRECT);
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
        FloatDenseMatrix out = DenseFactory.floatMatrix(data.rows(), data.columns());
        dataset2.read(out.data(), dtype, DataSpace.ALL, dataspace2);
        dataspace2.close();
        dataset2.close();

        h5.close();
    }

    @Test
    public void testMultipleThreads() throws Throwable {
        int nThreads = 8;
        int loops = 10 * nThreads;
        ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
        CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(threadPool);
        try {
            List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
            for (int i = 0; i < loops; i++) {
                Callable<Boolean> task = new MTTask();
                Future<Boolean> future = completionService.submit(task);
                futures.add(future);
            }
            for (Future<Boolean> future : futures) {
                assertTrue(future.get());
            }
        } finally {
            threadPool.shutdown();
            threadPool.awaitTermination(0L, TimeUnit.MILLISECONDS);
        }
    }
}
