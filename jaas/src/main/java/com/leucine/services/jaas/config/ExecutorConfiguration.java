package com.leucine.services.jaas.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class ExecutorConfiguration implements AsyncConfigurer {

  private static final int USER_AUDIT_THREAD_POOL_SIZE = 10;
  private static final int EMAIL_THREAD_POOL_SIZE = 3;

  @Bean(name = "auditExecutor")
  public TaskExecutor auditTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(ExecutorConfiguration.USER_AUDIT_THREAD_POOL_SIZE);
    executor.setMaxPoolSize(ExecutorConfiguration.USER_AUDIT_THREAD_POOL_SIZE);
    executor.setThreadNamePrefix("audits");
    executor.initialize();
    return executor;
  }

  @Bean(name = "emailExecutor")
  public TaskExecutor emailTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(ExecutorConfiguration.EMAIL_THREAD_POOL_SIZE);
    executor.setMaxPoolSize(ExecutorConfiguration.EMAIL_THREAD_POOL_SIZE);
    executor.setThreadNamePrefix("email");
    executor.initialize();
    return executor;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
}
