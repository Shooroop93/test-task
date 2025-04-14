package com.example.test_task.advice;

import com.example.test_task.exception.ExceptionValidatedRequestOrResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Aspect
@Component
public class ValidationAspect {

    @Around("@annotation(com.example.test_task.annotation.CustomControllerHandler)")
    public Object handleValidation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        for (Object arg : proceedingJoinPoint.getArgs()) {
            if (arg instanceof BindingResult bindingResult && bindingResult.hasErrors()) {
                throw new ExceptionValidatedRequestOrResponse(bindingResult);
            }
        }
        return proceedingJoinPoint.proceed();
    }
}