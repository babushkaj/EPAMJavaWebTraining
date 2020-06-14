package by.training.hospital.validator;

import javax.servlet.http.HttpServletRequest;

public class BlockUserValidator implements Validator {

    @Override
    public ValidationResult validate(HttpServletRequest req) {
        ValidationResult validationResult = new ValidationResult();
        String userIdParameter = req.getParameter("userId");
        if (!ValidationUtil.thisStringIsPositiveLong(userIdParameter)) {
            validationResult.addErrorMessage("message", "error.incorrect_user_id_format");
        }
        return validationResult;
    }
}
