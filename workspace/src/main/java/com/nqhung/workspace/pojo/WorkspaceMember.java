package com.nqhung.workspace.pojo;

import com.nqhung.workspace.enums.MemberStatus;
import com.nqhung.workspace.enums.WorkspaceRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "workspace_member",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_workspace_member_workspace_user",
                        columnNames = {"workspace_id", "user_id"}
                )
        }
)
public class WorkspaceMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workspace_id", nullable = false)
    private Long workspaceId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_code", nullable = false, length = 30)
    private WorkspaceRole roleCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status", nullable = false, length = 30)
    private MemberStatus memberStatus;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "invited_at")
    private LocalDateTime invitedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;
}
