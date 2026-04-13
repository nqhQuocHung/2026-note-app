package com.nqhung.workspace.services;

import com.nqhung.workspace.dtos.requests.AddMemberRequest;
import com.nqhung.workspace.dtos.requests.UpdateMemberRoleRequest;
import com.nqhung.workspace.dtos.responses.WorkspaceMemberResponse;

import java.util.List;

public interface WorkspaceMemberService {
    WorkspaceMemberResponse addMember(Long workspaceId, AddMemberRequest request, Long currentUserId);
    void removeMember(Long workspaceId, Long userId, Long currentUserId);
    WorkspaceMemberResponse updateMemberRole(Long workspaceId, Long userId, UpdateMemberRoleRequest request, Long currentUserId);
    List<WorkspaceMemberResponse> listMembers(Long workspaceId, Long currentUserId);
}