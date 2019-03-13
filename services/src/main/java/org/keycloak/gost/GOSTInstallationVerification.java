package org.keycloak.gost;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

public class GOSTInstallationVerification {

    public static boolean verify() {
        try {
            Signature.getInstance(GOSTAlgorithm.GOST_SIGN_2012_512);
            Signature.getInstance(GOSTAlgorithm.GOST_SIGN_2012_256);

            KeyFactory.getInstance("GOST3410DHEL");
        } catch (NoSuchAlgorithmException e) {
            return false;
        }

        return true;
    }

}
