package by.training.hospital.command;

import by.training.hospital.dto.VisitDTO;
import by.training.hospital.entity.VisitStatus;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.VisitService;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.ShowWriteVisitReasonValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ShowWriteVisitReasonCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ShowWriteVisitReasonCommand.class);

    private VisitService visitService;

    public ShowWriteVisitReasonCommand(VisitService visitService) {
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
            Validator validator = new ShowWriteVisitReasonValidator();
            ValidationResult validationResult = validator.validate(req);
            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            String date = req.getParameter("visitTime");
            String doctorUserId = req.getParameter("doctorUserId");
            Long currentUserId = (Long) req.getSession().getAttribute("currentUserId");

            boolean hasVisitsAtTheSameTime = isVisitorAlreadyHasVisitsAtTheSameTime(currentUserId, date);

            if (hasVisitsAtTheSameTime) {
                req.setAttribute("error", "error.two_visits_at_the_same_time");
                RequestUtil.forwardToCommand(req, resp, ApplicationCommandConstants.GET_DOCTORS_CMD);
                return;
            }

            req.setAttribute("visitTime", date);
            req.setAttribute("doctorUserId", doctorUserId);

            RequestUtil.forwardToJsp(req, resp, "write_reason");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing ShowWriteVisitReasonCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private boolean isVisitorAlreadyHasVisitsAtTheSameTime(Long visitorUserId, String date) throws ServiceException {
        Long visitDateStamp = Long.parseLong(date);
        Date visitDate = new Date(visitDateStamp);
        List<VisitDTO> visits = visitService.getVisitByVisitorId(visitorUserId);
        return !visits.stream()
                .filter(v -> v.getDate().compareTo(visitDate) == 0)
                .filter(v -> v.getVisitStatus() != VisitStatus.CANCELED)
                .collect(Collectors.toList()).isEmpty();
    }
}
