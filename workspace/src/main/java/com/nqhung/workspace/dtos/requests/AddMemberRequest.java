package com.nqhung.workspace.dtos.requests;

import com.nqhung.workspace.enums.WorkspaceRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {

    @NotNull(message = "User id must not be null")
    private Long userId;

    @NotNull(message = "Role must not be null")
    private WorkspaceRole roleCode;
}