package com.nqhung.workspace.repositories;

import com.nqhung.workspace.pojo.WorkspaceMember;
import com.nqhung.workspace.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    Optional<WorkspaceMember> findByWorkspaceIdAndUserId(Long workspaceId, Long userId);
    List<WorkspaceMember> findAllByWorkspaceIdAndMemberStatus(Long workspaceId, MemberStatus memberStatus);
    boolean existsByWorkspaceIdAndUserId(Long workspaceId, Long userId);
}