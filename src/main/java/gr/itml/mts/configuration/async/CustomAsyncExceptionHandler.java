package gr.itml.mts.configuration.async;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        log.error("Exception for Async execution: ", throwable);
        log.error("Method name - {}", method.getName());
        for (Object param : objects) {
            log.error("Parameter value - {}", param);
        }
    }
}
