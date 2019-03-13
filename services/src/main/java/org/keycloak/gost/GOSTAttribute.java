package org.keycloak.gost;

import org.keycloak.provider.ProviderConfigProperty;

import static org.keycloak.provider.ProviderConfigProperty.BOOLEAN_TYPE;
import static org.keycloak.provider.ProviderConfigProperty.LIST_TYPE;
import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

/**
 * Статический класс, содержащий описания аттрибутов провайдера.
 *
 * @author Anatoliy Pokhresnyi
 */
public final class GOSTAttribute {

    /**
     * Название аттрибута "Priority".
     */
    public static final String PRIORITY_KEY = "priority";

    /**
     * Описание аттрибута "Priority".
     */
    public static final ProviderConfigProperty PRIORITY_PROPERTY = new ProviderConfigProperty(
        PRIORITY_KEY, "Priority", "Priority for the provider", STRING_TYPE, "0");

    /**
     * Название аттрибута "Enabled".
     */
    public static final String ENABLED_KEY = "enabled";

    /**
     * Описание аттрибута "Enabled".
     */
    public static final ProviderConfigProperty ENABLED_PROPERTY = new ProviderConfigProperty(
        ENABLED_KEY, "Enabled", "Set if the keys are enabled", BOOLEAN_TYPE, "true");

    /**
     * Название аттрибута "Active".
    */
    public static final String ACTIVE_KEY = "active";

    /**
     * Описание аттрибута "Active".
     */
    public static final ProviderConfigProperty ACTIVE_PROPERTY = new ProviderConfigProperty(
        ACTIVE_KEY, "Active", "Set if the keys can be used for signing", BOOLEAN_TYPE, "true");

    /**
     * Название аттрибута "KeyStore".
     */
    public static final String KEYSTORE_KEY = "keystore";

    /**
     * Описание аттрибута "KeyStore".
     */
    public static final ProviderConfigProperty KEYSTORE_PROPERTY = new ProviderConfigProperty(
        KEYSTORE_KEY, "Keystore", "Key store name", STRING_TYPE, null);

    /**
     * Название аттрибута "Keystore Password".
     */
    public static final String KEYSTORE_PASSWORD_KEY = "keystorePassword";

    /**
     * Описание аттрибута "Keystore Password".
     */
    public static final ProviderConfigProperty KEYSTORE_PASSWORD_PROPERTY = new ProviderConfigProperty(
        KEYSTORE_PASSWORD_KEY, "Keystore Password", "Password for the keys", STRING_TYPE, null, true);

    /**
     * Название аттрибута "Alias".
     */
    public static final String KEY_ALIAS_KEY = "keyAlias";

    /**
     * Описание аттрибута "Alias".
     */
    public static final ProviderConfigProperty KEY_ALIAS_PROPERTY = new ProviderConfigProperty(
        KEY_ALIAS_KEY, "Key Alias", "Alias for the private key", STRING_TYPE, null);

    /**
     * Название аттрибута "Algorithm".
     */
    public static final String ALGORITHM_KEY = "algorithm";

    /**
     * Описание аттрибута "Algorithm".
     */
    public static final ProviderConfigProperty ALGORITHM_PROPERTY = new ProviderConfigProperty(
        ALGORITHM_KEY, "Algorithm", "Intended algorithm for the key", LIST_TYPE,
        GOSTAlgorithm.GOST_SIGN_2012_256_DISPLAY_NAME,
        GOSTAlgorithm.GOST_SIGN_2012_256_DISPLAY_NAME, GOSTAlgorithm.GOST_SIGN_2012_512_DISPLAY_NAME);

    private GOSTAttribute() {

    }
}
