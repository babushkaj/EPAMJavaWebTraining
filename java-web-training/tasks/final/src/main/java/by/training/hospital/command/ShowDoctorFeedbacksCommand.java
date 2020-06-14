package by.training.hospital.command;

import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.VisitFeedbackDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.service.NoConcreteDTOException;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.UserService;
import by.training.hospital.service.VisitFeedbackService;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.ShowDoctorFeedbacksValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ShowDoctorFeedbacksCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ShowDoctorFeedbacksCommand.class);

    private VisitFeedbackService visitFeedbackService;
    private UserService userService;

    public ShowDoctorFeedbacksCommand(VisitFeedbackService visitFeedbackService, UserService userService) {
        this.visitFeedbackService = visitFeedbackService;
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Validator validator = new ShowDoctorFeedbacksValidator();
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
                req.setAttribute("error", "error.impossible_feedbacks_by_doc_id");
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            List<VisitFeedbackDTO> feedbacks = visitFeedbackService.getByDoctorUserId(doctorUserId);

            req.setAttribute("feedbacks", feedbacks);
            RequestUtil.forwardToJsp(req, resp, "doctor_feedbacks");
        } catch (ServiceException e) {
            LOGGER.error("An error occurred during retrieving all feedbacks for doctor in ShowDoctorFeedbacksCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete user in ShowChooseVisitDateCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_feedbacks_by_doc_id");
        }
    }
}
