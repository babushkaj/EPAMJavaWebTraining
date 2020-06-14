package by.training.hospital.command;

import by.training.hospital.dto.AddressDTO;
import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.service.*;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.ShowProfileValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowProfileCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ShowProfileCommand.class);

    private UserService userService;
    private AddressService addressService;
    private UserInfoService userInfoService;
    private DoctorInfoService doctorInfoService;

    public ShowProfileCommand(UserService userService, AddressService addressService,
                              UserInfoService userInfoService, DoctorInfoService doctorInfoService) {
        this.userService = userService;
        this.addressService = addressService;
        this.userInfoService = userInfoService;
        this.doctorInfoService = doctorInfoService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Validator validator = new ShowProfileValidator();
            ValidationResult validationResult = validator.validate(req);
            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            long userId = defineUserId(req, resp);
            if (userId == -1) {
                RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_user_id_format");
                return;
            }

            UserDTO userDTO = userService.getById(userId);
            if (userDTO.getRole() == Role.ADMIN) {
                RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_show_admin_profile");
                return;
            }
            AddressDTO addressDTO = addressService.getByUserId(userDTO.getId());
            UserInfoDTO userInfoDTO = userInfoService.getUserInfoByUserId(userDTO.getId());
            userDTO.setAddress(addressDTO);
            userDTO.setUserInfo(userInfoDTO);

            if (userDTO.getRole() == Role.DOCTOR) {
                DoctorInfoDTO doctorInfoDTO = doctorInfoService.getDoctorInfoByUserId(userDTO.getId());
                userDTO.setDoctorInfo(doctorInfoDTO);
            }

            req.setAttribute("user", userDTO);

            RequestUtil.forwardToJsp(req, resp, "user_page");
        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete user in ShowProfileCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_show_page_by_user_id");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing ShowProfileCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private long defineUserId(HttpServletRequest req, HttpServletResponse resp) {
        Long userId;
        Long currentUserId = (Long) req.getSession().getAttribute("currentUserId");
        Role currentUserRole = (Role) req.getSession().getAttribute("currentUserRole");

        if (currentUserRole == Role.ADMIN) {
            String userIdParameter = req.getParameter("userId");
            if (userIdParameter == null || userIdParameter.isEmpty()) {
                return -1;
            }
            userId = Long.parseLong(userIdParameter);
        } else {
            userId = currentUserId;
        }
        return userId;
    }
}
