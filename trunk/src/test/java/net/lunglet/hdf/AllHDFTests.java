package net.lunglet.hdf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AttributeTest.class, DataSetTest.class, DataSpaceTest.class, FilterTest.class, GroupTest.class,
        H5FileTest.class, H5LibraryTest.class, TypesTest.class})
public final class AllHDFTests {

}
