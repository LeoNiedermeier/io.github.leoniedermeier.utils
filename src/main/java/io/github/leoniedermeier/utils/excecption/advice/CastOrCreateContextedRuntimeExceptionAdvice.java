package io.github.leoniedermeier.utils.excecption.advice;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.stereotype.Component;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;
import io.github.leoniedermeier.utils.excecption.StringErrorCode;

/**
 * Create a {@link ContextedRuntimeException} and adds the method parameters as context values unless they are annotated with {@link IgnoreParam}.
 *
 */
@Aspect
@Component
class CastOrCreateContextedRuntimeExceptionAdvice {

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @AfterThrowing(
            pointcut = "execution(@io.github.leoniedermeier.utils.excecption.advice.CastOrCreateContextedRuntimeException * *(..)) "
                    + "&& @annotation(castOrCreateContextedRuntimeException)",
            throwing = "ex")
    public void afterThrowingAdvice(JoinPoint jp,
            CastOrCreateContextedRuntimeException castOrCreateContextedRuntimeException, Exception ex)
            throws Exception {
        try {

            ContextedRuntimeException cre = ContextedRuntimeException
                    .castOrCreate(new StringErrorCode(castOrCreateContextedRuntimeException.value()), ex);

            addContextValues(cre, jp);
            throw cre;
        } catch (ContextedRuntimeException e) {
            throw e;
        } catch (Exception e) {
            // In the case we can not build the ContextedRuntimeException, throw the
            // original exception
            log.info("Ignored exception: ", e);
            throw ex;
        }
    }

    private void addContextValues(ContextedRuntimeException cre, JoinPoint jp) {
        if (!(jp.getSignature() instanceof MethodSignature)) {
            return;
        }
        
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        for (int parameterIndex = 0; parameterIndex < method.getParameterCount(); parameterIndex++) {

            MethodParameter methodParameter = SynthesizingMethodParameter.forExecutable(method, parameterIndex);
            if (methodParameter.hasParameterAnnotation(IgnoreParam.class)) {
                continue;
            }
            methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
            String parameterName = StringUtils.defaultIfEmpty(methodParameter.getParameterName(), "!unknownName!");

            cre.addContextValue(parameterName, jp.getArgs()[parameterIndex]);
        }
    }
}