package com.nqhung.workspace.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceCreateRequest {

    @NotBlank(message = "Workspace name must not be blank")
    private String name;

    private String description;
}