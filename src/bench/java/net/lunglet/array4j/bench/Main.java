package net.lunglet.array4j.bench;

import net.lunglet.array4j.blas.FloatDenseBLASBenchmark;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

public final class Main {
    @RunWith(Suite.class)
    @SuiteClasses({FloatDenseBLASBenchmark.class})
    private static final class AllTests {
    }

    public static void main(final String[] args) {
        org.junit.runner.JUnitCore.runClasses(AllTests.class);
    }

    private Main() {
    }
}
