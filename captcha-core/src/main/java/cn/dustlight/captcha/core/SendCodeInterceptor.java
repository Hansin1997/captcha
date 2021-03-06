package cn.dustlight.captcha.core;

import cn.dustlight.captcha.Util;
import cn.dustlight.captcha.annotations.CodeParam;
import cn.dustlight.captcha.annotations.CodeValue;
import cn.dustlight.captcha.annotations.SendCode;
import cn.dustlight.captcha.configurations.DefaultBeanProperties;
import cn.dustlight.captcha.generator.CodeGenerator;
import cn.dustlight.captcha.sender.CodeSender;
import cn.dustlight.captcha.store.CodeStore;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Vector;

public class SendCodeInterceptor implements MethodBeforeAdvice, Ordered {

    private BeanFactory factory;
    private int order;
    private DefaultBeanProperties defaultBeanProperties;

    public SendCodeInterceptor(DefaultBeanProperties defaultBeanProperties) {
        this.defaultBeanProperties = defaultBeanProperties;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        SendCode sendCodeAnnotation = AnnotationUtils.findAnnotation(method, SendCode.class); // 获取注解
        /*
         * 获取Bean
         */
        CodeGenerator generator = !StringUtils.hasText(sendCodeAnnotation.generator().value()) ?
                Util.getBean(factory, defaultBeanProperties.getGenerator().getName(), defaultBeanProperties.getGenerator().getType()) :
                Util.getBean(factory, sendCodeAnnotation.generator().value(), sendCodeAnnotation.generator().type());
        CodeStore store = !StringUtils.hasText(sendCodeAnnotation.store().value()) ?
                Util.getBean(factory, defaultBeanProperties.getStore().getName(), defaultBeanProperties.getStore().getType()) :
                Util.getBean(factory, sendCodeAnnotation.store().value(), sendCodeAnnotation.store().type());
        CodeSender sender = !StringUtils.hasText(sendCodeAnnotation.sender().value()) ?
                Util.getBean(factory, defaultBeanProperties.getSender().getName(), defaultBeanProperties.getSender().getType()) :
                Util.getBean(factory, sendCodeAnnotation.sender().value(), sendCodeAnnotation.sender().type());

        Map<String, Object> parameters = Util.getParameters(method, objects); // 获取方法参数列表
        for (cn.dustlight.captcha.annotations.Parameter parameter : sendCodeAnnotation.parameters())
            parameters.put(parameter.name(), parameter.value()); // 获取注解参数

        Code code = generator.generate(sendCodeAnnotation.value(), parameters); // 生成验证码
        Object codeValue = code.getValue();

        Parameter[] params = method.getParameters();
        if (params != null && objects != null) {
            for (int i = 0, len = Math.min(params.length, objects.length); i < len; i++) {
                CodeParam codeParamAnnotation;
                CodeValue codeValueAnnotation;
                if ((codeValueAnnotation = AnnotationUtils.findAnnotation(params[i], CodeValue.class)) != null &&
                        codeValueAnnotation.value().equals(sendCodeAnnotation.value())) {
                    objects[i] = codeValue; // 往验证码值注入方法参数
                    parameters.put(params[i].getName(), codeValue); // 更新参数表
                } else if ((codeParamAnnotation = AnnotationUtils.findAnnotation(params[i], CodeParam.class)) != null &&
                        codeParamAnnotation.value().equals(sendCodeAnnotation.value())) {
                    String key = codeParamAnnotation.name().length() > 0 ? codeParamAnnotation.name() : params[i].getName();
                    Object value = objects[i] != null ? objects[i] : codeParamAnnotation.defaultValue();
                    code.getData().put(key, value); // 存储验证码参数
                } else if (objects[i] != null) {
                    Class<?> paramType = params[i].getType();
                    Vector<Util.AnnotationField<CodeValue>> codeValues = Util.AnnotationFieldFinder.get(CodeValue.class).find(paramType);
                    Vector<Util.AnnotationField<CodeParam>> codeParams = Util.AnnotationFieldFinder.get(CodeParam.class).find(paramType);
                    if (codeValues != null)
                        for (Util.AnnotationField<CodeValue> field : codeValues) {
                            if (!field.getAnnotation().value().equals(sendCodeAnnotation.value()))
                                continue;
                            Field f = field.getField();
                            field.write(objects[i], codeValue);
                            parameters.put(f.getName(), codeValue);
                        }
                    if (codeParams != null)
                        for (Util.AnnotationField<CodeParam> field : codeParams) {
                            if (!field.getAnnotation().value().equals(sendCodeAnnotation.value()))
                                continue;
                            Field f = field.getField();
                            String key = field.getAnnotation().name().length() > 0 ? field.getAnnotation().name() : f.getName();
                            Object value = field.read(objects[i]);
                            if (value == null)
                                value = field.getAnnotation().defaultValue();
                            code.getData().put(key, value); // 存储验证码参数
                        }
                }
            }
        }

        store.store(code, sendCodeAnnotation.duration(), parameters); // 存储验证码
        sender.send(code, parameters); // 发送验证码
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public BeanFactory getFactory() {
        return factory;
    }

    public void setFactory(BeanFactory factory) {
        this.factory = factory;
    }
}
