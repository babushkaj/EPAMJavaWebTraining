package by.training.hospital.command;

import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.service.*;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.ShowChooseVisitDateValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowChooseVisitDateCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ShowChooseVisitDateCommand.class);

    private UserService userService;
    private UserInfoService userInfoService;
    private DoctorInfoService doctorInfoService;

    public ShowChooseVisitDateCommand(UserService userService, UserInfoService userInfoService,
                                      DoctorInfoService doctorInfoService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.doctorInfoService = doctorInfoService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Validator validator = new ShowChooseVisitDateValidator();
            ValidationResult validationResult = validator.validate(req);
            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            String doctorUserIdParameter = req.getParameter("doctorUserId");
            Long doctorUserId = Long.parseLong(doctorUserIdParameter);

            UserDTO userDTO = userService.getById(doctorUserId);
            if (userDTO.getRole() != Role.DOCTOR) {
                req.setAttribute("error", "error.impossible_visit_by_doc_id");
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            UserInfoDTO userInfoDTO = userInfoService.getUserInfoByUserId(doctorUserId);
            DoctorInfoDTO doctorInfoDTO = doctorInfoService.getDoctorInfoByUserId(doctorUserId);
            userDTO.setUserInfo(userInfoDTO);
            userDTO.setDoctorInfo(doctorInfoDTO);

            req.setAttribute("doc", userDTO);
            RequestUtil.forwardToJsp(req, resp, "choose_date");

        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete user in ShowChooseVisitDateCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_visit_by_doc_id");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing ShowChooseVisitDateCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }
}
