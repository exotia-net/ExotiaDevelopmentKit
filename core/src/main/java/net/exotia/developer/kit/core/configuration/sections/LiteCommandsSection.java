package net.exotia.developer.kit.core.configuration.sections;

import eu.okaeri.configs.OkaeriConfig;

public class LiteCommandsSection extends OkaeriConfig {
    private String invalidCommandUsage = "&cNie poprawne użycie komendy &8>> &7{command}";
    private String invalidCommandUsageHeader = "&cNie poprawne użycie komendy!";
    private String invalidCommandUsageBody = "&8 >> &7{usage}";
    private String unauthorizedCommand = "&cNie masz permisji do tej komendy! &7({permission})";
    private String playerIsOffline = "&8&l>> &cTen gracz jest offline!";
    private String commandOnlyForPlayers = "&8&l>> &cTej komendy nie mozesz wywolac z poziomu konsoli!";

    public String getInvalidCommandUsage() {
        return invalidCommandUsage;
    }

    public String getInvalidCommandUsageHeader() {
        return invalidCommandUsageHeader;
    }

    public String getInvalidCommandUsageBody() {
        return invalidCommandUsageBody;
    }

    public String getUnauthorizedCommand() {
        return unauthorizedCommand;
    }

    public String getPlayerIsOffline() {
        return playerIsOffline;
    }

    public String getCommandOnlyForPlayers() {
        return commandOnlyForPlayers;
    }
}
