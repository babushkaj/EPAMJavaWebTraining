package by.training.hospital.command;

import by.training.hospital.dto.UserDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.service.NoConcreteDTOException;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.UserService;
import by.training.hospital.util.MD5Util;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.ChangePasswordValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangePasswordCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ChangePasswordCommand.class);

    private UserService userService;

    public ChangePasswordCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("post")) {
            processPost(req, resp);
        } else {
            processGet(req, resp);
        }
    }

    private void processGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestUtil.forwardToJsp(req, resp, "change_password");
    }

    private void processPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long currentUserId = (Long) req.getSession().getAttribute("currentUserId");
            UserDTO userDTO = userService.getById(currentUserId);

            String oldPassword = req.getParameter("oldPassword");

            if (!userDTO.getPassword().equals(MD5Util.getMD5String(oldPassword))) {
                req.setAttribute("oldPasswordErr", "error.invalid_current_password");
                RequestUtil.forwardToJsp(req, resp, "change_password");
                return;
            }

            Validator validator = new ChangePasswordValidator();
            ValidationResult validationResult = validator.validate(req);
            if (validationResult.isValid()) {

                String newPassword = req.getParameter("newPassword1");

                userDTO.setPassword(MD5Util.getMD5String(newPassword));

                userService.update(userDTO);

                Role currentUserRole = (Role) req.getSession().getAttribute("currentUserRole");
                if (currentUserRole == Role.ADMIN) {
                    resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.SHOW_USERS_CMD);
                } else {
                    resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.SHOW_PROFILE_CMD);
                }
            } else {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "change_password");
            }
        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete user in ChangePasswordCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing ChangePasswordCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }
}
