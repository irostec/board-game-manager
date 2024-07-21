package com.irostec.boardgamemanager.application.boundary.api.transaction;

import com.irostec.boardgamemanager.application.core.api.transaction.TransactionalOperationFactory;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.function.Function;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public final class SpringTransactionalOperationFactory implements TransactionalOperationFactory {

    private static final DefaultTransactionDefinition DEFAULT_TRANSACTION_DEFINITION;
    private final PlatformTransactionManager transactionManager;

    static {
        DEFAULT_TRANSACTION_DEFINITION = new DefaultTransactionDefinition();
    }

    private static DefaultTransactionDefinition getDefaultTransactionDefinition() {
        return DEFAULT_TRANSACTION_DEFINITION;
    }

    @Override
    public <L, R> Supplier<Either<L, R>> fromSupplier(Supplier<Either<L, R>> supplier) {

        return () -> {

            TransactionStatus status = transactionManager.getTransaction(getDefaultTransactionDefinition());

            return supplier.get()
                    .peek(r -> transactionManager.commit(status))
                    .peekLeft(l -> transactionManager.rollback(status));

        };

    }

    @Override
    public <L, R, I> Function<I, Either<L, R>> fromFunction(Function<I, Either<L, R>> f) {

        return input -> {

            TransactionStatus status = transactionManager.getTransaction(getDefaultTransactionDefinition());

            return f.apply(input)
                    .peek(r -> transactionManager.commit(status))
                    .peekLeft(l -> transactionManager.rollback(status));

        };

    }

}
