package com.example.orchestration.workflow;

public class ProjectWorkflowInput {

    private String projectId;
    private String name;
    private String requestedBy;

    public ProjectWorkflowInput() {
    }

    public ProjectWorkflowInput(
            String projectId,
            String name,
            String requestedBy) {

        this.projectId = projectId;
        this.name = name;
        this.requestedBy = requestedBy;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getRequestedBy() {
        return requestedBy;
    }
}