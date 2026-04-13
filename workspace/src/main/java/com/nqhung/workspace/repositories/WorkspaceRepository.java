package com.nqhung.workspace.repositories;

import com.nqhung.workspace.pojo.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    boolean existsByCode(String code);
}