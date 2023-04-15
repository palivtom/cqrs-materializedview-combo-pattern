package cz.ctu.fee.palivtom.orderupdaterservice.config

import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
import org.springframework.boot.task.TaskExecutorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Primary
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

const val EVENT_TASK_EXECUTOR = "eventTaskExecutor"

@Configuration
class ExecutorConfig {

    @Lazy
    @Primary
    @Bean(name = [
        TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME,
        AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME
    ])
    fun applicationTaskExecutor(builder: TaskExecutorBuilder): ThreadPoolTaskExecutor {
        return builder.build()
    }

    @Bean
    fun eventTaskExecutor(builder: TaskExecutorBuilder): ThreadPoolTaskExecutor {
        return builder
            .corePoolSize(1)
            .maxPoolSize(1)
            .threadNamePrefix("event-task#")
            .build()
    }
}