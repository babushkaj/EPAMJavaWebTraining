package by.training.hospital.command;

import by.training.hospital.dto.VisitDTO;
import by.training.hospital.entity.VisitStatus;
import by.training.hospital.service.NoConcreteDTOException;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.VisitService;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.CancelVisitValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class CancelVisitCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(CancelVisitCommand.class);
    private static final long ONE_HOUR_IN_MS = 60 * 60 * 1000;

    private VisitService visitService;

    public CancelVisitCommand(VisitService visitService) {
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
            Validator validator = new CancelVisitValidator();
            ValidationResult validationResult = validator.validate(req);

            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            String visitIdParameter = req.getParameter("visitId");
            long visitId = Long.parseLong(visitIdParameter);
            VisitDTO visitDTO = visitService.getById(visitId);

            Long currentVisitorId = (Long) req.getSession().getAttribute("currentUserId");
            Long lastViewedVisitId = (Long) req.getSession().getAttribute("lastViewedVisitId");
            if (!isPossibleCancelVisit(visitDTO, currentVisitorId, lastViewedVisitId)) {
                RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_cancel_visit");
                return;
            }

            if (isTooLateCancelVisit(visitDTO)) {
                req.setAttribute("cancelErr", "error.too_late_to_cancel_visit");
                RequestUtil.forwardToCommand(req, resp, ApplicationCommandConstants.SHOW_VISIT_INFO_CMD + "&visitId=" + visitId);
                return;
            }

            visitDTO.setVisitStatus(VisitStatus.CANCELED);
            visitService.update(visitDTO);

            resp.sendRedirect(req.getContextPath() + "?command=" +
                    ApplicationCommandConstants.SHOW_VISIT_INFO_CMD + "&visitId=" + visitId);

        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete visit in CancelVisitCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing CancelVisitCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private boolean isPossibleCancelVisit(VisitDTO visitDTO, Long currentVisitorId, Long lastViewedVisitId) {
        return visitDTO.getVisitorId() == currentVisitorId
                && visitDTO.getVisitStatus() == VisitStatus.PLANNED
                && visitDTO.getId() == lastViewedVisitId;
    }

    private boolean isTooLateCancelVisit(VisitDTO visitDTO) {
        Long visitTime = visitDTO.getDate().getTime();
        Long timeNow = new Date().getTime();
        return visitTime - timeNow < ONE_HOUR_IN_MS;
    }
}
