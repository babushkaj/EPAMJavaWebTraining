package by.training.module3.controller;

import by.training.module3.annotation.ValidAnnotationHandler;
import by.training.module3.command.Command;
import by.training.module3.command.CommandException;
import by.training.module3.command.CommandType;
import by.training.module3.command.ParserCommandProvider;
import by.training.module3.entity.Gem;
import by.training.module3.service.GemsService;
import by.training.module3.validation.FileValidator;
import by.training.module3.validation.ValidationResult;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class GemsController {
    private static final Logger LOGGER = LogManager.getLogger(GemsController.class);

    private FileValidator fileValidator;
    private ParserCommandProvider commandProvider;
    private GemsService service;
    private ValidAnnotationHandler annotationHandler;

    public GemsController(FileValidator fileValidator, ParserCommandProvider commandProvider,
                          GemsService service, ValidAnnotationHandler annotationHandler) {
        this.fileValidator = fileValidator;
        this.commandProvider = commandProvider;
        this.service = service;
        this.annotationHandler = annotationHandler;
    }

    public void processFile(String xmlPath, CommandType commandType) {
        ValidationResult fileValidationResult = fileValidator.validate(xmlPath);

        if (!fileValidationResult.isValid()) {
            LOGGER.error(fileValidationResult.getErrorsAsString());
            return;
        }

        Command<Gem> command = commandProvider.getCommand(commandType);

        List<Gem> gems;
        try {
            gems = command.build(xmlPath);
        } catch (CommandException e) {
            LOGGER.error("Error in controller level.", e);
            throw new ControllerException(e);
        }

        List<Gem> annotatedGems = gems.stream()
                .filter(annotationHandler::isValid)
                .collect(Collectors.toList());

        annotatedGems.forEach(g -> service.add(g));

    }
}
