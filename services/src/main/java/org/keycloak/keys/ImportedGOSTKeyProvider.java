package org.keycloak.keys;

import java.util.Collections;
import java.util.List;

import org.keycloak.component.ComponentModel;
import org.keycloak.crypto.KeyWrapper;
import org.keycloak.keys.loader.GOSTKeyStore;

/**
 * Провадер импорта ключей из JCP.
 *
 * @author Anatoliy Pokhresnyi
 */
public class ImportedGOSTKeyProvider implements KeyProvider {

    /**
     * Список ключей.
     */
    private final List<KeyWrapper> keys;

    /**
     * Создает провайдер  импорта ключей из JCP.
     * @param model Данные необзодимые для подключения к существующему контейнеру.
     */
    public ImportedGOSTKeyProvider(final ComponentModel model) {
        if (!model.hasNote(KeyWrapper.class.getName())) {
            model.setNote(KeyWrapper.class.getName(), new GOSTKeyStore(model).key());
        }

        this.keys = Collections.singletonList(model.getNote(KeyWrapper.class.getName()));
    }

    @Override
    public List<KeyWrapper> getKeys() {
        return keys;
    }
}
