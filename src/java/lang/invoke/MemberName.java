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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
//import static java.lang.invoke.MethodHandleNatives.Constants.*;
import static java.lang.invoke.MethodHandleStatics.*;
import java.util.Objects;

/**
 * A {@code MemberName} is a compact symbolic datum which fully characterizes
 * a method or field reference.
 * A member name refers to a field, method, constructor, or member type.
 * Every member name has a simple name (a string) and a type (either a Class or MethodType).
 * A member name may also have a non-null declaring class, or it may be simply
 * a naked name/type pair.
 * A member name may also have non-zero modifier flags.
 * Finally, a member name may be either resolved or unresolved.
 * If it is resolved, the existence of the named
 * <p>
 * Whether resolved or not, a member name provides no access rights or
 * invocation capability to its possessor.  It is merely a compact
 * representation of all symbolic information necessary to link to
 * and properly use the named member.
 * <p>
 * When resolved, a member name's internal implementation may include references to JVM metadata.
 * This representation is stateless and only decriptive.
 * It provides no private information and no capability to use the member.
 * <p>
 * By contrast, a {@linkplain java.lang.reflect.Method} contains fuller information
 * about the internals of a method (except its bytecodes) and also
 * allows invocation.  A MemberName is much lighter than a Method,
 * since it contains about 7 fields to the 16 of Method (plus its sub-arrays),
 * and those seven fields omit much of the information in Method.
 * @author jrose
 */
/*non-public*/ final class MemberName implements Member, Cloneable {

    MemberName(Method method, boolean wantSpecial) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    MemberName(Constructor constructor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Class<?> getDeclaringClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getModifiers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSynthetic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    MethodType getMethodOrFieldType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isMethodHandleInvoke() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isVarargs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isStatic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isPrivate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isInvocable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isCallerSensitive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

//    static {
//        System.out.println("Hello world!  My methods are:");
//        System.out.println(Factory.INSTANCE.getMethods(MemberName.class, true, null));
//    }

