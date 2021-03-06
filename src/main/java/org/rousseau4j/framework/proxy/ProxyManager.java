package org.rousseau4j.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.rousseau4j.framework.annotation.AspectSkip;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by ZhouHangqi on 2017/12/17.
 */
public class ProxyManager {

    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                if (targetClass.isAnnotationPresent(AspectSkip.class) || method.isAnnotationPresent(AspectSkip.class)) {
                    return methodProxy.invoke(o, objects);
                }
                return new ProxyChain(targetClass, o, method, methodProxy, objects, proxyList).doProxyChain();
            }
        });

    }
}
