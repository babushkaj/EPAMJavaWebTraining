package by.training.module3.validation;

import java.io.File;

public class FileValidator {

    public ValidationResult validate(String path) {
        ValidationResult vr = new ValidationResult();

        File file = new File(path);
        if (!file.exists()) {
            vr.addErrorMessage("exist", "File doesn't exist.");
            return vr;
        }

        if (file.length() == 0) {
            vr.addErrorMessage("size", "File is empty.");
            return vr;
        }

        return vr;
    }
}
