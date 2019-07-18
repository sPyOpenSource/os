/*
 * Copyright (c) 2008, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang.invoke;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

import static java.lang.invoke.LambdaForm.*;
import static java.lang.invoke.MethodHandleStatics.*;
//import static java.lang.invoke.MethodHandles.Lookup.IMPL_LOOKUP;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 * Trusted implementation code for MethodHandle.
 * @author jrose
 */
/*non-public*/ abstract class MethodHandleImpl {

    static void initStatics() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // Do not adjust this except for special platforms:
    
}
