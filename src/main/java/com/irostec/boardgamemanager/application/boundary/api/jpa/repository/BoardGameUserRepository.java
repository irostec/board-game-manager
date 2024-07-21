package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardGameUserRepository extends JpaRepository<BoardGameUser, Long> {

    Optional<BoardGameUser> findByUsername(String username);

}
