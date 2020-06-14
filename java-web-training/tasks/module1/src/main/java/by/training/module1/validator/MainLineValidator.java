package by.training.module1.validator;

import by.training.module1.model.Style;

import java.util.Map;
import java.util.Optional;

public abstract class MainLineValidator implements LineValidator {

    public ValidationResult validate(Map<String, String> string) {
        ValidationResult vr = validateMainFields(string);
        return validateAllFields(string, vr);

    }

    abstract public ValidationResult validateAllFields(Map<String, String> string, ValidationResult vr);

    private ValidationResult validateMainFields(Map<String, String> string) {

        ValidationResult vr = new ValidationResult();

        if (string.get("author") != null) {
            validateAuthor(string.get("author"), vr);
        } else {
            vr.addErrorMessage("author", "Field \'author\' isn't found.");
        }

        if (string.get("title") != null) {
            validateTitle(string.get("title"), vr);
        } else {
            vr.addErrorMessage("title", "Field \'title\' isn't found.");
        }

        if (string.get("duration") != null) {
            validateDuration(string.get("duration").trim(), vr);
        } else {
            vr.addErrorMessage("duration", "Field \'duration\' isn't found.");
        }

        if (string.get("style") != null) {
            validateStyle(string.get("style").trim(), vr);
        } else {
            vr.addErrorMessage("style", "Field \'style\' isn't found.");
        }

        return vr;
    }

    private void validateAuthor(String author, ValidationResult vr) {
        if (author.isEmpty()) {
            vr.addErrorMessage("author", "Field \'author\' is empty.");
        }
    }

    private void validateTitle(String title, ValidationResult vr) {
        if (title.isEmpty()) {
            vr.addErrorMessage("title", "Field \'author\' is empty.");
        }
    }

    private void validateStyle(String style, ValidationResult vr) {
        Optional<Style> styleOptional = Style.fromString(style);
        if (styleOptional.isPresent()) {
            return;
        }
        if (style.isEmpty()) {
            vr.addErrorMessage("style", "Field \'style\' is empty.");
            return;
        }
        vr.addErrorMessage("style", "Value of \'style\' field is wrong (current value is \'" + style + "\').");
    }

    private void validateDuration(String duration, ValidationResult vr) {
        int parsedDuration;
        try {
            parsedDuration = Integer.parseInt(duration);
            if (parsedDuration <= 0) {
                vr.addErrorMessage("duration", "The value of \'duration\' field must be greater than 0 " +
                        "(current value is \'" + duration + "\').");
            }
        } catch (NumberFormatException e) {
            vr.addErrorMessage("duration", "The value of \'duration\' field must be INT type (current value " +
                    "is \'" + duration + "\').");
        }
    }
}
