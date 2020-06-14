package by.training.hospital.command;

import by.training.hospital.dto.UserDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.UserService;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.ShowAllUsersValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ShowAllUsersCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ShowAllUsersCommand.class);

    private static final long USERS_ON_PAGE = 5;

    private UserService userService;

    public ShowAllUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Validator validator = new ShowAllUsersValidator();
            ValidationResult validationResult = validator.validate(req);

            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            String reqFilterParameter = req.getParameter("chosenUsers");

            if (reqFilterParameter == null) {
                reqFilterParameter = req.getParameter("checked");
            }
            if (reqFilterParameter == null) {
                reqFilterParameter = "all";
            }

            String pageNumberParameter = req.getParameter("pageNumber");

            long pageNumber = 1;
            if (pageNumberParameter != null) {
                pageNumber = Long.parseLong(pageNumberParameter);
            }

            Long from = USERS_ON_PAGE * (pageNumber - 1);
            Long totalUsersCount = getCountOfUsersByRequestParameter(reqFilterParameter);
            List<UserDTO> users = filterUsersByRequestParameter(reqFilterParameter, from);

            Long maxPage = totalUsersCount / USERS_ON_PAGE;
            if (totalUsersCount % USERS_ON_PAGE != 0) {
                maxPage++;
            }

            req.setAttribute("users", users);
            req.setAttribute("currentPage", pageNumber);
            req.setAttribute("maxPage", maxPage);
            req.setAttribute("checkedAttr", reqFilterParameter);
            req.getSession().setAttribute("lastVisitedPageNumber", pageNumber);
            req.getSession().setAttribute("lastChosenUsersGroup", reqFilterParameter);

            RequestUtil.forwardToJsp(req, resp, "all_users");
        } catch (ServiceException e) {
            System.out.println(e.toString());
            LOGGER.error("An error occurred during retrieving users with info in ShowAllUsersCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private List<UserDTO> filterUsersByRequestParameter(String filterParameter, Long from) throws ServiceException {
        switch (filterParameter) {
            case "visitors": {
                return userService.getAllUsersWithUserInfoByRoleFromTo(from, USERS_ON_PAGE, Role.VISITOR);
            }
            case "doctors": {
                return userService.getAllUsersWithUserInfoByRoleFromTo(from, USERS_ON_PAGE, Role.DOCTOR);
            }
            default:
                return userService.getAllUsersWithUserInfoFromTo(from, USERS_ON_PAGE);
        }
    }

    private Long getCountOfUsersByRequestParameter(String filterParameter) throws ServiceException {
        switch (filterParameter) {
            case "visitors": {
                return userService.getUserCountByRole(Role.VISITOR);
            }
            case "doctors": {
                return userService.getUserCountByRole(Role.DOCTOR);
            }
            default:
                return userService.getAllUsersCount();
        }
    }
}
