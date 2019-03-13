package org.keycloak.jose.jws.crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

import org.keycloak.common.util.Base64Url;


/**
 * Хранилище ключей.
 *
 * @author Anatoliy Pokhresnyi
 */
public class GOSTKeyStore {

    protected static final Logger LOGGER = Logger.getLogger(GOSTKeyStore.class.getName());

    /**
     * KeyStore
     */
    private final KeyStore keyStore;

    /**
     * Alias контейнера в KeyStore.
     */
    private final String alias;

    /**
     * Пароль контейнера в KeyStore.
     */
    private final char[] password;

    /**
     * Создает хранилище ключей.
     */
    public GOSTKeyStore() {
        try {
            this.alias = "container";
            this.password = "123".toCharArray();
            this.keyStore = KeyStore.getInstance("HDImageStore");

            this.keyStore.load(null);
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Получает закрытый ключ.
     *
     * @return Закрытый ключ.
     */
    public PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey(alias, password);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Получает сертификат.
     * @return Сертификат.
     */
    public X509Certificate getX509Certificate() {
        try {
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);

            if (certificate == null) {
                return null;
            }

            InputStream stream = new ByteArrayInputStream(certificate.getEncoded());

            return (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(stream);
        } catch (CertificateException | KeyStoreException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public X509Certificate getX509Certificate1() {
        try {
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);

            if (certificate == null) {
                return null;
            }

            return certificate;
        } catch (KeyStoreException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public PublicKey getPublicKey() {
        return getX509Certificate().getPublicKey();
    }

    /**
     * Получает Kid по открытому ключу.
     *
     * @return Kid.
     */
    public String getKid() {
        try {
            return Base64Url.encode(MessageDigest.getInstance("SHA-256").digest(getPublicKey().getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
