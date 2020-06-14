package by.training.hospital.command;

import by.training.hospital.dto.UserDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.UserService;
import by.training.hospital.util.MD5Util;
import by.training.hospital.util.RequestUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(LoginCommand.class);

    private UserService userService;

    public LoginCommand(UserService userService) {
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

    private void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RequestUtil.forwardToJsp(req, resp, "login");
    }

    private void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            String login = req.getParameter("login");
            String password = req.getParameter("password");
            UserDTO userDTO = userService.getUserByLogin(login);

            if (userDTO != null && MD5Util.getMD5String(password).equals(userDTO.getPassword())) {
                if (userDTO.isBlocked()) {
                    RequestUtil.forwardToJsp(req, resp, "error", "error.blocked");
                    return;
                }

                req.getSession().setAttribute("currentUserId", userDTO.getId());
                req.getSession().setAttribute("currentUserRole", userDTO.getRole());

                if (userDTO.getRole() != Role.ADMIN) {
                    resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.SHOW_PROFILE_CMD);
                } else {
                    resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.SHOW_USERS_CMD);
                }

            } else {
                req.setAttribute("login", login);
                req.setAttribute("message", "error.login_or_pass");
                RequestUtil.forwardToJsp(req, resp, "login");
            }
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during retrieving a user from database in LoginCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }
}
