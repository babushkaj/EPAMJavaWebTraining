package by.training.module3.command;

public interface CommandProvider<T> {
    Command<T> getCommand(CommandType type);

    void register(CommandType type, Command<T> command);
}
