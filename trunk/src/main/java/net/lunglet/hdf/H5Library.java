package net.lunglet.hdf;

import java.nio.ByteBuffer;

import com.sun.jna.Function;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

interface H5Library extends Library {
    static final class Loader {
        private static int loadIntValue(final String name) {
            int err = loadLibrary().H5open();
            if (err < 0) {
                throw new RuntimeException();
            }
            return new Function(LIBRARY_NAME, name).getInt(0);
        }

        private static H5Library loadLibrary() {
            return (H5Library) Native.loadLibrary(LIBRARY_NAME, H5Library.class);
        }

        private Loader() {
        }
    }

    int H5P_DATASET_CREATE = Loader.loadIntValue("H5P_CLS_DATASET_CREATE_g");

    int H5P_DATASET_CREATE_DEFAULT = Loader.loadIntValue("H5P_LST_DATASET_CREATE_g");

    int H5P_DATASET_XFER = Loader.loadIntValue("H5P_CLS_DATASET_XFER_g");

    int H5P_DATASET_XFER_DEFAULT = Loader.loadIntValue("H5P_LST_DATASET_XFER_g");

    int H5P_DEFAULT = 0;

    int H5P_FILE_ACCESS = Loader.loadIntValue("H5P_CLS_FILE_ACCESS_g");

    int H5P_FILE_ACCESS_DEFAULT = Loader.loadIntValue("H5P_LST_FILE_ACCESS_g");

    int H5P_FILE_CREATE = Loader.loadIntValue("H5P_CLS_FILE_CREATE_g");

    int H5P_FILE_CREATE_DEFAULT = Loader.loadIntValue("H5P_LST_FILE_CREATE_g");

    int H5P_MOUNT = Loader.loadIntValue("H5P_CLS_MOUNT_g");

    int H5P_MOUNT_DEFAULT = Loader.loadIntValue("H5P_LST_MOUNT_g");

    int H5P_NO_CLASS = Loader.loadIntValue("H5P_CLS_NO_CLASS_g");

    int H5P_NO_CLASS_DEFAULT = Loader.loadIntValue("H5P_LST_NO_CLASS_g");

    int H5T_C_S1 = Loader.loadIntValue("H5T_C_S1_g");

    int H5T_FORTRAN_S1 = Loader.loadIntValue("H5T_FORTRAN_S1_g");

    int H5T_IEEE_F32BE = Loader.loadIntValue("H5T_IEEE_F32BE_g");

    int H5T_IEEE_F32LE = Loader.loadIntValue("H5T_IEEE_F32LE_g");

    int H5T_IEEE_F64BE = Loader.loadIntValue("H5T_IEEE_F64BE_g");

    int H5T_IEEE_F64LE = Loader.loadIntValue("H5T_IEEE_F64LE_g");

    int H5T_NATIVE_B16 = Loader.loadIntValue("H5T_NATIVE_B16_g");

    int H5T_NATIVE_B32 = Loader.loadIntValue("H5T_NATIVE_B32_g");

    int H5T_NATIVE_B64 = Loader.loadIntValue("H5T_NATIVE_B64_g");

    int H5T_NATIVE_B8 = Loader.loadIntValue("H5T_NATIVE_B8_g");

    int H5T_NATIVE_DOUBLE = Loader.loadIntValue("H5T_NATIVE_DOUBLE_g");

    int H5T_NATIVE_FLOAT = Loader.loadIntValue("H5T_NATIVE_FLOAT_g");

    int H5T_NATIVE_HADDR = Loader.loadIntValue("H5T_NATIVE_HADDR_g");

    int H5T_NATIVE_HBOOL = Loader.loadIntValue("H5T_NATIVE_HBOOL_g");

    int H5T_NATIVE_HERR = Loader.loadIntValue("H5T_NATIVE_HERR_g");

    int H5T_NATIVE_HSIZE = Loader.loadIntValue("H5T_NATIVE_HSIZE_g");

    int H5T_NATIVE_HSSIZE = Loader.loadIntValue("H5T_NATIVE_HSSIZE_g");

    int H5T_NATIVE_INT = Loader.loadIntValue("H5T_NATIVE_INT_g");

    int H5T_NATIVE_INT_FAST16 = Loader.loadIntValue("H5T_NATIVE_INT_FAST16_g");

    int H5T_NATIVE_INT_FAST32 = Loader.loadIntValue("H5T_NATIVE_INT_FAST32_g");

    int H5T_NATIVE_INT_FAST64 = Loader.loadIntValue("H5T_NATIVE_INT_FAST64_g");

