package com.nqhung.workspace.services.impl;

import com.nqhung.workspace.dtos.requests.WorkspaceCreateRequest;
import com.nqhung.workspace.dtos.responses.WorkspaceResponse;
import com.nqhung.workspace.pojo.Workspace;
import com.nqhung.workspace.pojo.WorkspaceMember;
import com.nqhung.workspace.enums.MemberStatus;
import com.nqhung.workspace.enums.WorkspaceRole;
import com.nqhung.workspace.enums.WorkspaceStatus;
import com.nqhung.workspace.repositories.WorkspaceMemberRepository;
import com.nqhung.workspace.repositories.WorkspaceRepository;
import com.nqhung.workspace.services.WorkspaceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Override
    @Transactional
    public WorkspaceResponse createWorkspace(Long currentUserId, WorkspaceCreateRequest request) {
        Workspace workspace = new Workspace();
        workspace.setCode(generateCode(request.getName()));
        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());
        workspace.setOwnerId(currentUserId);
        workspace.setStatus(WorkspaceStatus.ACTIVE);
        workspace.setCreatedBy(currentUserId);
        workspace.setUpdatedBy(currentUserId);

        workspace = workspaceRepository.save(workspace);

        WorkspaceMember ownerMember = new WorkspaceMember();
        ownerMember.setWorkspaceId(workspace.getId());
        ownerMember.setUserId(currentUserId);
        ownerMember.setRoleCode(WorkspaceRole.OWNER);
        ownerMember.setMemberStatus(MemberStatus.ACTIVE);
        ownerMember.setJoinedAt(LocalDateTime.now());
        ownerMember.setCreatedBy(currentUserId);
        ownerMember.setUpdatedBy(currentUserId);

        workspaceMemberRepository.save(ownerMember);

        return toResponse(workspace);
    }

    @Override
    public WorkspaceResponse getWorkspace(Long workspaceId, Long currentUserId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, currentUserId)
                .orElseThrow(() -> new RuntimeException("You are not a member of this workspace"));

        return toResponse(workspace);
    }

    private WorkspaceResponse toResponse(Workspace workspace) {
        WorkspaceResponse response = new WorkspaceResponse();
        response.setId(workspace.getId());
        response.setCode(workspace.getCode());
        response.setName(workspace.getName());
        response.setDescription(workspace.getDescription());
        response.setOwnerId(workspace.getOwnerId());
        response.setStatus(workspace.getStatus().name());
        response.setCreatedAt(workspace.getCreatedAt());
        return response;
    }

    private String generateCode(String name) {
        return name.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }
}