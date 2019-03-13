package org.keycloak.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;

import org.keycloak.gost.GOSTAlgorithm;

/**
 * Класс реализующий процесс подписания.
 *
 * @author Anatoliy Pokhresnyi
 */
public class GOSTSignatureSignerContext implements SignatureSignerContext {

    /**
     * Ключ.
     */
    private final KeyWrapper key;

    /**
     * Создает объект подписания.
     *
     * @param key Ключ.
     */
    public GOSTSignatureSignerContext(final KeyWrapper key) {
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
    public String getHashAlgorithm() {
        return GOSTAlgorithm.getDisplayName(key.getAlgorithm());
    }

    @Override
    public byte[] sign(byte[] data) throws SignatureException {
        try {
            Signature signature = Signature.getInstance(GOSTAlgorithm.getAlgorithm(key.getAlgorithm()));
            signature.initSign((PrivateKey) key.getSignKey());
            signature.update(data);

            return signature.sign();
        } catch (InvalidKeyException | java.security.SignatureException | NoSuchAlgorithmException e) {
            throw new SignatureException(e.getMessage(), e);
        }
    }
}
