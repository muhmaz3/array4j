package net.lunglet.hdf;

import com.sun.jna.NativeLong;
import java.nio.charset.Charset;
import java.util.Arrays;

abstract class H5Object extends IdComponent {
    private static final Charset CHARSET = Charset.forName("US-ASCII");

    public H5Object(final int id, final CloseAction closeAction) {
        super(id, closeAction);
    }

    public final Attribute createAttribute(final String name, final DataType type, final DataSpace space) {
        return Attribute.create(getId(), name, type, space);
    }

    public void createAttribute(final String name, final int[] value) {
        DataSpace space = new DataSpace(value.length);
        Attribute attr = null;
        try {
            attr = createAttribute(name, IntType.STD_I32LE, space);
            attr.write(value);
        } finally {
            if (attr != null) {
                attr.close();
            }
            space.close();
        }
    }

    public final void createAttribute(final String name, final String value) {
        DataSpace space = DataSpace.createScalar();
        StringType stype = null;
        Attribute attr = null;
        try {
            stype = StringType.C_S1.copy();
            stype.setSize(value.length());
            attr = createAttribute(name, stype, space);
            byte[] buf = value.getBytes(CHARSET);
            attr.write(buf, stype);
        } finally {
            if (attr != null) {
                attr.close();
            }
            if (stype != null) {
                stype.close();
            }
            space.close();
        }
    }

    public final int[] getIntArrayAttribute(final String name) {
        Attribute attr = openAttribute(name);
        DataType dtype = null;
        DataSpace space = null;
        try {
            dtype = attr.getType();
            if (!dtype.equals(IntType.STD_I32LE)) {
                throw new IllegalArgumentException("invalid datatype for " + attr.getName());
            }
            space = attr.getSpace();
            if (space.getNDims() != 1) {
                throw new IllegalArgumentException();
            }
            int[] arr = new int[(int) space.getDim(0)];
            // TODO check datatype issues
            attr.read(arr);
            return arr;
        } finally {
            if (space != null) {
                space.close();
            }
            if (dtype != null) {
                dtype.close();
            }
            attr.close();
        }
    }

    public String getName() {
        final NativeLong size;
        synchronized (H5Library.INSTANCE) {
            size = H5Library.INSTANCE.H5Iget_name(getId(), null, new NativeLong(0));
            if (size.longValue() < 0) {
                throw new H5IdComponentException("H5Iget_name failed");
            }
        }
        byte[] buf = new byte[size.intValue() + 1];
        synchronized (H5Library.INSTANCE) {
            NativeLong err = H5Library.INSTANCE.H5Iget_name(getId(), buf, new NativeLong(buf.length));
            if (err.longValue() < 0) {
                throw new H5IdComponentException("H5Iget_name failed");
            }
        }
        return new String(Arrays.copyOf(buf, buf.length - 1), CHARSET);
    }

    public final int getNumAttrs() {
        throw new UnsupportedOperationException();
    }

    public final String getStringAttribute(final String name) {
        Attribute attr = openAttribute(name);
        DataType dtype = null;
        try {
            dtype = attr.getType();
            if (!(dtype instanceof StringType)) {
                throw new IllegalArgumentException("invalid datatype for " + attr.getName());
            }
            StringType stype = (StringType) dtype;
            byte[] buf = new byte[(int) stype.getSize()];
            attr.read(buf, stype);
            return new String(buf, CHARSET);
        } finally {
            if (dtype != null) {
                dtype.close();
            }
            attr.close();
        }
    }

    public final Attribute openAttribute(final String name) {
        return Attribute.open(getId(), name);
    }
}
