package com.irostec.boardgamemanager.application.boundary.test2;

import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Test2ContextA {

    private final Test2ContextB test2ContextB;
    private final BoardGameRepository boardGameRepository;

    @Transactional
    public boolean execute() {

        test2ContextB.execute();

        throw new RuntimeException();

    }

}