    int H5T_NATIVE_INT_FAST8 = Loader.loadIntValue("H5T_NATIVE_INT_FAST8_g");

    int H5T_NATIVE_INT_LEAST16 = Loader.loadIntValue("H5T_NATIVE_INT_LEAST16_g");

    int H5T_NATIVE_INT_LEAST32 = Loader.loadIntValue("H5T_NATIVE_INT_LEAST32_g");

    int H5T_NATIVE_INT_LEAST64 = Loader.loadIntValue("H5T_NATIVE_INT_LEAST64_g");

    int H5T_NATIVE_INT_LEAST8 = Loader.loadIntValue("H5T_NATIVE_INT_LEAST8_g");

    int H5T_NATIVE_INT16 = Loader.loadIntValue("H5T_NATIVE_INT16_g");

    int H5T_NATIVE_INT32 = Loader.loadIntValue("H5T_NATIVE_INT32_g");

    int H5T_NATIVE_INT64 = Loader.loadIntValue("H5T_NATIVE_INT64_g");

    int H5T_NATIVE_INT8 = Loader.loadIntValue("H5T_NATIVE_INT8_g");

    int H5T_NATIVE_LDOUBLE = Loader.loadIntValue("H5T_NATIVE_LDOUBLE_g");

    int H5T_NATIVE_LLONG = Loader.loadIntValue("H5T_NATIVE_LLONG_g");

    int H5T_NATIVE_LONG = Loader.loadIntValue("H5T_NATIVE_LONG_g");

    int H5T_NATIVE_OPAQUE = Loader.loadIntValue("H5T_NATIVE_OPAQUE_g");

    int H5T_NATIVE_SCHAR = Loader.loadIntValue("H5T_NATIVE_SCHAR_g");

    int H5T_NATIVE_SHORT = Loader.loadIntValue("H5T_NATIVE_SHORT_g");

    int H5T_NATIVE_UCHAR = Loader.loadIntValue("H5T_NATIVE_UCHAR_g");

    int H5T_NATIVE_UINT = Loader.loadIntValue("H5T_NATIVE_UINT_g");

    int H5T_NATIVE_UINT_FAST16 = Loader.loadIntValue("H5T_NATIVE_UINT_FAST16_g");

    int H5T_NATIVE_UINT_FAST32 = Loader.loadIntValue("H5T_NATIVE_UINT_FAST32_g");

    int H5T_NATIVE_UINT_FAST64 = Loader.loadIntValue("H5T_NATIVE_UINT_FAST64_g");

    int H5T_NATIVE_UINT_FAST8 = Loader.loadIntValue("H5T_NATIVE_UINT_FAST8_g");

    int H5T_NATIVE_UINT_LEAST16 = Loader.loadIntValue("H5T_NATIVE_UINT_LEAST16_g");

    int H5T_NATIVE_UINT_LEAST32 = Loader.loadIntValue("H5T_NATIVE_UINT_LEAST32_g");

    int H5T_NATIVE_UINT_LEAST64 = Loader.loadIntValue("H5T_NATIVE_UINT_LEAST64_g");

    int H5T_NATIVE_UINT_LEAST8 = Loader.loadIntValue("H5T_NATIVE_UINT_LEAST8_g");

    int H5T_NATIVE_UINT16 = Loader.loadIntValue("H5T_NATIVE_UINT16_g");

    int H5T_NATIVE_UINT32 = Loader.loadIntValue("H5T_NATIVE_UINT32_g");

    int H5T_NATIVE_UINT64 = Loader.loadIntValue("H5T_NATIVE_UINT64_g");

    int H5T_NATIVE_UINT8 = Loader.loadIntValue("H5T_NATIVE_UINT8_g");

    int H5T_NATIVE_ULLONG = Loader.loadIntValue("H5T_NATIVE_ULLONG_g");

    int H5T_NATIVE_ULONG = Loader.loadIntValue("H5T_NATIVE_ULONG_g");

    int H5T_NATIVE_USHORT = Loader.loadIntValue("H5T_NATIVE_USHORT_g");

    int H5T_STD_B16BE = Loader.loadIntValue("H5T_STD_B16BE_g");

    int H5T_STD_B16LE = Loader.loadIntValue("H5T_STD_B16LE_g");

    int H5T_STD_B32BE = Loader.loadIntValue("H5T_STD_B32BE_g");

    int H5T_STD_B32LE = Loader.loadIntValue("H5T_STD_B32LE_g");

