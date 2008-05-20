package net.lunglet.array4j;

import net.lunglet.hdf.AllHDFTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AllHDFTests.class})
public final class AllTests {
    public static void main(final String[] args) {
        org.junit.runner.JUnitCore.runClasses(AllTests.class);
    }

    private AllTests() {
    }
}
