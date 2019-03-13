package org.keycloak.crypto;

import org.keycloak.common.VerificationException;
import org.keycloak.models.KeycloakSession;

/**
 * Провайдер подписания и проверки подписи.
 *
 * @author Anatoliy Pokhresnyi
 */
public class GOSTSignatureProvider
implements SignatureProvider
{

    /**
     * Сессия Keycloak.
     */
    private final KeycloakSession session;

    /**
     * Алгоритм шифрования.
     */
    private final String algorithm;

    /**
     * Создает провайдер.
     *
     * @param session Сессия в Keycloak.
     * @param algorithm Алгоритм шифрования.
     */
    public GOSTSignatureProvider(final KeycloakSession session, final String algorithm) {
        this.session = session;
        this.algorithm = algorithm;
    }

    @Override
    public SignatureSignerContext signer() throws SignatureException {
        KeyWrapper key = session.keys().getActiveKey(session.getContext().getRealm(), KeyUse.SIG, algorithm);

        if (key == null) {
            throw new SignatureException("Active key for " + algorithm + " not found");
        }

        return new GOSTSignatureSignerContext(key);
    }

    @Override
    public SignatureVerifierContext verifier(String kid) throws VerificationException {
        KeyWrapper key = session.keys().getKey(session.getContext().getRealm(), kid, KeyUse.SIG, algorithm);

        if (key == null) {
            throw new VerificationException("Key not found");
        }

        return new GOSTSignatureVerifierContext(key);
    }
}
