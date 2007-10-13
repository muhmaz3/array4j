package net.lunglet.matlab;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import java.nio.ByteBuffer;

public interface EngineLibrary extends Library {
    EngineLibrary INSTANCE = (EngineLibrary) Native.loadLibrary("libeng", EngineLibrary.class);

    /**
     * Close down matlab server
     *
     * @param ep
     *                engine pointer
     */
    int engClose(Engine ep);

    /**
     * Execute matlab statement.
     *
     * @param ep
     *                engine pointer
     * @param string
     *                string for matlab to execute
     */
    int engEvalString(Engine ep, String string);

    /**
     * Get a variable with the specified name from MATLAB's workspace.
     *
     * @param ep
     *                engine pointer
     * @param name
     *                name of variable to get
     */
    MXArray engGetVariable(Engine ep, String name);

    int engGetVisible(Engine ep, ByteByReference bVal);

    /**
     * Start matlab process.
     *
     * @param startcmd
     *                exec command string used to start matlab
     * @return
     */
    Engine engOpen(String startcmd);

    /**
     * Start matlab process for single use.
     * <p>
     * Not currently supported on UNIX.
     *
     * @param startcmd
     *                exec command string used to start matlab
     * @param reserved
     *                reserved for future use, must be NULL
     * @param retstatus
     *                return status
     */
    Engine engOpenSingleUse(String startcmd, Pointer reserved, IntByReference retstatus);

    /**
     * Register a buffer to hold matlab text output.
     *
     * @param engine
     *                pointer
     * @param buffer
     *                character array to hold output
     * @param buflen
     *                buffer array length
     */
    int engOutputBuffer(Engine ep, ByteBuffer buffer, int buflen);

    /**
     * Put a variable into MATLAB's workspace with the specified name.
     *
     * @param ep
     *                engine pointer
     * @param name
     *                name of variable to put
     * @param ap
     *                array pointer
     */
    int engPutVariable(Engine ep, String name, MXArray ap);

    int engSetVisible(Engine ep, boolean newVal);
}
