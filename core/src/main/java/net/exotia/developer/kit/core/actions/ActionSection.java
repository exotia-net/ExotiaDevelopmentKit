package net.exotia.developer.kit.core.actions;

import eu.okaeri.configs.OkaeriConfig;

public class ActionSection extends OkaeriConfig {
    public String identifier;
    public String value;

    public ActionSection(String identifier, String value) {
        this.identifier = identifier;
        this.value = value;
    }
}
