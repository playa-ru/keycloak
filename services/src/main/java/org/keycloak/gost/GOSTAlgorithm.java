package org.keycloak.gost;

/**
 * Статический класс, содержащий дисплейные и JCP названия алгоритмов шифрования.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class GOSTAlgorithm {

    /**
     * Дисплейное название алгоритма ГОСТ Р 34.10-2012 (256).
     */
    public static final String GOST_SIGN_2012_256_DISPLAY_NAME = "ГОСТ Р 34.10-2012 (256)";

    /**
     * Навазние алгоритма ГОСТ Р 34.10-2012 (256) в JCP.
     */
    public static final String GOST_SIGN_2012_256 = "GOST3411_2012_256withGOST3410_2012_256";

    /**
     * Дисплейное название алгоритма ГОСТ Р 34.10-2012 (512).
     */
    public static final String GOST_SIGN_2012_512_DISPLAY_NAME = "ГОСТ Р 34.10-2012 (512)";

    /**
     * Навазние алгоритма ГОСТ Р 34.10-2012 (512) в JCP.
     */
    public static final String GOST_SIGN_2012_512 = "GOST3411_2012_512withGOST3410_2012_512";

    /**
     * Приватный конструктор.
     */
    private GOSTAlgorithm() {

    }

    /**
     * Получение по дисплейному названию алгоритма названия в JCP.
     *
     * @param displayName Дислейное название алгоритма.
     * @return Название алгоритма в JCP.
     */
    public static String getAlgorithm(final String displayName) {
        switch (displayName) {
            case GOST_SIGN_2012_256_DISPLAY_NAME:
                return GOST_SIGN_2012_256;
            case GOST_SIGN_2012_512_DISPLAY_NAME:
                return GOST_SIGN_2012_512;
            default:
                throw new GOSTException("Algorithm not found: " + displayName);
        }
    }
}
