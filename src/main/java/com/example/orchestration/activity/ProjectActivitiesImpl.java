package com.example.orchestration.activity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class ProjectActivitiesImpl
        implements ProjectActivities {

    private final RestClient projectClient;
    private final RestClient planningClient;

    public ProjectActivitiesImpl(
            @Value("${services.project-url}")
            String projectUrl,

            @Value("${services.planning-url}")
            String planningUrl) {

        this.projectClient =
                RestClient.builder()
                        .baseUrl(projectUrl)
                        .build();

        this.planningClient =
                RestClient.builder()
                        .baseUrl(planningUrl)
                        .build();
    }

    @Override
    public void createProject(
            String projectId,
            String name,
            String requestedBy) {

        projectClient.post()
                .uri("/internal/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "projectId", projectId,
                        "name", name,
                        "requestedBy", requestedBy
                ))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void createDefaultWbs(
            String projectId) {

        planningClient.post()
                .uri(
                        "/internal/projects/{id}/wbs/default",
                        projectId
                )
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void activateProject(
            String projectId) {

        projectClient.put()
                .uri(
                        "/internal/projects/{id}/activate",
                        projectId
                )
                .retrieve()
                .toBodilessEntity();
    }
}