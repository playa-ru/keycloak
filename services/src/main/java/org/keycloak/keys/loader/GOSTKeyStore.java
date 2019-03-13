package org.keycloak.keys.loader;

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

import org.keycloak.common.util.Base64Url;
import org.keycloak.component.ComponentModel;
import org.keycloak.crypto.KeyStatus;
import org.keycloak.crypto.KeyType;
import org.keycloak.crypto.KeyUse;
import org.keycloak.crypto.KeyWrapper;
import org.keycloak.gost.GOSTAlgorithm;
import org.keycloak.gost.GOSTAttribute;
import org.keycloak.gost.GOSTException;
import org.keycloak.keys.Attributes;

/**
 * Хранилище ключей.
 *
 * @author Anatoliy Pokhresnyi
 */
public class GOSTKeyStore {

    /**
     * ID KeyStore.
     */
    private final String id;

    /**
     * Приоритет.
     */
    private final long priority;

    /**
     * Статус.
     */
    private final KeyStatus status;

    /**
     * Алгоритм подписи.
     */
    private final String algorithm;

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
     *
     * @param model Данные для подключения к хранилищу.
     */
    public GOSTKeyStore(final ComponentModel model) {
        try {
            this.id = model.getId();
            this.priority = model.get(GOSTAttribute.PRIORITY_KEY, 0L);
            this.keyStore = KeyStore.getInstance(model.get(GOSTAttribute.KEYSTORE_KEY, ""));
            this.status = KeyStatus.from(model.get(Attributes.ACTIVE_KEY, true),
                                         model.get(Attributes.ENABLED_KEY, true));
            this.algorithm = GOSTAlgorithm.getDisplayName(model.get(GOSTAttribute.ALGORITHM_KEY, ""));
            this.alias = model.get(GOSTAttribute.KEY_ALIAS_KEY, "");
            this.password = model.get(GOSTAttribute.KEYSTORE_PASSWORD_KEY, "")
                                 .toCharArray();

            this.keyStore.load(null);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            throw new GOSTException(e.getMessage(), e);
        }
    }

    /**
     * Получает ключ.
     *
     * @return Ключ.
     */
    public KeyWrapper key() {
        X509Certificate certificate = getX509Certificate();

        if (certificate == null) {
            throw new GOSTException("Certificate not found");
        }

        PublicKey publicKey = certificate.getPublicKey();

        KeyWrapper key = new KeyWrapper();

        key.setProviderId(id);
        key.setProviderPriority(priority);
        key.setKid(getKid(publicKey));
        key.setUse(KeyUse.SIG);
        key.setType(KeyType.GOST);
        key.setAlgorithm(GOSTAlgorithm.getDisplayName(algorithm));
        key.setStatus(status);
        key.setSignKey(getPrivateKey());
        key.setVerifyKey(publicKey);
        key.setCertificate(certificate);

        return key;
    }

    /**
     * Получает закрытый ключ.
     *
     * @return Закрытый ключ.
     */
    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey(alias, password);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new GOSTException(e.getMessage(), e);
        }
    }

    /**
     * Получает сертификат.
     * @return Сертификат.
     */
    private X509Certificate getX509Certificate() {
        try {
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);

            if (certificate == null) {
                return null;
            }

            InputStream stream = new ByteArrayInputStream(certificate.getEncoded());

            return (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(stream);
        } catch (CertificateException | KeyStoreException e) {
            throw new GOSTException(e.getMessage(), e);
        }
    }

    /**
     * Получает Kid по открытому ключу.
     *
     * @param publicKey Открытый ключ.
     * @return Kid.
     */
    private String getKid(final PublicKey publicKey) {
        try {
            return Base64Url.encode(MessageDigest.getInstance("SHA-256").digest(publicKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            throw new GOSTException(e.getMessage(), e);
        }
    }
}
