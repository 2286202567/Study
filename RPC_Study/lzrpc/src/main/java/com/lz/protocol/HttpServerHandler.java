package com.lz.protocol;

import com.lz.common.Invocation;
import com.lz.register.LocalRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServerHandler {
    public void handler(HttpServletRequest req, HttpServletResponse resp) {
        //处理请求  -- 》 方法，接口，方法参数
        try {

            //req中传输的是Invocation对象
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();
            //获取对应的接口
            String interfaceName = invocation.getInterfaceName();
            //根据接口找到对应的实现类
            Class aClass = new LocalRegister().get(interfaceName, "1.0");
            //获取对应实现类的方法
            Method method = aClass.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            String result = (String) method.invoke(aClass.newInstance(), invocation.getParameters());
            IOUtils.write(result, resp.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
