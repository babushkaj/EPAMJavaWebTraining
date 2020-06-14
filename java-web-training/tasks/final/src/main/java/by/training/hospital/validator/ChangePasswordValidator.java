package by.training.hospital.validator;

import javax.servlet.http.HttpServletRequest;

public class ChangePasswordValidator implements Validator {
    @Override
    public ValidationResult validate(HttpServletRequest req) {

        ValidationResult vr = new ValidationResult();

        String newPassword1 = req.getParameter("newPassword1");
        String newPassword2 = req.getParameter("newPassword2");

        if (!newPassword1.equals(newPassword2)) {
            vr.addErrorMessage("passwordErr", "error.diff_pass");
        } else if (newPassword1.length() < 8 || newPassword1.length() > 25) {
            vr.addErrorMessage("passwordErr", "error.pass_length");
        }

        return vr;
    }
}
