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

import static java.lang.invoke.LambdaForm.*;
import static java.lang.invoke.LambdaForm.BasicType.*;
import static java.lang.invoke.MethodHandleStatics.*;

import java.lang.invoke.LambdaForm.NamedFunction;
import java.lang.invoke.MethodHandles.Lookup;
//import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Function;
import java.util.Map;
import java.util.HashMap;


/**
 * The flavor of method handle which emulates an invoke instruction
 * on a predetermined argument.  The JVM dispatches to the correct method
 * when the handle is created, not when it is invoked.
 *
 * All bound arguments are encapsulated in dedicated species.
 */
/*non-public*/ abstract class BoundMethodHandle extends MethodHandle {

    /*non-public*/ BoundMethodHandle(MethodType type, LambdaForm form) {
        super(type, form);
        assert(speciesData() == speciesData(form));
    }

    //
    // BMH API and internals
    //

    static BoundMethodHandle bindSingle(MethodType type, LambdaForm form, BasicType xtype, Object x) {
        // for some type signatures, there exist pre-defined concrete BMH classes
       /* try {
            switch (xtype) {
            case L_TYPE:
                return bindSingle(type, form, x);  // Use known fast path.
            case I_TYPE:
                return (BoundMethodHandle) SpeciesData.EMPTY.extendWith(I_TYPE).constructor().invokeBasic(type, form, ValueConversions.widenSubword(x));
            case J_TYPE:
                return (BoundMethodHandle) SpeciesData.EMPTY.extendWith(J_TYPE).constructor().invokeBasic(type, form, (long) x);
            case F_TYPE:
                return (BoundMethodHandle) SpeciesData.EMPTY.extendWith(F_TYPE).constructor().invokeBasic(type, form, (float) x);
            case D_TYPE:
                return (BoundMethodHandle) SpeciesData.EMPTY.extendWith(D_TYPE).constructor().invokeBasic(type, form, (double) x);
            default : throw newInternalError("unexpected xtype: " + xtype);
            }
        } catch (Throwable t) {
            throw newInternalError(t);
        }*/
       throw new UnsupportedOperationException("Not supported yet.");
    }

    /*non-public*/
    LambdaFormEditor editor() {
        //return form.editor();
   throw new UnsupportedOperationException("Not supported yet.");     
    }

    static BoundMethodHandle bindSingle(MethodType type, LambdaForm form, Object x) {
        return Species_L.make(type, form, x);
    }

     // there is a default binder in the super class, for 'L' types only
    /*non-public*/
    BoundMethodHandle bindArgumentL(int pos, Object value) {
        return editor().bindArgumentL(this, pos, value);
    }
    /*non-public*/
    BoundMethodHandle bindArgumentI(int pos, int value) {
        return editor().bindArgumentI(this, pos, value);
    }
    /*non-public*/
    BoundMethodHandle bindArgumentJ(int pos, long value) {
        return editor().bindArgumentJ(this, pos, value);
    }
    /*non-public*/
    BoundMethodHandle bindArgumentF(int pos, float value) {
        return editor().bindArgumentF(this, pos, value);
    }
    /*non-public*/
    BoundMethodHandle bindArgumentD(int pos, double value) {
        return editor().bindArgumentD(this, pos, value);
    }

    @Override
    BoundMethodHandle rebind() {
        if (!tooComplex()) {
            return this;
        }
        return makeReinvoker(this);
    }

    private boolean tooComplex() {
       /* return (fieldCount() > FIELD_COUNT_THRESHOLD ||
                form.expressionCount() > FORM_EXPRESSION_THRESHOLD);*/
       throw new UnsupportedOperationException("Not supported yet.");
    }
    private static final int FIELD_COUNT_THRESHOLD = 12;      // largest convenient BMH field count
    private static final int FORM_EXPRESSION_THRESHOLD = 24;  // largest convenient BMH expression count

    /**
     * A reinvoker MH has this form:
     * {@code lambda (bmh, arg*) { thismh = bmh[0]; invokeBasic(thismh, arg*) }}
     */
    static BoundMethodHandle makeReinvoker(MethodHandle target) {
        /*LambdaForm form = DelegatingMethodHandle.makeReinvokerForm(
                target, MethodTypeForm.LF_REBIND,
                Species_L.SPECIES_DATA, Species_L.SPECIES_DATA.getterFunction(0));
        return Species_L.make(target.type(), form, target);*/
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Return the {@link SpeciesData} instance representing this BMH species. All subclasses must provide a
     * static field containing this value, and they must accordingly implement this method.
     */
    /*non-public*/ abstract SpeciesData speciesData();

    /*non-public*/ static SpeciesData speciesData(LambdaForm form) {
        /*Object c = form.names[0].constraint;
        if (c instanceof SpeciesData)
            return (SpeciesData) c;*/
        // if there is no BMH constraint, then use the null constraint
        return SpeciesData.EMPTY;
    }

    /**
     * Return the number of fields in this BMH.  Equivalent to speciesData().fieldCount().
     */
    /*non-public*/ abstract int fieldCount();

    Object internalProperties() {
        return "\n& BMH="+internalValues();
    }

    final Object internalValues() {
        Object[] boundValues = new Object[speciesData().fieldCount()];
        for (int i = 0; i < boundValues.length; ++i) {
            boundValues[i] = arg(i);
        }
        return Arrays.asList(boundValues);
    }

    /*non-public*/ final Object arg(int i) {
        /*try {
            switch (speciesData().fieldType(i)) {
            case L_TYPE: return          speciesData().getters[i].invokeBasic(this);
            //case I_TYPE: return (int)    speciesData().getters[i].invokeBasic(this);
            //case J_TYPE: return (long)   speciesData().getters[i].invokeBasic(this);
            //case F_TYPE: return (float)  speciesData().getters[i].invokeBasic(this);
            //case D_TYPE: return (double) speciesData().getters[i].invokeBasic(this);
            }
        } catch (Throwable ex) {
            throw newInternalError(ex);
        }*/
        throw new InternalError("unexpected type: " + speciesData().typeChars+"."+i);
    }

    //
    // cloning API
    //

    /*non-public*/ abstract BoundMethodHandle copyWith(MethodType mt, LambdaForm lf);
    /*non-public*/ abstract BoundMethodHandle copyWithExtendL(MethodType mt, LambdaForm lf, Object narg);
    /*non-public*/ abstract BoundMethodHandle copyWithExtendI(MethodType mt, LambdaForm lf, int    narg);
    /*non-public*/ abstract BoundMethodHandle copyWithExtendJ(MethodType mt, LambdaForm lf, long   narg);
    /*non-public*/ abstract BoundMethodHandle copyWithExtendF(MethodType mt, LambdaForm lf, float  narg);
    /*non-public*/ abstract BoundMethodHandle copyWithExtendD(MethodType mt, LambdaForm lf, double narg);

    //
    // concrete BMH classes required to close bootstrap loops
    //

    private  // make it private to force users to access the enclosing class first
    static final class Species_L extends BoundMethodHandle {

        private static BoundMethodHandle make(MethodType type, LambdaForm form, Object x) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public Species_L(MethodType type, LambdaForm form) {
            super(type, form);
        }

        @Override
        SpeciesData speciesData() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        int fieldCount() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        BoundMethodHandle copyWith(MethodType mt, LambdaForm lf) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        BoundMethodHandle copyWithExtendL(MethodType mt, LambdaForm lf, Object narg) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        BoundMethodHandle copyWithExtendI(MethodType mt, LambdaForm lf, int narg) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        BoundMethodHandle copyWithExtendJ(MethodType mt, LambdaForm lf, long narg) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        BoundMethodHandle copyWithExtendF(MethodType mt, LambdaForm lf, float narg) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        BoundMethodHandle copyWithExtendD(MethodType mt, LambdaForm lf, double narg) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }

    //
    // BMH species meta-data
    //

    /**
     * Meta-data wrapper for concrete BMH types.
     * Each BMH type corresponds to a given sequence of basic field types (LIJFD).
     * The fields are immutable; their values are fully specified at object construction.
     * Each BMH type supplies an array of getter functions which may be used in lambda forms.
     * A BMH is constructed by cloning a shorter BMH and adding one or more new field values.
     * The shortest possible BMH has zero fields; its class is SimpleMethodHandle.
     * BMH species are not interrelated by subtyping, even though it would appear that
     * a shorter BMH could serve as a supertype of a longer one which extends it.
     */
    static class SpeciesData {

        static SpeciesData EMPTY;
        private String typeChars;

        private SpeciesData(String l, Class<Species_L> aClass) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private NamedFunction getterFunction(int i) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private int fieldCount() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

 

    /**
     * Generation of concrete BMH classes.
     *
     * A concrete BMH species is fit for binding a number of values adhering to a
     * given type pattern. Reference types are erased.
     *
     * BMH species are cached by type pattern.
     *
     * A BMH species has a number of fields with the concrete (possibly erased) types of
     * bound values. Setters are provided as an API in BMH. Getters are exposed as MHs,
     * which can be included as names in lambda forms.
     */
    static class Factory {}

    private static final Lookup LOOKUP = Lookup.IMPL_LOOKUP;

    /**
     * All subclasses must provide such a value describing their type signature.
     */
    static final SpeciesData SPECIES_DATA = SpeciesData.EMPTY;

    private static final SpeciesData[] SPECIES_DATA_CACHE = new SpeciesData[5];
    private static SpeciesData checkCache(int size, String types) {
        int idx = size - 1;
        SpeciesData data = SPECIES_DATA_CACHE[idx];
        if (data != null)  return data;
        SPECIES_DATA_CACHE[idx] = data = null;//getSpeciesData(types);
        return data;
    }
    static SpeciesData speciesData_L()     { return checkCache(1, "L"); }
    static SpeciesData speciesData_LL()    { return checkCache(2, "LL"); }
    static SpeciesData speciesData_LLL()   { return checkCache(3, "LLL"); }
    static SpeciesData speciesData_LLLL()  { return checkCache(4, "LLLL"); }
    static SpeciesData speciesData_LLLLL() { return checkCache(5, "LLLLL"); }
}
