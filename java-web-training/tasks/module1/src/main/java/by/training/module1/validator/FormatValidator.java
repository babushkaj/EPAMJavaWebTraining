package by.training.module1.validator;

import by.training.module1.model.Format;

import java.util.Map;
import java.util.Optional;

public class FormatValidator {

    public ValidationResult validate(Map<String, String> oneModelInfo) {

        ValidationResult vr = new ValidationResult();

        String format = oneModelInfo.get("format");
        if (format == null) {
            vr.addErrorMessage("format", "Field \'format\' isn't found. The field is required.");
            return vr;
        }

        Optional<Format> optionalFormat = Format.fromString(oneModelInfo.get("format"));

        if (!optionalFormat.isPresent()) {
            vr.addErrorMessage("format", "Value of \'format\' field is wrong (current value is \'" + format +
                    "\'). The field is required.");
            return vr;
        }

        return vr;
    }

}
