package net.lunglet.matlab;

import com.sun.jna.NativeLong;

public interface MXConstants {
    int CELL_CLASS = 1;

    int CHAR_CLASS = 4;

    int COMPLEX = 1;

    int DOUBLE_CLASS = 6;

    int FUNCTION_CLASS = 16;

    int INT16_CLASS = 10;

    int INT32_CLASS = 12;

    int INT64_CLASS = 14;

    int INT8_CLASS = 8;

    int LOGICAL_CLASS = 3;

    int OBJECT_CLASS = 18;

    int OPAQUE_CLASS = 17;

    int REAL = 0;

    int SINGLE_CLASS = 7;

    int STRUCT_CLASS = 2;

    int UINT16_CLASS = 11;

    int UINT32_CLASS = 13;

    int UINT64_CLASS = 15;

    int UINT8_CLASS = 9;

    int UNKNOWN_CLASS = 0;

    int VOID_CLASS = 5;

    int INDEX_CLASS = NativeLong.SIZE == 4 ? UINT32_CLASS : UINT64_CLASS;
}
