package cn.dustlight.captcha.core;

import cn.dustlight.captcha.annotations.VerifyCode;
import cn.dustlight.captcha.configurations.DefaultBeanProperties;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;

public class VerifyCodeAdvisor extends AbstractPointcutAdvisor {

    private VerifyCodeInterceptor interceptor;

    public VerifyCodeAdvisor(DefaultBeanProperties defaultBeanProperties) {
        this.interceptor = new VerifyCodeInterceptor(defaultBeanProperties);
    }

    public Pointcut getPointcut() {
        return new AnnotationMatchingPointcut(null, VerifyCode.class, true);
    }

    public Advice getAdvice() {
        return interceptor;
    }

    public BeanFactory getFactory() {
        return interceptor.getFactory();
    }

    public void setFactory(BeanFactory factory) {
        interceptor.setFactory(factory);
    }

    @Override
    public int getOrder() {
        return interceptor.getOrder();
    }

    @Override
    public void setOrder(int order) {
        interceptor.setOrder(order);
    }
}
