package com.example.orchestration.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ProjectWorkflow {

    @WorkflowMethod
    void run(ProjectWorkflowInput input);

    @SignalMethod
    void approve(String approvedBy);

    @QueryMethod
    String getStatus();
}