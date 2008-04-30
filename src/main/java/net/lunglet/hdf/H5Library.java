package net.lunglet.hdf;

import com.sun.jna.Callback;
import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.NativeLongByReference;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO make an H5Constants interface that contains static and DLL constants

// TODO look at opening of absolute names from arbitrary groups or datasets

public interface H5Library extends Library {
    static class H5E_error_t extends Structure implements ByReference {
        /** Major number (first field). */
        public int maj_num;

        /** Minor number (second field). */
        public int min_num;

        /** Function name (third field). */
        public String func_name;

        /** File name (fourth field). */
        public String file_name;

        /** Line number (fifth field). */
        public int line;

        /** Description (sixth field). */
        public String desc;
    }

    interface H5E_walk_t extends Callback {
        int callback(int n, H5E_error_t err_desc, Pointer client_data);
    }

    interface H5G_iterate_t extends Callback {
        int callback(int locId, String name, Pointer data);
    }

    static final class Loader {
        private static int loadIntValue(final String name) {
            int err = loadLibrary().H5open();
            if (err < 0) {
                throw new H5Exception();
            }
            return NativeLibrary.getInstance(LIBRARY_NAME).getFunction(name).getInt(0);
        }

        private static H5Library loadLibrary() {
            Map<String, TypeMapper> options = new HashMap<String, TypeMapper>();
            DefaultTypeMapper mapper = new DefaultTypeMapper();
            mapper.addToNativeConverter(long[][].class, new ToNativeConverter() {
                public Class<LongArrays> nativeType() {
                    return LongArrays.class;
                }

                public Object toNative(final Object obj, final ToNativeContext context) {
                    return new LongArrays((long[][]) obj);
                }
            });
            options.put(Library.OPTION_TYPE_MAPPER, mapper);
            // TODO use type mapper at some stage
            H5Library lib = (H5Library) Native.loadLibrary(LIBRARY_NAME, H5Library.class);
            // Disable automatic printing of errors
            lib.H5Eset_auto(null, null);
            return lib;
        }

        private Loader() {
        }
    }

    static final class LongArrays extends Memory {
        private final List<Memory> buffers;

        public LongArrays(final long[][] arrs) {
            super(arrs.length * Pointer.SIZE);
            this.buffers = new ArrayList<Memory>(arrs.length);
            for (int i = 0; i < arrs.length; i++) {
                Memory buf = new Memory(arrs[i].length * LONG_BYTES);
                buf.getByteBuffer(0, buf.getSize()).asLongBuffer().put(arrs[i]);
                buffers.add(buf);
                setPointer(i * Pointer.SIZE, buf);
            }
        }
    }

    /**
     * The number of bytes used to represent a <tt>double</tt> value.
     */
    int DOUBLE_BYTES = Double.SIZE >>> 3;

    /**
     * The number of bytes used to represent a <tt>float</tt> value.
     */
    int FLOAT_BYTES = Float.SIZE >>> 3;

    /** Begin error stack traversal at API function, end deep. */
    int H5E_WALK_DOWNWARD = 1;

    /** Begin error stack traversal deep, end at API function. */
    int H5E_WALK_UPWARD = 0;

    int H5G_DATASET = 2;

    int H5G_GROUP = 1;

    int H5G_LINK = 0;

    int H5G_TYPE = 3;

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

    int H5S_ALL = 0;

    int H5S_UNLIMITED = -1;

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

    int INTEGER_BYTES = Integer.SIZE >>> 3;

    String LIBRARY_NAME = "hdf5";

    int LONG_BYTES = Long.SIZE >>> 3;

    int H5Aclose(int attr_id);

    int H5Acreate(int loc_id, String name, int type_id, int space_id, int create_plist);

    /**
     * <CODE>ssize_t H5Aget_name(hid_t attr_id, size_t buf_size, char *buf)</CODE>
     */
    NativeLong H5Aget_name(int attr_id, NativeLong buf_size, byte[] buf);

    int H5Aget_space(int attr_id);

    int H5Aget_type(int attr_id);

    int H5Aopen_name(int loc_id, String name);

    int H5Aread(int attr_id, int mem_type_id, Buffer buf);

    int H5Awrite(int attr_id, int mem_type_id, Buffer buf);

