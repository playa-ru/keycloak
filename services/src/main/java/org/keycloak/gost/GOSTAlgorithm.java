package org.keycloak.gost;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

/**
 * Статический класс, содержащий дисплейные и JCP названия алгоритмов шифрования.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class GOSTAlgorithm {

    public static final String GOST3410DHEL = "GOST3410DHEL";

    /**
     * Дисплейное название алгоритма ГОСТ Р 34.10-2012 (256).
     */
    public static final String GOST_SIGN_2012_256_DISPLAY_NAME = "GOST3411_2012_256";

    /**
     * Навазние алгоритма ГОСТ Р 34.10-2012 (256) в JCP.
     */
    public static final String GOST_SIGN_2012_256 = "GOST3411_2012_256withGOST3410_2012_256";

    /**
     * Дисплейное название алгоритма ГОСТ Р 34.10-2012 (512).
     */
    public static final String GOST_SIGN_2012_512_DISPLAY_NAME = "GOST3411_2012_512";

    /**
     * Навазние алгоритма ГОСТ Р 34.10-2012 (512) в JCP.
     */
    public static final String GOST_SIGN_2012_512 = "GOST3411_2012_512withGOST3410_2012_512";

    /**
     * Приватный конструктор.
     */
    private GOSTAlgorithm() {

    }

    public static String getDisplayName(final String algorithm) {
        switch (algorithm) {
            case GOST_SIGN_2012_256:
                return GOST_SIGN_2012_256_DISPLAY_NAME;
            case GOST_SIGN_2012_512:
                return GOST_SIGN_2012_512_DISPLAY_NAME;
            default:
                return algorithm;
        }
    }

    public static String getAlgorithm(final String displayName) {
        switch (displayName) {
            case GOST_SIGN_2012_256_DISPLAY_NAME:
                return GOST_SIGN_2012_256;
            case GOST_SIGN_2012_512_DISPLAY_NAME:
                return GOST_SIGN_2012_512;
            default:
                return displayName;
        }
    }

    public static boolean jcpVerify() {
        try {
            Signature.getInstance(GOST_SIGN_2012_512);
            Signature.getInstance(GOST_SIGN_2012_256);
            KeyFactory.getInstance(GOST3410DHEL);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }

        return true;
    }
}
