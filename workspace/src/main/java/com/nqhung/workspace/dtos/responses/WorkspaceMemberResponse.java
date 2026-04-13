package com.nqhung.workspace.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkspaceMemberResponse {
    private Long id;
    private Long workspaceId;
    private Long userId;
    private String roleCode;
    private String memberStatus;
    private LocalDateTime joinedAt;
}