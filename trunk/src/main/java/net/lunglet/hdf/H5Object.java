package net.lunglet.hdf;

import com.sun.jna.NativeLong;
import java.nio.charset.Charset;
import java.util.Arrays;

abstract class H5Object extends IdComponent {
    private static final Charset CHARSET = Charset.forName("US-ASCII");

    public H5Object(final int id) {
        super(id);
    }

    public final Attribute createAttribute(final String name, final DataType type, final DataSpace space) {
        return Attribute.create(getId(), name, type, space);
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

    public String getName() {
        NativeLong size = H5Library.INSTANCE.H5Iget_name(getId(), null, new NativeLong(0));
        if (size.longValue() < 0) {
            throw new H5IdComponentException("H5Iget_name failed");
        }
        byte[] buf = new byte[size.intValue() + 1];
        NativeLong err = H5Library.INSTANCE.H5Iget_name(getId(), buf, new NativeLong(buf.length));
        if (err.longValue() < 0) {
            throw new H5IdComponentException("H5Iget_name failed");
        }
        return new String(Arrays.copyOf(buf, buf.length - 1), CHARSET);
    }

    public final int getNumAttrs() {
        throw new UnsupportedOperationException();
    }

    public final String getStringAttribute(final String name) {
        Attribute attr = openAttribute(name);
        try {
            DataType dtype = attr.getType();
            if (!(dtype instanceof StringType)) {
                dtype.close();
                throw new IllegalArgumentException();
            }
            StringType stype = (StringType) dtype;
            byte[] buf = new byte[stype.getSize()];
            attr.read(buf, stype);
            stype.close();
            return new String(buf, CHARSET);
        } finally {
            attr.close();
        }
    }

    public final Attribute openAttribute(final String name) {
        return Attribute.open(getId(), name);
    }
}
