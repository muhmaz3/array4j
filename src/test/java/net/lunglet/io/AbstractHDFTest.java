package net.lunglet.io;

import java.util.UUID;
import net.lunglet.hdf.FileAccessPropList;
import net.lunglet.hdf.FileAccessPropListBuilder;
import net.lunglet.hdf.FileCreatePropList;
import net.lunglet.hdf.H5File;

abstract class AbstractHDFTest {
    protected H5File createMemoryH5File() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5file = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();
        return h5file;
    }
}