    int H5T_STD_B64BE = Loader.loadIntValue("H5T_STD_B64BE_g");

    int H5T_STD_B64LE = Loader.loadIntValue("H5T_STD_B64LE_g");

    int H5T_STD_B8BE = Loader.loadIntValue("H5T_STD_B8BE_g");

    int H5T_STD_B8LE = Loader.loadIntValue("H5T_STD_B8LE_g");

    int H5T_STD_I16BE = Loader.loadIntValue("H5T_STD_I16BE_g");

    int H5T_STD_I16LE = Loader.loadIntValue("H5T_STD_I16LE_g");

    int H5T_STD_I32BE = Loader.loadIntValue("H5T_STD_I32BE_g");

    int H5T_STD_I32LE = Loader.loadIntValue("H5T_STD_I32LE_g");

    int H5T_STD_I64BE = Loader.loadIntValue("H5T_STD_I64BE_g");

    int H5T_STD_I64LE = Loader.loadIntValue("H5T_STD_I64LE_g");

    int H5T_STD_I8BE = Loader.loadIntValue("H5T_STD_I8BE_g");

    int H5T_STD_I8LE = Loader.loadIntValue("H5T_STD_I8LE_g");

    int H5T_STD_REF_DSETREG = Loader.loadIntValue("H5T_STD_REF_DSETREG_g");

    int H5T_STD_REF_OBJ = Loader.loadIntValue("H5T_STD_REF_OBJ_g");

    int H5T_STD_U16BE = Loader.loadIntValue("H5T_STD_U16BE_g");

    int H5T_STD_U16LE = Loader.loadIntValue("H5T_STD_U16LE_g");

    int H5T_STD_U32BE = Loader.loadIntValue("H5T_STD_U32BE_g");

    int H5T_STD_U32LE = Loader.loadIntValue("H5T_STD_U32LE_g");

    int H5T_STD_U64BE = Loader.loadIntValue("H5T_STD_U64BE_g");

    int H5T_STD_U64LE = Loader.loadIntValue("H5T_STD_U64LE_g");

    int H5T_STD_U8BE = Loader.loadIntValue("H5T_STD_U8BE_g");

    int H5T_STD_U8LE = Loader.loadIntValue("H5T_STD_U8LE_g");

    int H5T_UNIX_D32BE = Loader.loadIntValue("H5T_UNIX_D32BE_g");

    int H5T_UNIX_D32LE = Loader.loadIntValue("H5T_UNIX_D32LE_g");

    int H5T_UNIX_D64BE = Loader.loadIntValue("H5T_UNIX_D64BE_g");

    int H5T_UNIX_D64LE = Loader.loadIntValue("H5T_UNIX_D64LE_g");

    H5Library INSTANCE = Loader.loadLibrary();

    // String LIBRARY_NAME = "hdf5dll";
    String LIBRARY_NAME = "hdf5ddll";

    int H5Dcreate(int file_id, String name, int type_id, int space_id, int plist_id);

    int H5Dread(int dset_id, int mem_type_id, int mem_space_id, int file_space_id, int plist_id, byte[] buf);

    int H5Dread(int dset_id, int mem_type_id, int mem_space_id, int file_space_id, int plist_id, ByteBuffer buf);

    int H5Fclose(int file_id);

    int H5Fcreate(String filename, int flags, int create_plist, int access_plist);

    int H5Fget_filesize(int file_id, LongByReference size);

    int H5Fget_name(int obj_id, String name, int size);

    int H5Fis_hdf5(String filename);

    int H5Fopen(String filename, int flags, int access_plist);

    int H5Gcreate(int loc_id, String name, int size_hint);

    int H5get_libversion(IntByReference majnum, IntByReference minnum, IntByReference relnum);

    int H5Gopen(int loc_id, String name);

    int H5open();

    int H5Sclose(int space_id);

    int H5Screate(int type);

    int H5Screate_simple(int rank, int[] dims, int[] maxdims);

    int H5Tclose(int type_id);

    int H5Tcommit(int loc_id, String name, int type_id);

    int H5Tcommitted(int type_id);

    int H5Tcopy(int type_id);

    int H5Tcreate(int type, int size);

    int H5Dwrite(int dset_id, int mem_type_id, int mem_space_id, int file_space_id, int plist_id, byte[] buf);

    int H5Dwrite(int dset_id, int mem_type_id, int mem_space_id, int file_space_id, int plist_id, ByteBuffer buf);

    int H5Dclose(int dset_id);
}
