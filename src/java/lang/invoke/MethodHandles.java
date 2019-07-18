/*
 * Copyright (c) 2008, 2016, Oracle and/or its affiliates. All rights reserved.
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

import java.lang.reflect.*;
import java.util.BitSet;
import java.util.List;
import java.util.Arrays;

import static java.lang.invoke.MethodHandleStatics.*;
//import static java.lang.invoke.MethodHandleImpl.Intrinsic;
//import static java.lang.invoke.MethodHandleNatives.Constants.*;


/**
 * This class consists exclusively of static methods that operate on or return
 * method handles. They fall into several categories:
 * <ul>
 * <li>Lookup methods which help create method handles for methods and fields.
 * <li>Combinator methods, which combine or transform pre-existing method handles into new ones.
 * <li>Other factory methods to create method handles that emulate other common JVM operations or control flow patterns.
 * </ul>
 * <p>
 * @author John Rose, JSR 292 EG
 * @since 1.7
 */
public class MethodHandles {

    static MethodHandle dropArguments(MethodHandle mh, int i, List<Class<?>> subList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static MethodHandle identity(Class<Object> aClass) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private MethodHandles() { }  // do not instantiate

    static class Lookup {

        static Lookup IMPL_LOOKUP;
        static int PRIVATE;

        public Lookup() {
        }

        MethodHandle findStatic(Class<? extends BoundMethodHandle> cbmh, String make, MethodType fromMethodDescriptorString) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        int lookupModes() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        Object[] lookupClass() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

   
}
