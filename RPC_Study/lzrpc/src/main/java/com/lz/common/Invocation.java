package com.lz.common;

import java.io.Serializable;

public class Invocation implements Serializable {
    private String InterfaceName;
    private String  methodName;
    private Class[] parameterTypes;
    private Object[] parameters;
    //private String version;  多版本

    public Invocation(String interfaceName, String methodName, Class[] parameterTypes, Object[] parameters) {
        InterfaceName = interfaceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    public String getInterfaceName() {
        return InterfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setInterfaceName(String interfaceName) {
        InterfaceName = interfaceName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

}
