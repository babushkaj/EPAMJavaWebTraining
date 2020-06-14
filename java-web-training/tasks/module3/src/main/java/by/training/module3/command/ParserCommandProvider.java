package by.training.module3.command;

import by.training.module3.entity.Gem;

import java.util.HashMap;
import java.util.Map;

public class ParserCommandProvider implements CommandProvider<Gem> {

    private Map<CommandType, Command<Gem>> commands;

    public ParserCommandProvider() {
        commands = new HashMap<>();
    }

    @Override
    public Command<Gem> getCommand(CommandType type) {
        return commands.get(type);
    }

    @Override
    public void register(CommandType type, Command<Gem> command) {
        commands.put(type, command);
    }
}
