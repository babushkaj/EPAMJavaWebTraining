package by.training.hospital.validator;

import javax.servlet.http.HttpServletRequest;

public class ShowWriteVisitReasonValidator implements Validator {
    @Override
    public ValidationResult validate(HttpServletRequest req) {
        ValidationResult validationResult = new ValidationResult();
        String date = req.getParameter("visitTime");
        String doctorUserId = req.getParameter("doctorUserId");
        if (!ValidationUtil.thisStringIsPositiveLong(date) || !ValidationUtil.thisStringIsPositiveLong(doctorUserId)) {
            req.setAttribute("message", "error.incorrect_date_or_doctor_id");
        }
        return validationResult;
    }
}
