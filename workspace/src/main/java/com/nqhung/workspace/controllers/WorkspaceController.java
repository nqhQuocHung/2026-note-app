package com.nqhung.workspace.controllers;

import com.nqhung.workspace.dtos.requests.WorkspaceCreateRequest;
import com.nqhung.workspace.dtos.responses.WorkspaceResponse;
import com.nqhung.workspace.services.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace(
            @RequestBody @Valid WorkspaceCreateRequest request,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        return ResponseEntity.ok(workspaceService.createWorkspace(currentUserId, request));
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> getWorkspace(
            @PathVariable Long workspaceId,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        return ResponseEntity.ok(workspaceService.getWorkspace(workspaceId, currentUserId));
    }
}