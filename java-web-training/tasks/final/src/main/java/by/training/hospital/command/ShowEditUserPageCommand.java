package by.training.hospital.command;

import by.training.hospital.dto.AddressDTO;
import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.service.*;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.ShowEditUserPageValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowEditUserPageCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ShowEditUserPageCommand.class);

    private UserService userService;
    private AddressService addressService;
    private UserInfoService userInfoService;
    private DoctorInfoService doctorInfoService;

    public ShowEditUserPageCommand(UserService userService, AddressService addressService,
                                   UserInfoService userInfoService, DoctorInfoService doctorInfoService) {
        this.userService = userService;
        this.addressService = addressService;
        this.userInfoService = userInfoService;
        this.doctorInfoService = doctorInfoService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Validator validator = new ShowEditUserPageValidator();
            ValidationResult validationResult = validator.validate(req);
            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            Role roleParameter = (Role) req.getSession().getAttribute("currentUserRole");

            if ((roleParameter == null) || (roleParameter == Role.ADMIN && req.getParameter("userId") == null)) {
                req.setAttribute("action", ApplicationCommandConstants.REGISTRATION_CMD);
                RequestUtil.forwardToJsp(req, resp, "edit_user");
                return;
            }

            long userId = defineUserId(req, resp);
            if (userId == -1) {
                RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_user_id_format");
                return;
            }

            UserDTO userDTO = userService.getById(userId);
            if (userDTO.getRole() == Role.ADMIN) {
                RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_edit_admin");
                return;
            }
            setRequestAttributes(req, userDTO);
            req.getSession().setAttribute("lastEditedUserId", userDTO.getId());
            RequestUtil.forwardToJsp(req, resp, "edit_user");

        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete user in ShowEditUserPageCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_show_page_by_user_id");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing ShowEditUserPageCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private void setRequestAttributes(HttpServletRequest req, UserDTO userDTO) throws ServiceException {
        req.setAttribute("userId", userDTO.getId());
        req.setAttribute("userLogin", userDTO.getLogin());
        req.setAttribute("userRole", userDTO.getRole());

        UserInfoDTO userInfoDTO = userInfoService.getUserInfoByUserId(userDTO.getId());
        req.setAttribute("userFirstName", userInfoDTO.getFirstName());
        req.setAttribute("userLastName", userInfoDTO.getLastName());
        req.setAttribute("userPhone", userInfoDTO.getPhone());
        req.setAttribute("userEmail", userInfoDTO.getEmail());

        AddressDTO addressDTO = addressService.getByUserId(userDTO.getId());
        req.setAttribute("userRegion", addressDTO.getRegion());
        req.setAttribute("userCity", addressDTO.getCity());
        req.setAttribute("userStreet", addressDTO.getStreet());
        req.setAttribute("userHouse", addressDTO.getHouse());
        req.setAttribute("userApartment", addressDTO.getApartment());

        if (userDTO.getRole() == Role.DOCTOR) {
            DoctorInfoDTO doctorInfoDTO = doctorInfoService.getDoctorInfoByUserId(userDTO.getId());
            req.setAttribute("docSpec", doctorInfoDTO.getSpec());
            req.setAttribute("workingDays", doctorInfoDTO.getWorkingDays());
            req.setAttribute("description", doctorInfoDTO.getDescription());
        }

        req.setAttribute("action", ApplicationCommandConstants.EDIT_USER_CMD);
    }


    private long defineUserId(HttpServletRequest req, HttpServletResponse resp) {
        Long userId;
        Role currentUserRole = (Role) req.getSession().getAttribute("currentUserRole");

        if (currentUserRole == Role.ADMIN) {
            String userIdParameter = req.getParameter("userId");
            if (userIdParameter == null || userIdParameter.isEmpty()) {
                return -1;
            }
            userId = Long.parseLong(userIdParameter);
        } else {
            userId = (Long) req.getSession().getAttribute("currentUserId");
        }
        return userId;
    }
}
