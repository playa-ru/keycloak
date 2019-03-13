package org.keycloak.jose.jwk;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GOSTPublicJWK extends JWK {

    public static final String GOST = "GOST";

    public static final String PUBLIC_KEY = "publicKey";

    @JsonProperty(PUBLIC_KEY)
    private String publicKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
