package org.keycloak.keys;

import java.util.List;

import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.gost.GOSTAttribute;
import org.keycloak.keys.loader.GOSTKeyStore;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ConfigurationValidationHelper;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

/**
 * Провадер импорта ключей из JCP.
 *
 * @author Anatoliy Pokhresnyi
 */
public class ImportedGOSTKeyProviderFactory implements KeyProviderFactory {

    /**
     * Название провайдера.
     */
    public static final String ID = "gost-keystore";

    /**
     * Описание провайдера.
     */
    private static final String HELP_TEXT = "GOST key provider that can optionally generated a self-signed certificate";

    /**
     * Список настроек провайдера.
     */
    private static final List<ProviderConfigProperty> CONFIG_PROPERTIES = ProviderConfigurationBuilder
        .create()
        .property(GOSTAttribute.PRIORITY_PROPERTY)
        .property(GOSTAttribute.ENABLED_PROPERTY)
        .property(GOSTAttribute.ACTIVE_PROPERTY)
        .property(GOSTAttribute.ALGORITHM_PROPERTY)
        .property(GOSTAttribute.KEYSTORE_PROPERTY)
        .property(GOSTAttribute.KEYSTORE_PASSWORD_PROPERTY)
        .property(GOSTAttribute.KEY_ALIAS_PROPERTY)
        .build();

    @Override
    public KeyProvider create(KeycloakSession session, ComponentModel model) {
        return new ImportedGOSTKeyProvider(model);
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel model)
    throws ComponentValidationException {
        ConfigurationValidationHelper.check(model)
                                     .checkLong(GOSTAttribute.PRIORITY_PROPERTY, false)
                                     .checkBoolean(GOSTAttribute.ENABLED_PROPERTY, false)
                                     .checkBoolean(GOSTAttribute.ACTIVE_PROPERTY, false)
                                     .checkSingle(GOSTAttribute.KEYSTORE_PROPERTY, true)
                                     .checkSingle(GOSTAttribute.KEYSTORE_PASSWORD_PROPERTY, true)
                                     .checkSingle(GOSTAttribute.KEY_ALIAS_PROPERTY, true);

        try {
            new GOSTKeyStore(model).key();
        } catch (Throwable t) {
            throw new ComponentValidationException("Failed to load keys. " + t.getMessage(), t);
        }
    }

    @Override
    public String getHelpText() {
        return HELP_TEXT;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG_PROPERTIES;
    }

    @Override
    public String getId() {
        return ID;
    }
}
