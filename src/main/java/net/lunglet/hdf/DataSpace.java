package net.lunglet.hdf;

import com.sun.jna.NativeLong;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.Arrays;
import net.lunglet.util.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataSpace extends IdComponent {
    public static final DataSpace ALL = new DataSpace(H5Library.H5S_ALL, true);

    private static final CloseAction CLOSE_ACTION = new CloseAction() {
        @Override
        public void close(final int id) {
            if (id != H5Library.H5S_ALL) {
                // not a constant, should call H5Sclose
                int err = H5Library.INSTANCE.H5Sclose(id);
                if (err < 0) {
                    throw new H5DataSpaceException("H5Sclose failed");
                }
            }
        }
    };

    public static DataSpace createScalar() {
        return new DataSpace(DataSpaceClass.SCALAR);
    }

    private static int init(final DataSpaceClass type) {
        int id = H5Library.INSTANCE.H5Screate(type.intValue());
        if (id < 0) {
            throw new H5DataSpaceException("H5Screate failed");
        }
        return id;
    }

    private static int init(final long[] dims, final long[] maxdims) {
        if (maxdims != null && dims.length != maxdims.length) {
            throw new IllegalArgumentException();
        }
        int id = H5Library.INSTANCE.H5Screate_simple(dims.length, dims, maxdims);
        if (id < 0) {
            throw new H5DataSpaceException("H5Screate_simple failed");
        }
        return id;
    }

    private final Logger logger = LoggerFactory.getLogger(DataSpace.class);

    private DataSpace(final DataSpaceClass type) {
        this(init(type), true);
    }

    DataSpace(final int id, final boolean tag) {
        // tag helps to avoid confusion with long... constructor
        super(id, CLOSE_ACTION);
        logger.debug("Created [id={}]", getId());
    }

    public DataSpace(final long... dims) {
        this(dims, null);
    }

    public DataSpace(final long[] dims, final long[] maxdims) {
        this(init(dims, maxdims), true);
    }

    public long getDim(final int index) {
        return getDims()[index];
    }

    public long[] getDims() {
        long[] dims = new long[getNDims()];
        int err = H5Library.INSTANCE.H5Sget_simple_extent_dims(getId(), dims, null);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sget_simple_extent_dims failed");
        }
        return dims;
    }

    public Hyperslab[] getHyperslabs() {
        if (getSelectHyperNBlocks() > Integer.MAX_VALUE) {
            throw new UnsupportedOperationException();
        }
        return getHyperslabs(0, (int) getSelectHyperNBlocks());
    }

    public Hyperslab[] getHyperslabs(final long startblock, final int numblocks) {
        if (!getSelectType().equals(SelectionType.HYPERSLABS)) {
            throw new IllegalStateException();
        }
        if (numblocks > getSelectHyperNBlocks()) {
            throw new IllegalArgumentException();
        }
        int rank = getNDims();
        // TODO use a direct buffer here to avoid a copy
        long[] buf = new long[2 * numblocks * rank];
        int err = H5Library.INSTANCE.H5Sget_select_hyper_blocklist(getId(), startblock, numblocks, buf);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sget_select_hyper_blocklist failed");
        }
        Hyperslab[] slabs = new Hyperslab[numblocks];
        for (int i = 0; i < numblocks; i++) {
            int j = 2 * rank * i;
            int k = j + rank;
            Point start = new Point(Arrays.copyOfRange(buf, j, k));
            Point end = new Point(Arrays.copyOfRange(buf, k, k + rank));
            slabs[i] = new Hyperslab(start, end);
        }
        return slabs;
    }

    public int[] getIntDims() {
        return ArrayUtils.castOf(getDims());
    }

    public long[] getMaxDims() {
        long[] maxdims = new long[getNDims()];
        int err = H5Library.INSTANCE.H5Sget_simple_extent_dims(getId(), null, maxdims);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sget_simple_extent_dims failed");
        }
        return maxdims;
    }

    public int getNDims() {
        int ndims = H5Library.INSTANCE.H5Sget_simple_extent_ndims(getId());
        if (ndims < 0) {
            throw new H5DataSpaceException("H5Sget_simple_extent_ndims failed");
        }
        return ndims;
    }

    public Point[] getSelectBounds() {
        if (getSelectType().equals(SelectionType.NONE)) {
            throw new IllegalStateException();
        }
        int rank = getNDims();
        long[] start = new long[rank];
        long[] end = new long[rank];
        int err = H5Library.INSTANCE.H5Sget_select_bounds(getId(), start, end);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sget_select_bounds failed");
        }
        return new Point[]{new Point(start), new Point(end)};
    }

    public Point[] getSelectedPoints() {
        if (getSelectNPoints() > Integer.MAX_VALUE) {
            throw new UnsupportedOperationException();
        }
        return getSelectedPoints(0, (int) getSelectNPoints());
    }

    public Point[] getSelectedPoints(final long startpoint, final int numpoints) {
        if (!getSelectType().equals(SelectionType.POINTS)) {
            throw new IllegalStateException();
        }
        if (numpoints > getSelectNPoints()) {
            throw new IllegalArgumentException();
        }
        int rank = getNDims();
        // TODO use a direct buffer here to avoid a copy
        long[] buf = new long[numpoints * rank];
        int err = H5Library.INSTANCE.H5Sget_select_elem_pointlist(getId(), startpoint, numpoints, buf);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sget_select_elem_pointlist failed");
        }
        Point[] points = new Point[numpoints];
        for (int i = 0; i < numpoints; i++) {
            points[i] = new Point(Arrays.copyOfRange(buf, i * rank, (i + 1) * rank));
        }
        return points;
    }

    public long getSelectHyperNBlocks() {
        if (!getSelectType().equals(SelectionType.HYPERSLABS)) {
            throw new IllegalStateException();
        }
        long size = H5Library.INSTANCE.H5Sget_select_hyper_nblocks(getId());
        if (size < 0) {
            throw new H5DataSpaceException("H5Sget_select_hyper_nblocks failed");
        }
        return size;
    }

    public long getSelectNPoints() {
        long size = H5Library.INSTANCE.H5Sget_select_npoints(getId());
        if (size < 0) {
            throw new H5DataSpaceException("H5Sget_select_npoints failed");
        }
        return size;
    }

    public SelectionType getSelectType() {
        int seltype = H5Library.INSTANCE.H5Sget_select_type(getId());
        if (seltype < 0) {
            throw new H5DataSpaceException("H5Sget_select_type failed");
        }
        for (SelectionType s : SelectionType.values()) {
            if (s.intValue() == seltype) {
                return s;
            }
        }
        throw new AssertionError();
    }

    public long getSimpleExtentNpoints() {
        long size = H5Library.INSTANCE.H5Sget_simple_extent_npoints(getId());
        if (size < 0) {
            throw new H5DataSpaceException("H5Sget_simple_extent_npoints failed");
        }
        return size;
    }

    public boolean isSimple() {
        int tri = H5Library.INSTANCE.H5Sis_simple(getId());
        if (tri < 0) {
            throw new H5DataSpaceException("H5Sis_simple failed");
        }
        return tri > 0;
    }

    public void offsetSimple(final long[] offset) {
        if (offset != null && offset.length != getNDims()) {
            throw new IllegalArgumentException();
        }
        int err = H5Library.INSTANCE.H5Soffset_simple(getId(), offset);
        if (err < 0) {
            throw new H5DataSpaceException("H5Soffset_simple failed");
        }
    }

    public void selectAll() {
        int err = H5Library.INSTANCE.H5Sselect_all(getId());
        if (err < 0) {
            throw new H5DataSpaceException("H5Sselect_all failed");
        }
    }

    public void selectElements(final SelectionOperator op, final Point... points) {
        int rank = getNDims();
        ByteBuffer buf = ByteBuffer.allocateDirect(H5Library.LONG_BYTES * points.length * rank);
        LongBuffer coord = buf.order(ByteOrder.nativeOrder()).asLongBuffer();
        for (int i = 0; i < points.length; i++) {
            if (points[i].getNDims() != rank) {
                throw new IllegalArgumentException();
            }
            coord.put(points[i].getCoordinates());
        }
        int err = H5Library.INSTANCE.H5Sselect_elements(getId(), op.intValue(), new NativeLong(points.length), coord);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sselect_elements failed");
        }
    }

    public void selectHyperslab(final SelectionOperator op, final long[] start, final long[] count) {
        selectHyperslab(op, start, null, count, null);
    }

    /**
     * Selects a hyperslab region to add to the current selected region.
     * <p>
     * Regions selected with this function call default to C order iteration
     * when I/O is performed.
     *
     * @param start starting coordinates of the hyperslab to select
     * @param stride how many elements to move in each dimension
     * @param count how many blocks to select from the dataspace, in each dimension
     * @param block size of the element block selected from the dataspace
     */
    public void selectHyperslab(final SelectionOperator op, final long[] start, final long[] stride,
            final long[] count, final long[] block) {
        int rank = getNDims();
        if (start.length != rank) {
            throw new IllegalArgumentException();
        }
        if (stride != null && stride.length != rank) {
            throw new IllegalArgumentException();
        }
        if (count.length != rank) {
            throw new IllegalArgumentException();
        }
        if (block != null && block.length != rank) {
            throw new IllegalArgumentException();
        }
        int err = H5Library.INSTANCE.H5Sselect_hyperslab(getId(), op.intValue(), start, stride, count, block);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sselect_hyperslab failed");
        }
    }

    public void selectNone() {
        int err = H5Library.INSTANCE.H5Sselect_none(getId());
        if (err < 0) {
            throw new H5DataSpaceException("H5Sselect_none failed");
        }
    }

    public boolean selectValid() {
        int tri = H5Library.INSTANCE.H5Sselect_valid(getId());
        if (tri < 0) {
            throw new H5DataSpaceException("H5Sselect_valid failed");
        }
        return tri > 0;
    }

    @Override
    public String toString() {
        if (equals(ALL)) {
            return "DataSpace[ALL]";
        } else if (isOpen()) {
            return "DataSpace[dims=" + Arrays.toString(getDims()) + ", maxdims=" + Arrays.toString(getMaxDims()) + "]";
        } else {
            return "DataSpace[invalid]";
        }
    }
}
