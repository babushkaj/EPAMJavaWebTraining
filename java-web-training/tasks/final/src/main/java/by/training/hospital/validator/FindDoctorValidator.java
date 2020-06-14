package by.training.hospital.validator;

import by.training.hospital.entity.Specialization;

import javax.servlet.http.HttpServletRequest;

public class FindDoctorValidator implements Validator {
    @Override
    public ValidationResult validate(HttpServletRequest req) {
        ValidationResult validationResult = new ValidationResult();
        String specSelectParameter = req.getParameter("specSelect");
        try {
            if (specSelectParameter != null && !specSelectParameter.isEmpty()) {
                Specialization.valueOf(specSelectParameter);
            }
        } catch (IllegalArgumentException e) {
            validationResult.addErrorMessage("message", "error.invalid_spec_format");
        }
        return validationResult;
    }
}
