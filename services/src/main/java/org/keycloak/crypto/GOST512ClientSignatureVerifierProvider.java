package org.keycloak.crypto;

import org.keycloak.common.VerificationException;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.keys.loader.PublicKeyStorageManager;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;

public class GOST512ClientSignatureVerifierProvider
implements ClientSignatureVerifierProvider
{

    private final KeycloakSession session;

    public GOST512ClientSignatureVerifierProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public SignatureVerifierContext verifier(ClientModel client, JWSInput input) throws VerificationException {
        KeyWrapper key = PublicKeyStorageManager.getClientPublicKeyWrapper(session, client, input);

        if (key == null) {
            throw new VerificationException("Key not found");
        }

        return new GOSTSignatureVerifierContext(key);
    }
}
