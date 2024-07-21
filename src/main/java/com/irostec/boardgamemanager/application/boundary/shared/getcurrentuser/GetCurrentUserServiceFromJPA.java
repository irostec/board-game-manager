package com.irostec.boardgamemanager.application.boundary.shared.getcurrentuser;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameUser;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameUserRepository;
import com.irostec.boardgamemanager.application.core.shared.GetCurrentUserService;
import com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error.GetCurrentUserError;
import com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error.UserNotAuthenticated;
import com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error.DatabaseError;
import com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error.UserNotFound;
import io.vavr.control.Either;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
class GetCurrentUserServiceFromJPA implements GetCurrentUserService {

    private final Logger logger = LogManager.getLogger(GetCurrentUserServiceFromJPA.class);

    private final BoardGameUserRepository userRepository;

    GetCurrentUserServiceFromJPA(BoardGameUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Either<GetCurrentUserError, com.irostec.boardgamemanager.application.core.shared.getcurrentuser.output.User> execute() {

        Either<GetCurrentUserError, String> usernameContainer =
            getUsernameFromSpringContext(UserNotAuthenticated::new);

        return usernameContainer
                .flatMap(this::getUserId)
                .map(com.irostec.boardgamemanager.application.core.shared.getcurrentuser.output.User::new);

    }

    private Either<GetCurrentUserError, Long> getUserId(String username) {

        Either<GetCurrentUserError, Optional<BoardGameUser>> optionalUserContainer = wrapWithErrorHandling(
            () -> {

                logger.info(String.format("Attempting to retrieve user information for username %s from database.", username));

                Optional<BoardGameUser> result = userRepository.findByUsername(username);

                result.ifPresentOrElse(
                    user -> logger.info(String.format("Retrieved user information for username %s", username)),
                    () -> logger.info(String.format("User information not found for username %s", username))
                );

                return result;
            },
            DatabaseError::new
        );

        return optionalUserContainer.flatMap(optionalUser ->
            optionalUser
                .map(Either::<GetCurrentUserError, BoardGameUser>right)
                .orElseGet(() -> Either.left(new UserNotFound(username)))
        )
        .map(BoardGameUser::getId);

    }

    private static <E> Either<E, String> getUsernameFromSpringContext(
        Supplier<E> errorSupplier
    ) {

        return Optional.ofNullable(SecurityContextHolder.getContext())
            .flatMap(securityContext -> Optional.ofNullable(securityContext.getAuthentication()))
            .flatMap(authentication -> Optional.ofNullable(authentication.getPrincipal()))
            .map(Object::toString)
            .map(Either::<E, String>right)
            .orElse(Either.left(errorSupplier.get()));

    }

}
