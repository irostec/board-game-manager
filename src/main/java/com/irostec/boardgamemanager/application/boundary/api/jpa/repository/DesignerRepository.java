package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Designer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignerRepository  extends JpaRepository<Designer, Long> {
}
