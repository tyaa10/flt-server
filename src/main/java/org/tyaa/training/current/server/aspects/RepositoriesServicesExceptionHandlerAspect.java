package org.tyaa.training.current.server.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Configuration;
import org.tyaa.training.current.server.models.ResponseModel;

/**
 * Набор служебных методов, внутри которых будут вызываться основные методы приложения
 * для перехвата и обработки исключительных ситуаций
 * */
@Aspect
@Configuration
public class RepositoriesServicesExceptionHandlerAspect {
    private static final String UNIQUE_CONSTRAINT_VIOLATION = "UNIQUE_CONSTRAINT_VIOLATION";
    /**
     * Обработка исключений в репозиториях
     * */
    @Around("execution(* org.tyaa.training.server.repositories.*.*(..))")
    public Object onRepositoryMethodExecution(ProceedingJoinPoint pjp) throws Exception {
        System.out.println("onRepositoryMethodExecution");
        Object output = null;
        try {
            output = pjp.proceed();
        } catch (Exception ex) {
            System.out.println("onRepositoryMethodException: " + ex.getMessage());
            // если перехвачено исключение типа "нарушение ограничения уникальности"
            if (ex.getMessage() != null && ex.getMessage().contains("users_name_key")) {
                // то вместо него выбросить стандартное исключение типа ConstraintViolationException
                // со значением сообщения "UNIQUE_CONSTRAINT_VIOLATION"
                System.out.println("onRepositoryMethodException handled");
                throw new ConstraintViolationException(UNIQUE_CONSTRAINT_VIOLATION, null, "");
            }
            throw ex;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return output;
    }

    /**
     * Обработка исключений в службах
     * */
    @Around("execution(* org.tyaa.training.server.services.*.*(..))")
    public Object onServiceMethodExecution(ProceedingJoinPoint pjp) {
        System.out.println("onServiceMethodExecution");
        Object output = null;
        try {
            output = pjp.proceed();
        } catch (ConstraintViolationException ex) {
            System.out.println("onServiceMethodException handled");
            final String constraintViolationMessage =
                ex.getMessage().equals(UNIQUE_CONSTRAINT_VIOLATION)
                        ? "This name is already taken"
                        : "Constraint violation error in the datasource";
            output =
                    ResponseModel.builder()
                            .status(ResponseModel.FAIL_STATUS)
                            .message(constraintViolationMessage)
                            .build();
        } catch (Exception ex) {
            System.err.println("Unknown Error");
            ex.printStackTrace();
            output =
                    ResponseModel.builder()
                            .status(ResponseModel.FAIL_STATUS)
                            .message("Unknown Error")
                            .build();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return output;
    }
}
