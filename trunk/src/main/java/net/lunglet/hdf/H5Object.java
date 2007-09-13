package net.lunglet.hdf;

import com.sun.jna.NativeLong;
import java.nio.charset.Charset;
import java.util.Arrays;

abstract class H5Object extends IdComponent {
    public H5Object(final int id) {
        super(id);
    }

    public final Attribute createAttribute(final String name, final DataType type, final DataSpace space) {
        return Attribute.create(getId(), name, type, space);
    }

    public final void createAttribute(final String name, final String value) {
//        DataSpace space = DataSpace.createScalar();
//        space.close();
        throw new UnsupportedOperationException();
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
        return new String(Arrays.copyOf(buf, buf.length - 1), Charset.forName("US-ASCII"));
    }

    public final int getNumAttrs() {
        return 0;
    }

    public final String getStringAttribute(final String name) {
        throw new UnsupportedOperationException();
    }

    public final Attribute openAttribute(final String name) {
        return Attribute.open(getId(), name);
    }
}
