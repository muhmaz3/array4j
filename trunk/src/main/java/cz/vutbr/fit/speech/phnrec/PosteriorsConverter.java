package cz.vutbr.fit.speech.phnrec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;
import com.googlecode.array4j.io.MatrixOutputStream;

public final class PosteriorsConverter {
    private static final List<String> PHONEMES_TO_IGNORE = Collections.unmodifiableList(Arrays.asList(new String[]{
            "int", "oth", "pau", "spk"}));

    private final FloatDenseMatrix posteriors;

    private final List<MasterLabel> labels;

    // 10ms frame period in the HTK time unit
    private static final long FRAME_PERIOD = 100000L;

    public PosteriorsConverter(final FloatDenseMatrix posteriors, final Collection<? extends MasterLabel> labels) {
        this.posteriors = posteriors;
        this.labels = new ArrayList<MasterLabel>(labels);
    }

    public List<FloatDenseVector> getPhonemePosteriors() {
        List<FloatDenseVector> postsums = new ArrayList<FloatDenseVector>();
        for (MasterLabel label : labels) {
            if (PHONEMES_TO_IGNORE.contains(label.label)) {
                continue;
            }
            int startIndex = (int) (label.startTime / FRAME_PERIOD);
            int endIndex = (int) (label.endTime / FRAME_PERIOD);
            FloatDenseMatrix postpart = posteriors.subMatrixColumns(startIndex, endIndex);
            FloatDenseVector postsum = FloatMatrixUtils.columnSum(postpart);
            postsums.add(postsum);
        }
        return postsums;
    }

    public void writePhonemePosteriors(final File outputFile) throws IOException {
        List<FloatDenseVector> postsums = getPhonemePosteriors();
        MatrixOutputStream out = new MatrixOutputStream(new FileOutputStream(outputFile));
        out.writeColumnsAsMatrix(postsums);
        out.close();
    }

    public void writeMasterLabels(final File outputFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (MasterLabel label : labels) {
            writer.write(label.toString());
            writer.write("\n");
        }
        writer.close();
    }
}