    int H5Dclose(int dset_id);

    int H5Dcreate(int file_id, String name, int type_id, int space_id, int plist_id);

    int H5Dget_space(int dset_id);

    /**
     * <CODE>hsize_t H5Dget_storage_size(hid_t dataset_id)</CODE>
     */
    long H5Dget_storage_size(int dset_id);

    int H5Dget_type(int dataset_id);

    int H5Dopen(int loc_id, String name);

    int H5Dread(int dset_id, int mem_type_id, int mem_space_id, int file_space_id, int plist_id, Buffer buf);

    int H5Dwrite(int dset_id, int mem_type_id, int mem_space_id, int file_space_id, int plist_id, Buffer buf);

    /**
     * <CODE>const char *H5Eget_major(H5E_major_t major_number)</CODE>
     */
    String H5Eget_major(int major_number);

    /**
     * <CODE>const char *H5Eget_minor(H5E_minor_t minor_number)</CODE>
     */
    String H5Eget_minor(int minor_number);

    int H5Eset_auto(Pointer func, Pointer client_data);

    /**
     * <CODE>herr_t H5Ewalk(H5E_direction_t direction, H5E_walk_t func, void* client_data)</CODE>
     */
    int H5Ewalk(int direction, H5E_walk_t func, Pointer client_data);

    int H5Fclose(int file_id);

    int H5Fcreate(String filename, int flags, int create_plist, int access_plist);

    /**
     * <CODE>herr_t H5Fflush(hid_t object_id, H5F_scope_t scope)</CODE>
     */
    int H5Fflush(int object_id, int scope);

    int H5Fget_access_plist(int file_id);

    int H5Fget_create_plist(int file_id);

    /**
     * <CODE>herr_t H5Fget_filesize(hid_t file_id, hsize_t *size)</CODE>
     */
    int H5Fget_filesize(int file_id, LongByReference size);

    /**
     * <CODE>hssize_t H5Fget_freespace(hid_t file_id)</CODE>
     */
    long H5Fget_freespace(int file_id);

    /**
     * <CODE>ssize_t H5Fget_name(hid_t obj_id, char *name, size_t size)</CODE>
     */
    NativeLong H5Fget_name(int obj_id, byte[] name, NativeLong size);

    int H5Fis_hdf5(String filename);

    int H5Fopen(String filename, int flags, int access_plist);

    int H5garbage_collect();

    int H5Gclose(int group_id);

    /**
     * <CODE>hid_t H5Gcreate(hid_t loc_id, const char *name, size_t size_hint)</CODE>
     */
    int H5Gcreate(int loc_id, String name, NativeLong size_hint);

    int H5get_libversion(IntByReference majnum, IntByReference minnum, IntByReference relnum);

    /**
     * <CODE>herr_t H5Gget_num_objs(hid_t loc_id, hsize_t* num_obj)</CODE>
     */
    int H5Gget_num_objs(int loc_id, LongByReference num_objs);

    /**
     * <CODE>int H5Gget_objtype_by_idx(hid_t loc_id, hsize_t idx)</CODE>
     */
    int H5Gget_objtype_by_idx(int loc_id, long idx);

    /**
     * <CODE>int H5Giterate(hid_t loc_id, const char *name, int *idx, H5G_iterate_t operator, void *operator_data)</CODE>
     */
    int H5Giterate(int loc_id, String name, IntByReference idx, H5G_iterate_t operator, Pointer operator_data);

    int H5Gopen(int loc_id, String name);

    /**
     * <CODE>ssize_t H5Iget_name(hid_t obj_id, char *name, size_t size)</CODE>
     */
    NativeLong H5Iget_name(int obj_id, byte[] name, NativeLong size);

    int H5Iget_type(int obj_id);

    int H5open();

    int H5Pclose(int plist);

    int H5Pcreate(int cls_id);

    /**
     * <CODE>herr_t H5Pget_cache(hid_t plist_id, int *mdc_nelmts, int *rdcc_nelmts, size_t *rdcc_nbytes, double *rdcc_w0)</CODE>
     */
    int H5Pget_cache(int plist_id, IntByReference mdc_nelmts, IntByReference rdcc_nelmts,
            NativeLongByReference rdcc_nbytes, DoubleByReference rdcc_w);

