package net.lunglet.primme;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import net.lunglet.primme.PRIMMELibrary.MatrixMatvecCallback;

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
    public PRIMMEParams() {
        this.numEvals = 0;
        this.target = Target.smallest.ordinal();
        this.n = 0;
        this.matrixMatvec = null;
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
        this.iseed = new int[]{-1, 0, 0, 0};
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
    }

    public int n;

    public MatrixMatvecCallback matrixMatvec;

    public Pointer applyPreconditioner;

    public Pointer massMatrixMatvec;

    public int numProcs;

    public int procID;

    public int nLocal;

    public Pointer commInfo;

    public Pointer globalSumDouble;

    /** Number of eigenvalues wanted. */
    public int numEvals;

    public int target;

    public int numTargetShifts;

    public Pointer targetShifts;

    public int dynamicMethodSwitch;

    public int locking;

    public int initSize;

    public int numOrthoConst;

    public int maxBasisSize;

    public int minRestartSize;

    public int maxBlockSize;

    public int maxMatvecs;

    public int maxOuterIterations;

    public int intWorkSize;

    public NativeLong realWorkSize;

    public int[] iseed;

    public Pointer intWork;

    public Pointer realWork;

    public double aNorm;

    public double eps;

    public int printLevel;

    public Pointer outputFile;

    public Pointer matrix;

    public Pointer preconditioner;

    public Pointer shiftsForPreconditioner;

    public RestartingParams restartingParams;

    public CorrectionParams correctionParams;

    public Stats stats;

    public Pointer stackTrace;
}
