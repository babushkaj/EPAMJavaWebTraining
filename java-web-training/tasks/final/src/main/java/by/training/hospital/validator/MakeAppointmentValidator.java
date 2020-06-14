package by.training.hospital.validator;

import javax.servlet.http.HttpServletRequest;

public class MakeAppointmentValidator implements Validator {
    @Override
    public ValidationResult validate(HttpServletRequest req) {
        ValidationResult validationResult = new ValidationResult();
        String dateParameter = req.getParameter("visitTime");
        String doctorUserIdParameter = req.getParameter("doctorUserId");
        if (!ValidationUtil.thisStringIsPositiveLong(dateParameter) || !ValidationUtil.thisStringIsPositiveLong(doctorUserIdParameter)) {
            req.setAttribute("message", "error.incorrect_date_or_doctor_id");
        }
        return validationResult;
    }
}