    /**
     * <CODE>herr_t H5Pset_cache(hid_t plist_id, int mdc_nelmts, int rdcc_nelmts, size_t rdcc_nbytes, double rdcc_w0)</CODE>
     */
    int H5Pset_cache(int plist_id, int mdc_nelmts, int rdcc_nelmts, NativeLong rdcc_nbytes, double rdcc_w);

    /**
     * <CODE>herr_t H5Pset_fapl_core(hid_t fapl_id, size_t increment, hbool_t backing_store)</CODE>
     */
    int H5Pset_fapl_core(int fapl_id, NativeLong increment, int backing_store);

    int H5Pset_fapl_sec2(int fapl_id);

    /**
     * <CODE>herr_t H5Pset_fill_time(hid_t plist_id, H5D_fill_time_t fill_time)</CODE>
     */
    int H5Pset_fill_time(int plist_id, int fill_time);

    /**
     * <CODE>herr_t H5Pset_meta_block_size(hid_t fapl_id, hsize_t size)</CODE>
     */
    int H5Pset_meta_block_size(int fapl_id, long size);

    /**
     * <CODE>herr_t H5Pset_sieve_buf_size(hid_t fapl_id, hsize_t size)</CODE>
     */
    int H5Pset_sieve_buf_size(int fapl_id, long size);

    int H5Sclose(int space_id);

    int H5Screate(int type);

    /**
     * <CODE>hid_t H5Screate_simple(int rank, const hsize_t * dims, const hsize_t * maxdims)</CODE>
     */
    int H5Screate_simple(int rank, long[] dims, long[] maxdims);

    int H5Sget_select_bounds(int space_id, long[] start, long[] end);

    int H5Sget_select_elem_pointlist(int space_id, long startpoint, long numpoints, long[] buf);

    int H5Sget_select_hyper_blocklist(int space_id, long startblock, long numblocks, long[] buf);

    long H5Sget_select_hyper_nblocks(int space_id);

    long H5Sget_select_npoints(int space_id);

    int H5Sget_select_type(int space_id);

    /**
     * <CODE>int H5Sget_simple_extent_dims(hid_t space_id, hsize_t *dims, hsize_t *maxdims)</CODE>
     */
    int H5Sget_simple_extent_dims(int space_id, long[] dims, long[] maxdims);

    int H5Sget_simple_extent_ndims(int space_id);

    long H5Sget_simple_extent_npoints(int space_id);

    int H5Sis_simple(int space_id);

    /**
     * <CODE>herr_t H5Soffset_simple(hid_t space_id, const hssize_t *offset)</CODE>
     */
    int H5Soffset_simple(int space_id, long[] offset);

    int H5Sselect_all(int space_id);

    /**
     * <CODE>herr_t H5Sselect_elements(hid_t space_id, H5S_seloper_t op, const size_t num_elements, const hsize_t *coord[])</CODE>
     */
    int H5Sselect_elements(int space_id, int op, NativeLong num_elements, Buffer coord);

    int H5Sselect_hyperslab(int space_id, int op, long[] start, long[] stride, long[] count, long[] block);

    int H5Sselect_none(int space_id);

    int H5Sselect_valid(int space_id);

    int H5Tclose(int type_id);

    int H5Tcommit(int loc_id, String name, int type_id);

    int H5Tcommitted(int type_id);

    int H5Tcopy(int type_id);

    /**
     * <CODE>hid_t H5Tcreate(H5T_class_t class, size_t size)</CODE>
     */
    int H5Tcreate(int type, NativeLong size);

    int H5Tequal(int type_id1, int type_id2);

    int H5Tget_class(int type_id);

    /**
     * <CODE>size_t H5Tget_size(hid_t type_id)</CODE>
     */
    NativeLong H5Tget_size(int type_id);

    /**
     * <CODE>herr_t H5Tset_size(hid_t type_id, size_t size)</CODE>
     */
    int H5Tset_size(int type_id, NativeLong size);

    /**
     * <CODE>herr_t H5Zfilter_avail(H5Z_filter_t filter)</CODE>
     */
    int H5Zfilter_avail(int filter);
}
