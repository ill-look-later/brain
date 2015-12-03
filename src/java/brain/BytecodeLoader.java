
package brain;

import java.lang.reflect.Method;

/**
 * Loads bytecode into classes.
 */
public class BytecodeLoader extends ClassLoader {
    /**
     * Converts the given bytecode to a class object.
     */
    static public Class loadBytecode(byte[] code) {
        BytecodeLoader loader = new BytecodeLoader();
        return loader.defineClass(null, code, 0, code.length);
    }

    /**
     * Calls the main method of the given class object.
     */
    static public void callMain(Class cls) {
        try {
            Method method = cls.getMethod("main", String[].class);

            Object[] args = new Object[1];
            args[0] = new String[] {};
            method.invoke(null, args);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
