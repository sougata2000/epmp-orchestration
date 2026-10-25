package com.example.orchestration.activity;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ProjectActivities {

    void createProject(
            String projectId,
            String name,
            String requestedBy);

    void createDefaultWbs(
            String projectId);

    void activateProject(
            String projectId);
}