package net.lunglet.array4j.test;

import net.lunglet.svm.SvmClassifierTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

public final class Main {
    @RunWith(Suite.class)
    @SuiteClasses({SvmClassifierTest.class})
    private static final class AllTests {
    }

    public static void main(final String[] args) {
        org.junit.runner.JUnitCore.runClasses(AllTests.class);
    }

    private Main() {
    }
}
