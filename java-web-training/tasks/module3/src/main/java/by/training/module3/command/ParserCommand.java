package by.training.module3.command;

import by.training.module3.parser.Parser;
import by.training.module3.parser.ParserException;
import by.training.module3.validation.XMLbyXSDValidator;

import java.util.List;

public class ParserCommand<T> implements Command<T> {

    private XMLbyXSDValidator validator;
    private Parser<T> parser;

    public ParserCommand(XMLbyXSDValidator validator, Parser<T> parser) {
        this.validator = validator;
        this.parser = parser;
    }

    @Override
    public List<T> build(String path) throws CommandException {
        if (!validator.validate(path)) {
            throw new CommandException("File with path \'" + path + "\' is invalid.");
        }
        try {
            return parser.parse(path);
        } catch (ParserException e) {
            throw new CommandException(e);
        }
    }
}
