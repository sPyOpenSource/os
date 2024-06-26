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

/**
 * The flavor of method handle which implements a constant reference
 * to a class member.
 * @author jrose
 */
class DirectMethodHandle extends MethodHandle {
    //final MemberName member;
    
    // Constructors and factory methods in this class *must* be package scoped or private.
    private DirectMethodHandle(MethodType mtype, LambdaForm form, MemberName member) {
        super(mtype, form);
       
    }

    @Override
    MethodHandle copyWith(MethodType mt, LambdaForm lf) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    BoundMethodHandle rebind() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   
}
