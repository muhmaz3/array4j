package net.lunglet.fft.mkl;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.PointerByReference;
import java.nio.Buffer;

// TODO make argument converters for enum types

public interface DftiLibrary extends Library {
    DftiLibrary INSTANCE = (DftiLibrary) Native.loadLibrary("array4j", DftiLibrary.class);

    NativeLong DftiCommitDescriptor(DftiDescriptorHandle handle);

    /**
     * <CODE>long DftiComputeBackward(DFTI_DESCRIPTOR_HANDLE, void*, ...);</CODE>
     */
    NativeLong DftiComputeBackward(DftiDescriptorHandle handle, Buffer buf, Object... args);

    /**
     * <CODE>long DftiComputeForward(DFTI_DESCRIPTOR_HANDLE, void*, ...);</CODE>
     */
    // TODO map the various variations of arguments as separate functions
    NativeLong DftiComputeForward(DftiDescriptorHandle handle, Buffer buf, Object... args);

    NativeLong DftiCopyDescriptor(DftiDescriptorHandle srcHandle, PointerByReference destHandle);

    /**
     * <CODE>
     * long DftiCreateDescriptor(DFTI_DESCRIPTOR_HANDLE*, enum DFTI_CONFIG_VALUE, enum DFTI_CONFIG_VALUE, long, ...);
     * </CODE>
     */
    NativeLong DftiCreateDescriptor(PointerByReference handle, DftiConfigValue config1, DftiConfigValue config2,
            NativeLong i, Object... args);

    NativeLong DftiErrorClass(NativeLong i, NativeLong j);

    String DftiErrorMessage(NativeLong i);

    NativeLong DftiFreeDescriptor(PointerByReference handle);

    NativeLong DftiGetValue(DftiDescriptorHandle handle, DftiConfigParam param, Object... args);

    NativeLong DftiSetValue(DftiDescriptorHandle handle, DftiConfigParam param, Object... args);
}
