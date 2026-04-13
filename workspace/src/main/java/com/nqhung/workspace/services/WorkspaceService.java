package com.nqhung.workspace.services;

import com.nqhung.workspace.dtos.requests.WorkspaceCreateRequest;
import com.nqhung.workspace.dtos.responses.WorkspaceResponse;

public interface WorkspaceService {
    WorkspaceResponse createWorkspace(Long currentUserId, WorkspaceCreateRequest request);
    WorkspaceResponse getWorkspace(Long workspaceId, Long currentUserId);
}