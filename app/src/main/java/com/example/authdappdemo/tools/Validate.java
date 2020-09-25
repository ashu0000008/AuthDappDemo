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

class Validate {

    static void isTrue(final boolean expression, final String message, final Object... values) {
        if (expression == false) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
}
