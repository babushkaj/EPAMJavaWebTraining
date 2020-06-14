package by.training.hospital.command;

import by.training.hospital.dto.VisitDTO;
import by.training.hospital.entity.VisitStatus;
import by.training.hospital.service.NoConcreteDTOException;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.VisitService;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.FinishVisitValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class FinishVisitCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(FinishVisitCommand.class);

    private VisitService visitService;

    public FinishVisitCommand(VisitService visitService) {
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
            Validator validator = new FinishVisitValidator();
            ValidationResult validationResult = validator.validate(req);
            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error.impossible_finish_visit");
                return;
            }

            String visitIdParameter = req.getParameter("visitId");
            String visitResultParameter = req.getParameter("visitResult");

            long visitId = Long.parseLong(visitIdParameter);
            VisitDTO visitDTO = visitService.getById(visitId);

            Long currentDoctorId = (Long) req.getSession().getAttribute("currentUserId");
            Long lastViewedVisitId = (Long) req.getSession().getAttribute("lastViewedVisitId");
            if (!isPossibleFinishVisit(visitDTO, currentDoctorId, lastViewedVisitId)) {
                RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_finish_visit");
                return;
            }

            if (visitDTO.getDate().after(new Date())) {
                req.setAttribute("finishErr", "error.visit_cannot_be_finished");
                RequestUtil.forwardToCommand(req, resp, ApplicationCommandConstants.SHOW_VISIT_INFO_CMD + "&visitId=" + visitId);
                return;
            }
            visitDTO.setResult(visitResultParameter);
            visitDTO.setVisitStatus(VisitStatus.COMPLETED);
            visitService.update(visitDTO);

            resp.sendRedirect(req.getContextPath() + "?command=" +
                    ApplicationCommandConstants.SHOW_VISIT_INFO_CMD + "&visitId=" + visitId);

        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred during trying to get a concrete visit in FinishVisitCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_finish_visit");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing FinishVisitCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private boolean isPossibleFinishVisit(VisitDTO visitDTO, Long currentDoctorId, Long lastViewedVisitId) {
        return visitDTO.getDoctorId() == currentDoctorId && visitDTO.getVisitStatus() == VisitStatus.PLANNED
                && visitDTO.getId() == lastViewedVisitId;
    }
}
