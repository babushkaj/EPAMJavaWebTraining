package by.training.module1.validator;

import java.io.File;

public class FileValidator {

    public ValidationResult validatePath(String path) {
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
