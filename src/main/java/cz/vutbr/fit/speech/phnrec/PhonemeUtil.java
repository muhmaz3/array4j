package cz.vutbr.fit.speech.phnrec;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixMath;
import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;

public final class PhonemeUtil {
    private PhonemeUtil() {
    }

    public static FloatDenseVector calculateNGrams(final FloatDenseMatrix posteriors) {
        FloatDenseVector monograms = FloatMatrixUtils.columnSum(posteriors);
        monograms.plusEquals(1.0f);
        monograms.divideEquals(FloatMatrixUtils.sum(monograms));
        FloatMatrixMath.logEquals(monograms);
        monograms.minusEquals(FloatMatrixUtils.mean(monograms));

        FloatDenseMatrix b1 = posteriors.subMatrixColumns(0, posteriors.columns() - 1);
        FloatDenseMatrix b2 = posteriors.subMatrixColumns(1, posteriors.columns());
        FloatMatrix<?, ?> bigrams = b1.times(b2.transpose());
        bigrams.plusEquals(1.0f);
        bigrams.divideEquals(FloatMatrixUtils.sum(bigrams));
        FloatMatrixMath.logEquals(bigrams);
        bigrams.minusEquals(FloatMatrixUtils.mean(bigrams));

        FloatDenseVector ngrams = FloatMatrixUtils.concatenate(monograms, bigrams.asVector());
        return ngrams;
    }
}
