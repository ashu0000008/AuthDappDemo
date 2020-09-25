/*
 * ===========================================================================================
 *    COPYRIGHT
 *             PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *      This software is supplied under the terms of a license agreement or nondisclosure
 *      agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *      disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2018 - ? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 *    Description: // Detail description about the function of this module,
 *                // interfaces with the other modules, and dependencies.
 *    Revision History:
 *    Date	                 Author	                Action
 *    2018-12-06   	         Sim.G                  Create
 *   ===========================================================================================
 */
package com.example.authdappdemo.tools;

import java.lang.reflect.InvocationTargetException;


public class SystemPropertiesCompat {

    private static Class<?> sClass;

    private static Class getMyClass() throws ClassNotFoundException {
        if (sClass == null) {
            sClass = Class.forName("android.os.SystemProperties");
        }
        return sClass;
    }

    private static String getInner(String key, String defaultValue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Class clazz = getMyClass();
        return (String) MethodUtils.invokeStaticMethod(clazz, "get", key, defaultValue);
    }

    public static String get(String key, String defaultValue) {
        try {
            return getInner(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static String setInner(String key, String defaultValue) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class clazz = getMyClass();
        return (String) MethodUtils.invokeStaticMethod(clazz, "set", key, defaultValue);
    }

    public static void set(String key, String defaultValue) {
        try {
            setInner(key, defaultValue);
        } catch (Exception e) {
        }
    }

}
