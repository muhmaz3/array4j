package net.lunglet.hdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileCreatePropList extends PropertyList {
    public static final FileCreatePropList DEFAULT = new FileCreatePropList(H5Library.H5P_DEFAULT);

    private final Logger logger = LoggerFactory.getLogger(FileCreatePropList.class);

    FileCreatePropList() {
        this(create(PropertyListClass.H5P_FILE_CREATE));
    }

    FileCreatePropList(final int id) {
        super(id);
        logger.debug("Created [id={}]", getId());
    }
}
