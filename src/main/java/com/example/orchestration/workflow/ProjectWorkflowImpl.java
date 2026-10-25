package com.example.orchestration.workflow;


import com.example.orchestration.activity.ProjectActivities;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class ProjectWorkflowImpl
        implements ProjectWorkflow {

    private boolean approved = false;
    private String approvedBy;

    private String status = "STARTED";

    private final ProjectActivities activities =
            Workflow.newActivityStub(
                    ProjectActivities.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(
                                    Duration.ofSeconds(30))
                            .setRetryOptions(
                                    RetryOptions.newBuilder()
                                            .setInitialInterval(
                                                    Duration.ofSeconds(2))
                                            .setBackoffCoefficient(2)
                                            .setMaximumInterval(
                                                    Duration.ofSeconds(30))
                                            .setMaximumAttempts(5)
                                            .build())
                            .build());

    @Override
    public void run(ProjectWorkflowInput input) {

        status = "CREATING_PROJECT";

        activities.createProject(
                input.getProjectId(),
                input.getName(),
                input.getRequestedBy());

        status = "WAITING_FOR_APPROVAL";

        Workflow.await(() -> approved);

        status = "CREATING_WBS";

        activities.createDefaultWbs(
                input.getProjectId());

        status = "ACTIVATING_PROJECT";

        activities.activateProject(
                input.getProjectId());

        status = "COMPLETED";
    }

    @Override
    public void approve(String approvedBy) {

        this.approvedBy = approvedBy;
        this.approved = true;
    }

    @Override
    public String getStatus() {

        return status +
                (approvedBy == null
                        ? ""
                        : " | approvedBy=" + approvedBy);
    }
}