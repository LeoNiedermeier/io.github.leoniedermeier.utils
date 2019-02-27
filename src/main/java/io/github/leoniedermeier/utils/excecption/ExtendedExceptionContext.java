package io.github.leoniedermeier.utils.excecption;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adds a functionality to handle {@link ErrorCode}s and a correlation id (CID).
 *
 * @param <T> The {@link ExtendedExceptionContext} type subclass
 *
 */
public interface ExtendedExceptionContext<T extends ExtendedExceptionContext<T>> extends ExceptionContext<T> {

    /*
     * The random number generator used by this class to create random based CID. In
     * a holder class to defer initialization until needed.
     */
    final class SecureRandomHolder {
        private static final SecureRandom numberGenerator = new SecureRandom();

        private SecureRandomHolder() {
            throw new AssertionError("No ExtendedExceptionContext.SecureRandomHolder instances for you!");
        }
    }

    /**
     * Adds a random CID.
     */
    default void addCID() {
        if (!findFirstContextValue("CID").isPresent()) {
            String cid = Integer
                    .toString(SecureRandomHolder.numberGenerator.nextInt(Integer.MAX_VALUE), Character.MAX_RADIX)
                    .toUpperCase();
            addContextValue("CID", cid);
        }
    }

    /**
     * Adds a {@link ErrorCode} into this context.
     * 
     * @param errorCode The {@link ErrorCode} to add.
     * @return {@code this}, for method chaining, not {@code null}
     */
    default T addErrorCode(final ErrorCode errorCode) {
        return addContextValue(ErrorCode.class.getSimpleName(), errorCode);
    }

    /**
     * Retrieves the first added {@link ErrorCode}.
     * 
     * @return The last added {@link ErrorCode}.
     */
    default Optional<ErrorCode> findFirstErrorCode() {
        return findFirstContextValue(ErrorCode.class.getSimpleName(), ErrorCode.class);
    }

    /**
     * Retrieves the last added {@link ErrorCode}.
     * 
     * @return The last added {@link ErrorCode}.
     */
    default Optional<ErrorCode> findLastErrorCode() {
        return findLastContextValue(ErrorCode.class.getSimpleName(), ErrorCode.class);
    }

    /**
     * Retrieves the CID.
     * 
     * @return The CID.
     */
    default Optional<String> getCID(ExtendedExceptionContext<T> this) {
        return findFirstContextValue("CID", String.class);
    }

    /**
     * Retrieves a list of the {@link ErrorCode}s.
     * 
     * @return The {@link ErrorCode}s, never {@code null}
     */
    default Stream<ErrorCode> getErrorCodes() {
        return getContextValues(ErrorCode.class.getSimpleName(), ErrorCode.class);
    }

    @Override
    default String getFormattedExceptionMessage() {
        final StringBuilder buffer = new StringBuilder(256);
        buffer.append("ErrorCodes=").append(getErrorCodes().map(ErrorCode::code).collect(Collectors.joining(", ")))
                .append("\n");

        buffer.append("CID=");
        getCID().ifPresent(buffer::append);
        buffer.append("\n---------------------------------\n");
        buffer.append(ExceptionContext.super.getFormattedExceptionMessage());
        return buffer.toString();
    }

    /**
     * Adds the {@link ErrorCode} and a correlation id.
     * 
     * @param errorCode The {@link ErrorCode} to add.
     */
    default void addErrorCodeAndCID(final ErrorCode errorCode) {
        addContextValue(ErrorCode.class.getSimpleName(), errorCode);
        addCID();
    }
}
