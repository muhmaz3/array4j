package cz.vutbr.fit.speech.phnrec;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat.Encoding;

import net.lunglet.sound.sampled.RawAudioFileWriter;

import com.googlecode.array4j.dense.FloatDenseMatrix;

public final class PhnRec {
    private static final File TEMP_PCM_FILE;

    private static final File TEMP_POSTERIORS_FILE;

    private static final File TEMP_STRINGS_FILE;

    static {
        try {
            TEMP_PCM_FILE = File.createTempFile("pcm", ".snd", new File(".")).getCanonicalFile();
            TEMP_PCM_FILE.deleteOnExit();
            TEMP_POSTERIORS_FILE = File.createTempFile("post", ".htk", new File(".")).getCanonicalFile();
            TEMP_POSTERIORS_FILE.deleteOnExit();
            TEMP_STRINGS_FILE = File.createTempFile("mlf", ".txt", new File(".")).getCanonicalFile();
            TEMP_STRINGS_FILE.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final PhnRecSystem[] PHNREC_SYSTEMS = {new PhnRecSystem("PHN_CZ_SPDAT_LCRC_N1500", "cz"),
            new PhnRecSystem("PHN_HU_SPDAT_LCRC_N1500", "hu"), new PhnRecSystem("PHN_RU_SPDAT_LCRC_N1500", "ru")};

    private static byte[][] splitChannels(final AudioInputStream sourceStream) throws IOException {
        int channels = sourceStream.getFormat().getChannels();
        byte[][] channelsData = new byte[channels][];
        int sampleSizeInBits = sourceStream.getFormat().getSampleSizeInBits();
        if (sampleSizeInBits % 8 != 0) {
            throw new UnsupportedOperationException();
        }
        int sampleSizeInBytes = sampleSizeInBits >>> 3;
        if (sourceStream.getFrameLength() > Integer.MAX_VALUE) {
            throw new UnsupportedOperationException();
        }
        int frameLength = (int) sourceStream.getFrameLength();
        for (int i = 0; i < channels; i++) {
            channelsData[i] = new byte[sampleSizeInBytes * frameLength];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AudioSystem.write(sourceStream, RawAudioFileWriter.RAW, baos);
        byte[] samples = baos.toByteArray();
        for (int i = 0, sampleOffset = 0; i < frameLength; i++) {
            for (int j = 0; j < channels; j++) {
                for (int k = 0; k < sampleSizeInBytes; k++, sampleOffset++) {
                    int channelOffset = i * sampleSizeInBytes + k;
                    channelsData[j][channelOffset] = samples[sampleOffset];
                }
            }
        }
        return channelsData;
    }

    private static void processFile(final File inputFile, final PhnRecSystem system)
            throws UnsupportedAudioFileException, IOException {
        AudioInputStream sourceStream = AudioSystem.getAudioInputStream(inputFile);
        AudioInputStream targetStream = AudioSystem.getAudioInputStream(Encoding.PCM_SIGNED, sourceStream);
        byte[][] channelsData = splitChannels(targetStream);
        for (int i = 0; i < channelsData.length; i++) {
            FileOutputStream fos = new FileOutputStream(TEMP_PCM_FILE);
            fos.write(channelsData[i]);
            fos.close();
            system.waveformToPosteriors(TEMP_PCM_FILE, TEMP_POSTERIORS_FILE);
            FloatDenseMatrix posteriors = system.readPosteriors(TEMP_POSTERIORS_FILE);
            system.posteriorsToStrings(TEMP_POSTERIORS_FILE, TEMP_STRINGS_FILE);
            List<MasterLabel> labels = system.readStrings(TEMP_STRINGS_FILE);
            PosteriorsConverter postConv = new PosteriorsConverter(posteriors, labels);
            String postSuffix = "." + i + "." + system.getShortName() + ".post";
            File postOutputFile = new File(inputFile.getAbsolutePath() + postSuffix);
            postConv.writePhonemePosteriors(postOutputFile);
            String mlfSuffix = "." + i + "." + system.getShortName() + ".mlf.txt";
            File mlfOutputFile = new File(inputFile.getAbsolutePath() + mlfSuffix);
            postConv.writeMasterLabels(mlfOutputFile);
        }
    }

    public static void main(final String[] args) throws UnsupportedAudioFileException, IOException {
        File[] files = new File[]{new File("SP_4089.SPH")};
        for (File file : files) {
            for (int i = 0; i < PHNREC_SYSTEMS.length; i++) {
                processFile(file, PHNREC_SYSTEMS[i]);
            }
        }
    }
}
