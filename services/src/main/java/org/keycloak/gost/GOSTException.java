package org.keycloak.gost;

/**
 * Ошибка.
 *
 * @author Anatoliy Pokhresnyi
 */
public class GOSTException extends RuntimeException {

    /**
     * Создает ошибку.
     *
     * @param message Сообщение об ошибке.
     */
    public GOSTException(final String message) {
        super(message);
    }

    /**
     * Создает ошибку.
     *
     * @param message Сообщение от ошибке.
     * @param cause StackTrace ошибки.
     */
    public GOSTException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
