package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameUser;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameUserRepository;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class BoardGameUserService {

    private final BoardGameUserRepository userRepository;

    public Try<BoardGameUser> saveUser(
        String username
    ) {

        Try<Optional<BoardGameUser>> optionalUserContainer = Try.of(() -> userRepository.findByUsername(username));

        return optionalUserContainer.flatMap(
            optionalUser -> optionalUser.map(Try::success).orElseGet(
                () -> {
                    BoardGameUser user = new BoardGameUser();
                    user.setUsername(username);

                    return Try.of(() -> userRepository.save(user));
                }
            )
        );

    }

}
