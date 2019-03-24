package demo.intercept;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;
import profiler.Profiler;

@Component
public class CommonMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Class clazz = invocation.getMethod().getDeclaringClass();
        String method = invocation.getMethod().getName();
        String mark = clazz.getCanonicalName() + "#" + method;
        // 调用被拦截的方法前，植入入口埋点
        Profiler.enter(mark);
        try {
            // 拦截器调用被拦截的方法
            return invocation.proceed();
        } finally {
            // 调用被拦截的方法后，植入出口埋点
            Profiler.exit();
        }

    }

}
