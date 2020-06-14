package by.training.hospital.validator;

import javax.servlet.http.HttpServletRequest;

public class GetDoctorsValidator implements Validator {
    @Override
    public ValidationResult validate(HttpServletRequest req) {
        ValidationResult validationResult = new ValidationResult();
        String pageNumberParameter = req.getParameter("pageNumber");
        if (pageNumberParameter != null && !pageNumberParameter.isEmpty()) {
            if (!ValidationUtil.thisStringIsPositiveLong(pageNumberParameter)) {
                validationResult.addErrorMessage("message", "error.incorrect_page_number_format");
            }
        }
        return validationResult;
    }
}
