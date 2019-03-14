package org.keycloak.jose.jws.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.keycloak.common.util.PemUtils;
import org.keycloak.jose.jws.Algorithm;
import org.keycloak.jose.jws.JWSInput;

public class GOSTProvider implements SignatureProvider {

    protected static final Logger LOGGER = Logger.getLogger(GOSTProvider.class.getName());

    public static String getGOSTAlgorithm(Algorithm alg) {
        switch (alg) {
            case GOST512:
                return "GOST3411_2012_512withGOST3410_2012_512";
            case GOST256:
                return "GOST3411_2012_256withGOST3410_2012_256";
            default:
                throw new IllegalArgumentException("Not an GOST Algorithm");
        }
    }

    public static Signature getSignature(Algorithm alg) {
        try {
            return Signature.getInstance(getGOSTAlgorithm(alg));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] sign(byte[] data, Algorithm algorithm, PrivateKey privateKey) {
        try {
            Signature signature = getSignature(algorithm);
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyViaCertificate(JWSInput input, String cert) {
        X509Certificate certificate = null;
        try {
            certificate = PemUtils.decodeCertificate(cert);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return verify(input, certificate.getPublicKey());
    }

    public static boolean verify(JWSInput input, PublicKey publicKey) {
        try {
            Signature verifier = getSignature(input.getHeader().getAlgorithm());
            verifier.initVerify(publicKey);
            verifier.update(input.getEncodedSignatureInput().getBytes("UTF-8"));
            return verifier.verify(input.getSignature());
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean verify(JWSInput input, String key) {
        return verifyViaCertificate(input, key);
    }
}
