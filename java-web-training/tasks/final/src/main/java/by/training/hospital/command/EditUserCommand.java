package by.training.hospital.command;

import by.training.hospital.dto.AddressDTO;
import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.entity.Specialization;
import by.training.hospital.service.*;
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

public class EditUserCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(EditUserCommand.class);

    private UserService userService;
    private UserInfoService userInfoService;
    private AddressService addressService;
    private DoctorInfoService doctorInfoService;

    public EditUserCommand(UserService userService, UserInfoService userInfoService,
                           AddressService addressService, DoctorInfoService doctorInfoService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.addressService = addressService;
        this.doctorInfoService = doctorInfoService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getMethod().equalsIgnoreCase("post")) {
            processPost(req, resp);
        } else {
            processGet(req, resp);
        }
    }

    private void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RequestUtil.forwardToJsp(req, resp, "error", "error.unsupported_get_method");
    }

    private void processPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Validator validator = new EditUserValidator();
            ValidationResult validationResult = validator.validate(req);

            long userId = (Long) req.getSession().getAttribute("lastEditedUserId");
            UserDTO editedUserDTO = userService.getById(userId);
            UserInfoDTO editedUserInfoDTO = userInfoService.getUserInfoByUserId(userId);

            boolean isLoginUnique = checkIsLoginUnique(req, editedUserDTO);
            boolean isEmailUnique = checkIsEmailUnique(req, editedUserInfoDTO);

            if (!validationResult.isValid() || !isLoginUnique || !isEmailUnique) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                setRequestAttributes(req);
                RequestUtil.forwardToJsp(req, resp, "edit_user");
            } else {
                userService.updateUserWithInfo(buildUserDTO(editedUserDTO, editedUserInfoDTO, req));

                Role currentUserRole = (Role) req.getSession().getAttribute("currentUserRole");
                if (currentUserRole == Role.ADMIN) {
                    resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.SHOW_PROFILE_CMD +
                            "&userId=" + userId);
                } else {
                    resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.SHOW_PROFILE_CMD);
                }
            }
        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete user in EditUserCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing EditUserCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private UserDTO buildUserDTO(UserDTO editedUserDTO, UserInfoDTO editedUserInfoDTO, HttpServletRequest req) throws ServiceException {
        editedUserDTO.setLogin(req.getParameter("login"));
        editedUserInfoDTO.setFirstName(req.getParameter("firstname"));
        editedUserInfoDTO.setLastName(req.getParameter("lastname"));
        editedUserInfoDTO.setEmail(req.getParameter("email"));
        editedUserInfoDTO.setPhone(req.getParameter("phone"));
        AddressDTO editedAddressDTO = addressService.getByUserId(editedUserDTO.getId());
        editedAddressDTO.setRegion(req.getParameter("region"));
        editedAddressDTO.setCity(req.getParameter("city"));
        editedAddressDTO.setStreet(req.getParameter("street"));
        editedAddressDTO.setHouse(req.getParameter("house"));
        editedAddressDTO.setApartment(req.getParameter("apartment"));

        DoctorInfoDTO editedDoctorInfoDTO = null;
        if (editedUserDTO.getRole() == Role.DOCTOR) {
            Set<DayOfWeek> workingDays = new HashSet<>();
            for (DayOfWeek d : DayOfWeek.values()) {
                String day = req.getParameter(d.name());
                if (day != null && !day.isEmpty()) {
                    workingDays.add(d);
                }
            }
            editedDoctorInfoDTO = doctorInfoService.getDoctorInfoByUserId(editedUserDTO.getId());
            editedDoctorInfoDTO.setDescription(req.getParameter("description"));
            editedDoctorInfoDTO.setSpec(Specialization.valueOf(req.getParameter("specSelect")));
            editedDoctorInfoDTO.setWorkingDays(workingDays);
        }

        editedUserDTO.setUserInfo(editedUserInfoDTO);
        editedUserDTO.setAddress(editedAddressDTO);
        editedUserDTO.setDoctorInfo(editedDoctorInfoDTO);
        return editedUserDTO;
    }

    private boolean checkIsLoginUnique(HttpServletRequest req, UserDTO editedUserDTO) throws ServiceException {
        String loginParameter = req.getParameter("login");
        boolean isLoginUnique = true;
        if (!editedUserDTO.getLogin().equals(loginParameter.trim())) {
            isLoginUnique = userService.isLoginUnique(loginParameter);
            if (!isLoginUnique) {
                req.setAttribute("loginErr", "error.not_unique_login");
            }
        }
        return isLoginUnique;
    }

    private boolean checkIsEmailUnique(HttpServletRequest req, UserInfoDTO editedUserInfoDTO) throws ServiceException {
        String emailParameter = req.getParameter("email");
        boolean isEmailUnique = true;
        if (!editedUserInfoDTO.getEmail().equals(emailParameter.trim())) {
            isEmailUnique = userInfoService.isEmailUnique(emailParameter);
            if (!isEmailUnique) {
                req.setAttribute("emailErr", "error.not_unique_email");
            }
        }
        return isEmailUnique;
    }

    private void setRequestAttributes(HttpServletRequest req) {
        req.setAttribute("userId", req.getParameter("userId"));
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
        req.setAttribute("action", ApplicationCommandConstants.EDIT_USER_CMD);
    }
}
