package net.exotia.developer.kit.core.actions;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ActionService {
    private final List<Action> actions = new ArrayList<>();

    public void register(Action action) {
        this.actions.add(action);
    }

    public void parse(ActionSection actionSection, Player player) {
        Action action = this.findAction(actionSection);
        action.execute(player, actionSection.value);
    }
    public void parse(List<ActionSection> actionSections, Player player) {
        actionSections.forEach(actionSection -> {
            this.parse(actionSection, player);
        });
    }

    private Action findAction(ActionSection actionSection) {
        return this.actions.stream().filter(action -> action.identifier().equals(actionSection.identifier)).findFirst().orElse(null);
    }
}
