package com.example.orchestration.api;

import com.example.orchestration.config.TemporalConfig;
import com.example.orchestration.workflow.ProjectWorkflow;
import com.example.orchestration.workflow.ProjectWorkflowInput;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/workflows/projects")
public class OrchestrationController {

    private final WorkflowClient workflowClient;

    public OrchestrationController(
            WorkflowClient workflowClient) {

        this.workflowClient = workflowClient;
    }

    @PostMapping
    public Map<String, String> start(
            @RequestBody CreateWorkflowRequest request) {

        String workflowId =
                "PROJECT-" + request.projectId();

        ProjectWorkflow workflow =
                workflowClient.newWorkflowStub(
                        ProjectWorkflow.class,
                        WorkflowOptions
                                .newBuilder()
                                .setWorkflowId(workflowId)
                                .setTaskQueue(
                                        TemporalConfig.TASK_QUEUE)
                                .build()
                );

        WorkflowClient.start(
                workflow::run,
                new ProjectWorkflowInput(
                        request.projectId(),
                        request.name(),
                        request.requestedBy()
                )
        );

        return Map.of(
                "workflowId", workflowId,
                "projectId", request.projectId(),
                "status", "STARTED"
        );
    }

    @PostMapping("/{projectId}/approve")
    public Map<String, String> approve(
            @PathVariable String projectId,
            @RequestBody ApprovalRequest request) {

        ProjectWorkflow workflow =
                workflowClient.newWorkflowStub(
                        ProjectWorkflow.class,
                        "PROJECT-" + projectId
                );

        workflow.approve(request.approvedBy());

        return Map.of(
                "projectId", projectId,
                "status", "APPROVAL_SIGNAL_SENT"
        );
    }

    @GetMapping("/{projectId}/status")
    public Map<String, String> status(
            @PathVariable String projectId) {

        ProjectWorkflow workflow =
                workflowClient.newWorkflowStub(
                        ProjectWorkflow.class,
                        "PROJECT-" + projectId
                );

        return Map.of(
                "projectId", projectId,
                "workflowStatus",
                workflow.getStatus()
        );
    }
}