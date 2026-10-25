package com.example.orchestration.api;

public record CreateWorkflowRequest(
        String projectId,
        String name,
        String requestedBy) {
}