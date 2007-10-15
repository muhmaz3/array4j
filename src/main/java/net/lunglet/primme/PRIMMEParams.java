package net.lunglet.primme;

import com.sun.jna.Callback;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.nio.DoubleBuffer;
import net.lunglet.primme.PRIMMELibrary.MatrixMatvecCallback;

// TODO fix order of fields in this structure

/**
 * PRIMME parameters.
 * <p>
 * <CODE>
 * typedef struct primme_params {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int n;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;void (*matrixMatvec)( void *x,  void *y, int *blockSize, struct primme_params *primme);<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;void (*applyPreconditioner)(void *x, void *y, int *blockSize, struct primme_params *primme);<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;void (*massMatrixMatvec)(void *x, void *y, int *blockSize, struct primme_params *primme);<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int numProcs;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int procID;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int nLocal;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;void *commInfo;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;void (*globalSumDouble)(void *sendBuf, void *recvBuf, int *count, struct primme_params *primme);<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int numEvals;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;primme_target target;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int numTargetShifts;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;double *targetShifts;<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int dynamicMethodSwitch;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int locking;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int initSize;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int numOrthoConst;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int maxBasisSize;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int minRestartSize;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int maxBlockSize;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int maxMatvecs;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int maxOuterIterations;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int intWorkSize;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;long int realWorkSize;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int iseed[4];<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int *intWork;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;void *realWork;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;double aNorm;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;double eps;<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int printLevel;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;FILE *outputFile;<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;void *matrix;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;void *preconditioner;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;double *ShiftsForPreconditioner;<br>
 * <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;struct restarting_params restartingParams;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;struct correction_params correctionParams;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;struct primme_stats stats;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;struct stackTraceNode *stackTrace;<br>
 * } primme_params;
 * </CODE>
 */
public final class PRIMMEParams extends Structure {
    /**
     * @param numEvals number of eigenvalues wanted
     * @param n dimension of the matrix
     */
    public PRIMMEParams(final int numEvals, final Target target, final int n, final MatrixMatvecCallback matrixMatvec) {
        this.numEvals = numEvals;
        this.target = target.ordinal();
        this.n = n;
        this.matrixMatvec = matrixMatvec;
        // set default values for other fields
        this.numProcs = 1;
        this.procID = 0;
        this.applyPreconditioner = null;
        this.massMatrixMatvec = null;
        this.numTargetShifts = 0;
        this.targetShifts = null;
        this.printLevel = 0;
        this.aNorm = 0.0;
        this.eps = 1.0e-12;
        this.outputFile = null;
        this.commInfo = null;
        this.globalSumDouble = null;
        this.intWorkSize = 0;
        this.intWork = null;
        this.iseed = new int[]{1, 2, 3, 5};
        this.matrix = null;
        this.applyPreconditioner = null;
        this.shiftsForPreconditioner = null;
        this.restartingParams = new RestartingParams();
        restartingParams.scheme = RestartScheme.thick.ordinal();
        restartingParams.maxPrevRetain = 1;
        this.correctionParams = new CorrectionParams();
        correctionParams.precondition = 0;
        correctionParams.robustShifts = 1;
        correctionParams.maxInnerIterations = 0;
        correctionParams.convTest = ConvergenceTest.full_LTolerance.ordinal();
        correctionParams.projectors = new JDProjectors();
        allocateMemory();
        PRIMMELibrary.INSTANCE.primme_initialize(this);
        int ret = PRIMMELibrary.INSTANCE.primme_set_method(PresetMethod.DYNAMIC, this);
        if (ret == 0 || ret != 0) {
            throw new RuntimeException("primme_set_method failed");
        }
    }

    double aNorm;

    Pointer applyPreconditioner;

    Pointer commInfo;

    CorrectionParams correctionParams;

    int dynamicMethodSwitch;

    double eps;

    Pointer globalSumDouble;

    int initSize;

    int intWorkSize;

    int locking;

    Callback massMatrixMatvec;

    Pointer matrix;

    Callback matrixMatvec;

    int maxBasisSize;

    int maxBlockSize;

    int maxMatvecs;

    int maxOuterIterations;

    int minRestartSize;

    int n;

    int nLocal;

    /** Number of eigenvalues wanted. */
    int numEvals;

    int numOrthoConst;

    int numProcs;

    NativeLong realWorkSize;

    int[] iseed;

    Pointer intWork;

    int numTargetShifts;

    Pointer outputFile;

    Pointer preconditioner;

    int printLevel;

    int procID;

    Pointer realWork;

    RestartingParams restartingParams;

    DoubleBuffer shiftsForPreconditioner;

    Pointer stackTrace;

    Stats stats;

    int target;

    DoubleBuffer targetShifts;
}
