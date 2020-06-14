package by.training.hospital.validator;

import javax.servlet.http.HttpServletRequest;

public class FinishVisitValidator implements Validator {
    @Override
    public ValidationResult validate(HttpServletRequest req) {
        ValidationResult validationResult = new ValidationResult();
        String visitIdParameter = req.getParameter("visitId");
        if (!ValidationUtil.thisStringIsPositiveLong(visitIdParameter)) {
            validationResult.addErrorMessage("message", "error.incorrect_visit_id_format");
        }
        return validationResult;
    }
}
