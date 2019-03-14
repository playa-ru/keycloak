package org.keycloak.crypto;

import org.keycloak.gost.GOSTAlgorithm;
import org.keycloak.models.KeycloakSession;

/**
 * Подписание по алгоритму ГОСТ Р 34.10-2012 (512).
 *
 * @author Anatoliy Pokhresnyi
 */
public class GOST512SignatureProviderFactory implements SignatureProviderFactory {

    @Override
    public String getId() {
        return GOSTAlgorithm.GOST_SIGN_2012_512_DISPLAY_NAME;
    }

    @Override
    public SignatureProvider create(KeycloakSession session) {
        return new GOSTSignatureProvider(session, GOSTAlgorithm.GOST_SIGN_2012_512);
    }
}
