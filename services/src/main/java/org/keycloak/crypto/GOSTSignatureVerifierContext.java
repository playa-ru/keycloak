package org.keycloak.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import org.keycloak.common.VerificationException;
import org.keycloak.gost.GOSTAlgorithm;

/**
 * Класс реализующий процесс проверки подписи.
 *
 * @author Anatoliy Pokhresnyi
 */
public class GOSTSignatureVerifierContext implements SignatureVerifierContext {

    /**
     * Ключ.
     */
    private final KeyWrapper key;

    /**
     * Создает объект проверки подписи.
     *
     * @param key Ключ.
     */
    public GOSTSignatureVerifierContext(final KeyWrapper key) {
        this.key = key;
    }

    @Override
    public String getKid() {
        return key.getKid();
    }

    @Override
    public String getAlgorithm() {
        return GOSTAlgorithm.getDisplayName(key.getAlgorithm());
    }

    @Override
    public boolean verify(byte[] data, byte[] signature) throws VerificationException {
        try {
            Signature verifier = Signature.getInstance(GOSTAlgorithm.getAlgorithm(key.getAlgorithm()));
            verifier.initVerify((PublicKey) key.getVerifyKey());
            verifier.update(data);

            return verifier.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new VerificationException("Signing failed", e);
        }
    }
}
