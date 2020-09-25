package com.example.authdappdemo.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SystemProperties 兼容类
 * 请不要使用隐藏的SystemProperties API
 * <p/>
 * 这样使得编译变得麻烦, 也没有必要
 * <p/>
 * Gives access to the system properties store.  The system properties
 * store contains a list of string key-value pairs.
 *
 * @author weishu
 * @date 16/2/23
 */
public class SystemPropertiesCompat2 {

//    private static final String SYSTEM_PROPERTIES_CLASS_NAME = "android.os.SystemProperties";
//
//    private static Class<?> sClazz;
//
//    /**
//     * Get the value for the given key.
//     *
//     * @return an empty string if the key isn't found
//     * @throws IllegalArgumentException if the key exceeds 32 characters
//     */
//    public static String get(String key) {
//        return invoke("get", String.class, new Class[] { String.class }, new Object[] { key });
//    }
//
//    /**
//     * Get the value for the given key.
//     *
//     * @return if the key isn't found, return def if it isn't null, or an empty string otherwise
//     * @throws IllegalArgumentException if the key exceeds 32 characters
//     */
//    public static String get(String key, String fallback) {
//        // if invoke failed, we must return the fallback value
//        String medium = invoke("get", String.class, new Class[] { String.class, String.class },
//                new Object[] { key, fallback });
//        return medium == null ? fallback : medium;
//    }
//
//    /**
//     * Get the value for the given key, and return as an integer.
//     *
//     * @param key the key to lookup
//     * @param def a default value to return
//     * @return the key parsed as an integer, or def if the key isn't found or
//     * cannot be parsed
//     * @throws IllegalArgumentException if the key exceeds 32 characters
//     */
//    public static int getInt(String key, int def) {
//        Integer medium = invoke("getInt", int.class, new Class[] { String.class, int.class },
//                new Object[] { key, def });
//        return medium == null ? def : medium;
//    }
//
//    /**
//     * Get the value for the given key, and return as a long.
//     *
//     * @param key the key to lookup
//     * @param def a default value to return
//     * @return the key parsed as a long, or def if the key isn't found or
//     * cannot be parsed
//     * @throws IllegalArgumentException if the key exceeds 32 characters
//     */
//    public static long getLong(String key, long def) {
//        Long medium = invoke("getLong", long.class, new Class[] { String.class, long.class },
//                new Object[] { key, def });
//        return medium == null ? def : medium;
//    }
//
//    /**
//     * Get the value for the given key, returned as a boolean.
//     * Values 'n', 'no', '0', 'false' or 'off' are considered false.
//     * Values 'y', 'yes', '1', 'true' or 'on' are considered true.
//     * (case sensitive).
//     * If the key does not exist, or has any other value, then the default
//     * result is returned.
//     *
//     * @param key the key to lookup
//     * @param def a default value to return
//     * @return the key parsed as a boolean, or def if the key isn't found or is
//     * not able to be parsed as a boolean.
//     * @throws IllegalArgumentException if the key exceeds 32 characters
//     */
//    public static boolean getBoolean(String key, boolean def) {
//        Boolean medium = invoke("getBoolean", boolean.class, new Class[] { String.class, boolean.class },
//                new Object[] { key, def });
//        return medium == null ? def : medium;
//    }
//
//    /**
//     * Set the value for the given key.
//     *
//     * @throws IllegalArgumentException if the key exceeds 32 characters
//     * @throws IllegalArgumentException if the value exceeds 92 characters
//     */
//    public static void set(String key, String val) {
//        invoke("set", void.class, new Class[] { String.class, String.class },
//                new Object[] { key, val });
//    }
//
//    public static void addChangeCallback(Runnable callback) {
//        invoke("addChangeCallback", void.class, new Class[] { Runnable.class }, new Object[] { callback });
//    }
//
//    /**
//     * internal implementations
//     *
//     * @param methodName
//     * @param returnType
//     * @param argTypes
//     * @param args
//     * @param <T>
//     * @return
//     */
//    private static <T> T invoke(String methodName, Class<T> returnType, Class<?>[] argTypes, Object[] args) {
//
//        if (sClazz == null) {
//            try {
//                sClazz = Class.forName(SYSTEM_PROPERTIES_CLASS_NAME);
//            } catch (ClassNotFoundException e) {
//                // no SystemProperties class??
//                // wtf
//                return null;
//            }
//        }
//
//        try {
//            Method method = sClazz.getDeclaredMethod(methodName, argTypes);
//            method.setAccessible(true);
//            Object returnValue = method.invoke(null, args);
//            return returnType.cast(returnValue);
//        } catch (NoSuchMethodException e) {
//            // wtf
//            return null;
//        } catch (InvocationTargetException e) {
//            // wtf
//            return null;
//        } catch (IllegalAccessException e) {
//            // wtf
//            return null;
//        }
//
//    }
}