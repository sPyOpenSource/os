/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.lang.invoke;

/**
 *
 * @author spy
 */
public class CallSite {

    static CallSite makeSite(MethodHandle bootstrapMethod, String name, MethodType type, Object staticArguments, Class<?> caller) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    MethodHandle target;

    Object dynamicInvoker() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * Make a blank call site object with the given method type.
     * An initial target method is supplied which will throw
     * an {@link IllegalStateException} if called.
     * <p>
     * Before this {@code CallSite} object is returned from a bootstrap method,
     * it is usually provided with a more useful target method,
     * via a call to {@link CallSite#setTarget(MethodHandle) setTarget}.
     * @throws NullPointerException if the proposed type is null
     */
    /*package-private*/
    CallSite(MethodType type) {
        //target = makeUninitializedCallSite(type);
    }

    /**
     * Make a call site object equipped with an initial target method handle.
     * @param target the method handle which will be the initial target of the call site
     * @throws NullPointerException if the proposed target is null
     */
    /*package-private*/
    CallSite(MethodHandle target) {
        target.type();  // null check
        this.target = target;
    }

    /**
     * Make a call site object equipped with an initial target method handle.
     * @param targetType the desired type of the call site
     * @param createTargetHook a hook which will bind the call site to the target method handle
     * @throws WrongMethodTypeException if the hook cannot be invoked on the required arguments,
     *         or if the target returned by the hook is not of the given {@code targetType}
     * @throws NullPointerException if the hook returns a null value
     * @throws ClassCastException if the hook returns something other than a {@code MethodHandle}
     * @throws Throwable anything else thrown by the hook function
     */
    /*package-private*/
    CallSite(MethodType targetType, MethodHandle createTargetHook) throws Throwable {
        this(targetType);
        ConstantCallSite selfCCS = (ConstantCallSite) this;
        MethodHandle boundTarget = null;//(MethodHandle) createTargetHook.invokeWithArguments(selfCCS);
        checkTargetChange(this.target, boundTarget);
        this.target = boundTarget;
    }

    private void checkTargetChange(MethodHandle target, MethodHandle boundTarget) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
