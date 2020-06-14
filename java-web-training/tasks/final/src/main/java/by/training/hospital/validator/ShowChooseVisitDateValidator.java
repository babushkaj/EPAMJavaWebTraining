package by.training.hospital.validator;

import javax.servlet.http.HttpServletRequest;

public class ShowChooseVisitDateValidator implements Validator {
    @Override
    public ValidationResult validate(HttpServletRequest req) {
        ValidationResult validationResult = new ValidationResult();
        String doctorUserIdParameter = req.getParameter("doctorUserId");
        if (!ValidationUtil.thisStringIsPositiveLong(doctorUserIdParameter)) {
            validationResult.addErrorMessage("error", "error.incorrect_user_id_format");
        }
        return validationResult;
    }
}
