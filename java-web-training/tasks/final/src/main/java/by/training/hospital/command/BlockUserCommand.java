package by.training.hospital.command;

import by.training.hospital.context.SecurityContext;
import by.training.hospital.dto.UserDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.service.NoConcreteDTOException;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.UserService;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.BlockUserValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BlockUserCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(BlockUserCommand.class);

    private UserService userService;

    public BlockUserCommand(UserService userService) {
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
        RequestUtil.forwardToJsp(req, resp, "error", "error.unsupported_get_method");
    }

    private void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            Validator validator = new BlockUserValidator();
            ValidationResult validationResult = validator.validate(req);

            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            Long userId = Long.parseLong(req.getParameter("userId"));
            UserDTO user = userService.getById(userId);
            if (user.getRole() == Role.ADMIN) {
                RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_block_admin");
                return;
            }
            if (user.isBlocked()) {
                user.setBlocked(false);
            } else {
                user.setBlocked(true);
            }
            userService.update(user);

            SecurityContext.getInstance().deleteSessionByUserId(userId);

            String chosenUsers = (String) req.getSession().getAttribute("lastChosenUsersGroup");
            Long pageNumber = (Long) req.getSession().getAttribute("lastVisitedPageNumber");

            resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.SHOW_USERS_CMD +
                    "&chosenUsers=" + chosenUsers + "&pageNumber=" + pageNumber);
        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete user in BlockUserCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_block_user");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing BlockUserCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }
}
