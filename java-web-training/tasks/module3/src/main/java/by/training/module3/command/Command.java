package by.training.module3.command;

import java.util.List;

public interface Command<T> {
    List<T> build(String path) throws CommandException;
}
