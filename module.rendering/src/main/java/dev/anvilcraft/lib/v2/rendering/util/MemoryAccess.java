package dev.anvilcraft.lib.v2.rendering.util;

import org.lwjgl.system.Pointer;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/// @author IMS212
@SuppressWarnings("removal")
public class MemoryAccess {
    private static final Unsafe UNSAFE = getUnsafe();
    private static final boolean BITS32 = Pointer.BITS32;
    
    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void putInt(long address, int value) {
        UNSAFE.putInt(address, value);
    }

    public static void putFloat(long address, float value) {
        UNSAFE.putFloat(address, value);
    }

    public static void putLong(long address, long value) {
        UNSAFE.putLong(address, value);
    }

    public static void putShort(long address, short value) {
        UNSAFE.putShort(address, value);
    }

    public static void putByte(long address, byte b) {
        UNSAFE.putByte(address, b);
    }

    public static int getInt(long address) {
        return UNSAFE.getInt(address);
    }

    public static float getFloat(long address) {
        return UNSAFE.getFloat(address);
    }

    public static long getLong(long address) {
        return UNSAFE.getLong(address);
    }

    public static short getShort(long address) {
        return UNSAFE.getShort(address);
    }

    public static byte getByte(long address) {
        return UNSAFE.getByte(address);
    }

    public static void putAddress(long address, long value) {
        if (BITS32) {
            UNSAFE.putInt(address, (int) value);
        } else {
            UNSAFE.putLong(address, value);
        }
    }

    public static long getAddress(long address) {
        if (BITS32) {
            return UNSAFE.getInt(address) & 0xFFFF_FFFFL;
        } else {
            return UNSAFE.getLong(address);
        }
    }
}