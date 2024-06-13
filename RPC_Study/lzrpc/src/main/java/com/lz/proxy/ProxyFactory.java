package com.lz.proxy;

import com.lz.common.Invocation;
import com.lz.common.URL;
import com.lz.loadbalance.LoadBalance;
import com.lz.protocol.HttpClient;
import com.lz.register.MapRemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyFactory {
    public static <T> T getProxy(Class interfaceClass) {
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //服务Mock   不进入逻辑直接返回
                String mock = System.getProperty("mock");
                if(mock!=null && mock.startsWith("return:")){
                    String result = mock.replace("return:","");
                }


                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(), method.getParameterTypes(), args);
                //服务发现
                String className = interfaceClass.getName();
                //利用服务名称找到对应的 所有服务
                List<URL> urlList = MapRemoteRegister.get(className);

                //服务重试
                List<URL> invokedUrlList = new ArrayList<>();
                int  max= 2;
                String result = null;
                while(max>0){
                    //当服务调用失败  应该把服务从服务列表中移除
                    urlList.removeAll(invokedUrlList);
                    //很多服务列表   需要用到负载均衡
                    //随机获取一个
                    URL randomUrl = LoadBalance.random(urlList);
                    invokedUrlList.add(randomUrl);
                    //服务调用
                    try {
                        result = new HttpClient().send(randomUrl.getHostName(), randomUrl.getPort(), invocation);
                    } catch (Exception e) {
                        //当
                        if(max-- != 0)  continue;

                        //执行容错机制    error-callback
                        return "服务报错了";
                    }
                    return result;
                }
                return result;

            }
        });
        return (T) proxyInstance;
    }
}
