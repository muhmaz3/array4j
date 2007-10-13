package net.lunglet.primme;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.nio.DoubleBuffer;

public interface PRIMMELibrary extends Library {
    final class PRIMMEStats extends Structure {
        int numOuterIterations;

        int numRestarts;

        int numMatvecs;

        int numPreconds;

        double elapsedTime;
    }

    final class JDProjectors extends Structure {
        int leftQ;

        int leftX;

        int rightQ;

        int rightX;

        int skewQ;

        int skewX;
    }

    final class CorrectionParams extends Structure {
        int precondition;

        int robustShifts;

        int maxInnerIterations;

        JDProjectors projectors;

        int convTest;

        double relTolBase;
    }

    final class RestartingParams extends Structure {
        int scheme;

        int maxPrevRetain;
    }

    final class Stats extends Structure {
        int numOuterIterations;

        int numRestarts;

        int numMatvecs;

        int numPreconds;

        double elapsedTime;
    }

    final class PRIMMEParams extends Structure {
        int n;

        Callback matrixMatvec;

        Callback applyPreconditioner;

        Callback massMatrixMatvec;

        int numProcs;

        int procID;

        int nLocal;

        Pointer commInfo;

        Pointer globalSumDouble;

        int numEvals;

        int target;

        int numTargetShifts;

        DoubleBuffer targetShifts;

        int dynamicMethodSwitch;

        int locking;

        int initSize;

        int numOrthoConst;

        int maxBasisSize;

        int minRestartSize;

        int maxBlockSize;

        int maxMatvecs;

        int maxOuterIterations;

        int intWorkSize;

        // long int realWorkSize;

        // int iseed[4];

        // int *intWork;

        Pointer realWork;

        double aNorm;

        double eps;

        int printLevel;

        Pointer outputFile;

        Pointer matrix;

        Pointer preconditioner;

        DoubleBuffer shiftsForPreconditioner;

        RestartingParams restartingParams;

        CorrectionParams correctionParams;

        Stats stats;

        Pointer stackTrace;
    }

    void primme_initialize(PRIMMEParams params);

    int primme_set_method(int method, PRIMMEParams params);

    int dprimme(DoubleBuffer evals, DoubleBuffer evecs, DoubleBuffer resNorms, PRIMMEParams params);

    void primme_Free(PRIMMEParams params);
}
