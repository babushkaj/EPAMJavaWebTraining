package by.training.hospital.validator;

import by.training.hospital.entity.Specialization;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

public class EditUserValidator implements Validator {

    private static final String LOGIN_REGEX = "^[A-Za-z0-9_-]{5,25}$";
    private static final String NAME_REGEX = "^[A-Za-zА-Яа-я-]{1,25}$";
    private static final String PHONE_REGEX = "^((29)|(33)|(44)|(25))-(([1-9]{1})([0-9]{6}))$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]{5,18}@[A-Za-z]{4,8}\\.[A-Za-z]{2,4}$";
    private static final String REGION_REGEX = "^[А-Я]{1}[а-яё]{4,12}$";
    private static final String CITY_STREET_REGEX = "^[А-Яа-яё\\.-]{4,25}$";
    private static final String HOUSE_APARTMENT_REGEX = "^([1-9]{1})([0-9А-Яа-яё\\.\\/-]){0,4}$";


    public EditUserValidator() {

    }

    @Override
    public ValidationResult validate(HttpServletRequest req) {
        ValidationResult vr = new ValidationResult();

        String loginParameter = req.getParameter("login");
        if (loginParameter != null && !loginParameter.isEmpty()) {
            boolean result = ValidationUtil.thisStringMatchesRegex(loginParameter, LOGIN_REGEX);
            if (!result) {
                vr.addErrorMessage("loginErr", "error.login_not_match_regex");
            }
        } else {
            vr.addErrorMessage("loginErr", "error.not_null_field");
        }

        String password1 = req.getParameter("password1");
        String password2 = req.getParameter("password2");

        if (password1 != null && password2 != null) {
            if (!password1.equals(password2)) {
                vr.addErrorMessage("passwordErr", "error.diff_pass");
            } else if (password1.length() < 8 || password1.length() > 25) {
                vr.addErrorMessage("passwordErr", "error.pass_length");
            }
        }

        String firstnameParameter = req.getParameter("firstname");
        if (firstnameParameter != null && !firstnameParameter.isEmpty()) {
            boolean result = ValidationUtil.thisStringMatchesRegex(firstnameParameter, NAME_REGEX);
            if (!result) {
                vr.addErrorMessage("firstnameErr", "error.firstname_not_match_regex");
            }
        } else {
            vr.addErrorMessage("firstnameErr", "error.not_null_field");
        }

        String lastnameParameter = req.getParameter("lastname");
        if (lastnameParameter != null && !lastnameParameter.isEmpty()) {
            boolean result = ValidationUtil.thisStringMatchesRegex(lastnameParameter, NAME_REGEX);
            if (!result) {
                vr.addErrorMessage("lastnameErr", "error.lastname_not_match_regex");
            }
        } else {
            vr.addErrorMessage("lastnameErr", "error.not_null_field");
        }

        String phoneParameter = req.getParameter("phone");
        if (phoneParameter != null && !phoneParameter.isEmpty()) {
            boolean result = ValidationUtil.thisStringMatchesRegex(phoneParameter, PHONE_REGEX);
            if (!result) {
                vr.addErrorMessage("phoneErr", "error.phone_not_match_regex");
            }
        } else {
            vr.addErrorMessage("phoneErr", "error.not_null_field");
        }

        String emailParameter = req.getParameter("email");
        if (emailParameter != null && !emailParameter.isEmpty()) {
            boolean result = ValidationUtil.thisStringMatchesRegex(emailParameter, EMAIL_REGEX);
            if (!result) {
                vr.addErrorMessage("emailErr", "error.email_not_match_regex");
            }
        } else {
            vr.addErrorMessage("emailErr", "error.not_null_field");
        }

        String regionParameter = req.getParameter("region");
        if (regionParameter != null && !regionParameter.isEmpty()) {
            boolean result = ValidationUtil.thisStringMatchesRegex(regionParameter, REGION_REGEX);
            if (!result) {
                vr.addErrorMessage("regionErr", "error.region_not_match_regex");
            }
        } else {
            vr.addErrorMessage("regionErr", "error.not_null_field");
        }

        String cityParameter = req.getParameter("city");
        if (cityParameter != null && !cityParameter.isEmpty()) {
            boolean result = ValidationUtil.thisStringMatchesRegex(cityParameter, CITY_STREET_REGEX);
            if (!result) {
                vr.addErrorMessage("cityErr", "error.city_not_match_regex");
            }
        } else {
            vr.addErrorMessage("cityErr", "error.not_null_field");
        }

        String streetParameter = req.getParameter("street");
        if (streetParameter != null && !streetParameter.isEmpty()) {
            boolean result = ValidationUtil.thisStringMatchesRegex(streetParameter, CITY_STREET_REGEX);
            if (!result) {
                vr.addErrorMessage("streetErr", "error.street_not_match_regex");
            }
        } else {
            vr.addErrorMessage("streetErr", "error.not_null_field");
        }

        String houseParameter = req.getParameter("house");
        if (houseParameter != null && !houseParameter.isEmpty()) {
            boolean result = ValidationUtil.thisStringMatchesRegex(houseParameter, HOUSE_APARTMENT_REGEX);
            if (!result) {
                vr.addErrorMessage("houseErr", "error.house_not_match_regex");
            }
        } else {
            vr.addErrorMessage("houseErr", "error.not_null_field");
        }

        String apartmentParameter = req.getParameter("apartment");
        if (apartmentParameter != null && !apartmentParameter.isEmpty()) {
            boolean result = ValidationUtil.thisStringMatchesRegex(houseParameter, HOUSE_APARTMENT_REGEX);
            if (!result) {
                vr.addErrorMessage("apartmentErr", "error.apartment_not_match_regex");
            }
        }

        String specParameter = req.getParameter("specSelect");
        if (specParameter != null && !specParameter.isEmpty()) {
            try {
                Specialization.valueOf(specParameter);
            } catch (IllegalArgumentException e) {
                vr.addErrorMessage("specErr", "error.invalid_spec_format");
            }
        }

        Set<DayOfWeek> workingDays = new HashSet<>();
        for (DayOfWeek d : DayOfWeek.values()) {
            String day = req.getParameter(d.name());
            if (day != null && !day.isEmpty()) {
                workingDays.add(d);
            }
        }
        if (!workingDays.isEmpty()) {
            if (workingDays.size() != 5) {
                vr.addErrorMessage("daysErr", "error.not_five_working_days");
            }
        }

        return vr;
    }
}
