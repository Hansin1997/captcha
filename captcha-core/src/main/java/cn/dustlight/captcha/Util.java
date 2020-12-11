package cn.dustlight.captcha;

import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {

    public static <T> T getBean(BeanFactory factory, String name, Class<? extends T> clazz) {
        return factory.getBean(name, clazz);
    }

    public static Map<String, Object> getParameters(Method method, Object[] arguments) {
        Map<String, Object> parameters = new LinkedHashMap<>();
        if (method.getParameterCount() > 0) {
            Parameter[] parameters1 = method.getParameters();
            for (int i = 0, len = Math.min(arguments.length, parameters1.length); i < len; i++)
                parameters.put(parameters1[i].getName(), arguments[i]);
        }
        return parameters;
    }
}
