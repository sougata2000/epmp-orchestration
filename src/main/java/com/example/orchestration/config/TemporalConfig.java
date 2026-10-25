package com.example.orchestration.config;



import com.example.orchestration.activity.ProjectActivities;
import com.example.orchestration.workflow.ProjectWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {

    public static final String TASK_QUEUE =
            "PROJECT_TASK_QUEUE";

    @Bean(destroyMethod = "shutdown")
    public WorkflowServiceStubs workflowServiceStubs(
            @Value("${temporal.target}")
            String target) {

        return WorkflowServiceStubs.newServiceStubs(
                WorkflowServiceStubsOptions
                        .newBuilder()
                        .setTarget(target)
                        .build()
        );
    }

    @Bean
    public WorkflowClient workflowClient(
            WorkflowServiceStubs service,

            @Value("${temporal.namespace}")
            String namespace) {

        return WorkflowClient.newInstance(
                service,
                WorkflowClientOptions
                        .newBuilder()
                        .setNamespace(namespace)
                        .build()
        );
    }

    @Bean(destroyMethod = "shutdown")
    public WorkerFactory workerFactory(
            WorkflowClient client,
            ProjectActivities activities) {

        WorkerFactory factory =
                WorkerFactory.newInstance(client);

        Worker worker =
                factory.newWorker(TASK_QUEUE);

        worker.registerWorkflowImplementationTypes(
                ProjectWorkflowImpl.class);

        worker.registerActivitiesImplementations(
                activities);

        factory.start();

        return factory;
    }
}