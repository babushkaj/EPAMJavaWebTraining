package by.training.hospital.command;

import by.training.hospital.dto.AddressDTO;
import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.entity.Specialization;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.UserInfoService;
import by.training.hospital.service.UserService;
import by.training.hospital.util.MD5Util;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.EditUserValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

public class RegistrationCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(RegistrationCommand.class);

    private UserService userService;
    private UserInfoService userInfoService;

    public RegistrationCommand(UserService userService, UserInfoService userInfoService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("post")) {
            processPost(req, resp);
        } else {
            processGet(req, resp);
        }
    }

    private void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.SHOW_EDIT_USER_CMD);
    }

    private void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            Validator validator = new EditUserValidator();
            ValidationResult validationResult = validator.validate(req);
            Role currentUserRole = (Role) req.getSession().getAttribute("currentUserRole");

            boolean isLoginUnique = checkIsLoginUnique(req);
            boolean isEmailUnique = checkIsEmailUnique(req);

            if (!validationResult.isValid() || !isLoginUnique || !isEmailUnique) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                setRequestAttributes(req, currentUserRole);
                RequestUtil.forwardToJsp(req, resp, "edit_user");
            } else {
                UserDTO userDTO = buildUserDTO(req, currentUserRole);
                userService.saveNewUser(userDTO);
                if (currentUserRole == Role.ADMIN) {
                    resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.SHOW_USERS_CMD);
                } else {
                    resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.LOGIN_CMD);
                }
            }
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing RegistrationCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.error_save");
        }
    }

    private UserDTO buildUserDTO(HttpServletRequest req, Role currentUserRole) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(req.getParameter("login"));
        userDTO.setPassword(MD5Util.getMD5String(req.getParameter("password1")));
        if (currentUserRole != null && currentUserRole == Role.ADMIN) {
            userDTO.setRole(Role.DOCTOR);
        } else {
            userDTO.setRole(Role.VISITOR);
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setFirstName(req.getParameter("firstname"));
        userInfoDTO.setLastName(req.getParameter("lastname"));
        userInfoDTO.setEmail(req.getParameter("email"));
        userInfoDTO.setPhone(req.getParameter("phone"));

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setRegion(req.getParameter("region"));
        addressDTO.setCity(req.getParameter("city"));
        addressDTO.setStreet(req.getParameter("street"));
        addressDTO.setHouse(req.getParameter("house"));
        addressDTO.setApartment(req.getParameter("apartment"));

        DoctorInfoDTO doctorInfoDTO = new DoctorInfoDTO();
        if (currentUserRole != null && currentUserRole == Role.ADMIN) {
            Set<DayOfWeek> workingDays = new HashSet<>();
            for (DayOfWeek d : DayOfWeek.values()) {
                String day = req.getParameter(d.name());
                if (day != null && !day.isEmpty()) {
                    workingDays.add(d);
                }
            }
            doctorInfoDTO.setDescription(req.getParameter("description"));
            doctorInfoDTO.setSpec(Specialization.valueOf(req.getParameter("specSelect")));
            doctorInfoDTO.setWorkingDays(workingDays);
        }

        userDTO.setUserInfo(userInfoDTO);
        userDTO.setAddress(addressDTO);
        userDTO.setUserInfo(userInfoDTO);
        if (currentUserRole != null && currentUserRole == Role.ADMIN) {
            userDTO.setDoctorInfo(doctorInfoDTO);
        }

        return userDTO;
    }

    private void setRequestAttributes(HttpServletRequest req, Role currentUserRole) {
        req.setAttribute("userLogin", req.getParameter("login"));
        req.setAttribute("userFirstName", req.getParameter("firstname"));
        req.setAttribute("userLastName", req.getParameter("lastname"));
        req.setAttribute("userPhone", req.getParameter("phone"));
        req.setAttribute("userEmail", req.getParameter("email"));
        req.setAttribute("userRegion", req.getParameter("region"));
        req.setAttribute("userCity", req.getParameter("city"));
        req.setAttribute("userStreet", req.getParameter("street"));
        req.setAttribute("userHouse", req.getParameter("house"));
        req.setAttribute("userApartment", req.getParameter("apartment"));

        if (currentUserRole != null && currentUserRole == Role.ADMIN) {

            Set<DayOfWeek> workingDays = new HashSet<>();
            for (DayOfWeek d : DayOfWeek.values()) {
                String day = req.getParameter(d.name());
                if (day != null && !day.isEmpty()) {
                    workingDays.add(d);
                }
            }
            req.setAttribute("workingDays", workingDays);
            req.setAttribute("docSpec", req.getParameter("specSelect"));
            req.setAttribute("description", req.getParameter("description"));
        }

        req.setAttribute("action", ApplicationCommandConstants.REGISTRATION_CMD);
    }

    private boolean checkIsLoginUnique(HttpServletRequest req) throws ServiceException {
        boolean isLoginUnique = userService.isLoginUnique(req.getParameter("login"));
        if (!isLoginUnique) {
            req.setAttribute("loginErr", "error.not_unique_login");
        }
        return isLoginUnique;
    }

    private boolean checkIsEmailUnique(HttpServletRequest req) throws ServiceException {
        boolean isEmailUnique = userInfoService.isEmailUnique(req.getParameter("email"));
        if (!isEmailUnique) {
            req.setAttribute("emailErr", "error.not_unique_email");
        }
        return isEmailUnique;
    }
}
