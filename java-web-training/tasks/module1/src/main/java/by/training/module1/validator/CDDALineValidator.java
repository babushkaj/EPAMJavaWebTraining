package by.training.module1.validator;

import by.training.module1.model.Compression;

import java.util.Map;
import java.util.Optional;

public class CDDALineValidator extends MainLineValidator {

    @Override
    public ValidationResult validateAllFields(Map<String, String> oneModelInfo, ValidationResult vr) {

        if (oneModelInfo.get("comp") != null) {
            validateCompression(oneModelInfo.get("comp").trim(), vr);
        } else {
            vr.addErrorMessage("comp", "Field \'comp\' isn't found.");
        }

        return vr;
    }

    private void validateCompression(String comp, ValidationResult vr) {
        Optional<Compression> compOptional = Compression.fromString(comp);
        if (compOptional.isPresent()) {
            return;
        }
        if (comp.isEmpty()) {
            vr.addErrorMessage("comp", "Field \'comp\' is empty.");
            return;
        }
        vr.addErrorMessage("comp", "Value of \'comp\' field is wrong (current value is \'" + comp + "\').");
    }
}
