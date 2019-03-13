package org.keycloak.crypto;

import org.keycloak.gost.GOSTAlgorithm;
import org.keycloak.models.KeycloakSession;

public class GOST256ClientSignatureVerifierProviderFactory implements ClientSignatureVerifierProviderFactory {

    @Override
    public String getId() {
        return GOSTAlgorithm.GOST_SIGN_2012_256;
    }

    @Override
    public ClientSignatureVerifierProvider create(KeycloakSession session) {
        return new GOST256ClientSignatureVerifierProvider(session);
    }
}