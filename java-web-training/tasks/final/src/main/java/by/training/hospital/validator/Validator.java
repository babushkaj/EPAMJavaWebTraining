package by.training.hospital.validator;

import javax.servlet.http.HttpServletRequest;

public interface Validator {
    ValidationResult validate(HttpServletRequest req);
}
