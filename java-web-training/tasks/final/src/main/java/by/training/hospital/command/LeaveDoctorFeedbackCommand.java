package by.training.hospital.command;

import by.training.hospital.dto.VisitDTO;
import by.training.hospital.dto.VisitFeedbackDTO;
import by.training.hospital.entity.VisitStatus;
import by.training.hospital.service.NoConcreteDTOException;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.VisitFeedbackService;
import by.training.hospital.service.VisitService;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.LeaveFeedbackValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LeaveDoctorFeedbackCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(LeaveDoctorFeedbackCommand.class);

    private VisitFeedbackService visitFeedbackService;
    private VisitService visitService;

    public LeaveDoctorFeedbackCommand(VisitFeedbackService visitFeedbackService, VisitService visitService) {
        this.visitFeedbackService = visitFeedbackService;
        this.visitService = visitService;
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
            Validator validator = new LeaveFeedbackValidator();
            ValidationResult validationResult = validator.validate(req);

            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            Long currentDoctorId = (Long) req.getSession().getAttribute("currentUserId");
            Long lastViewedVisitId = (Long) req.getSession().getAttribute("lastViewedVisitId");
            long visitId = Long.parseLong(req.getParameter("visitId"));
            VisitFeedbackDTO visitFeedbackDTO = visitFeedbackService.getByVisitId(visitId);
            VisitDTO visitDTO = visitService.getById(visitId);

            if (!isPossibleLeaveFeedback(visitDTO, visitFeedbackDTO, currentDoctorId, lastViewedVisitId)) {
                RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_leave_feedback");
                return;
            }

            String doctorFeedbackParameter = req.getParameter("doctorFeedback");
            visitFeedbackDTO.setDoctorMess(doctorFeedbackParameter);
            visitFeedbackService.update(visitFeedbackDTO);

            resp.sendRedirect(req.getContextPath() + "?command=" +
                    ApplicationCommandConstants.SHOW_VISIT_INFO_CMD + "&visitId=" + visitId);

        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing LeaveDoctorFeedbackCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete visit in LeaveDoctorFeedbackCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_leave_feedback");
        }
    }

    private boolean isPossibleLeaveFeedback(VisitDTO visitDTO, VisitFeedbackDTO visitFeedbackDTO,
                                            Long currentDoctorId, Long lastViewedVisitId) {
        return visitDTO.getVisitStatus() == VisitStatus.COMPLETED
                && visitDTO.getDoctorId() == currentDoctorId
                && visitFeedbackDTO.getDoctorMess() == null
                && visitDTO.getId() == lastViewedVisitId;
    }
}
