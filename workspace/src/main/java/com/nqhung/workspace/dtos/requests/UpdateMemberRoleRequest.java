package com.nqhung.workspace.dtos.requests;

import com.nqhung.workspace.enums.WorkspaceRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberRoleRequest {

    @NotNull(message = "Role must not be null")
    private WorkspaceRole roleCode;
}