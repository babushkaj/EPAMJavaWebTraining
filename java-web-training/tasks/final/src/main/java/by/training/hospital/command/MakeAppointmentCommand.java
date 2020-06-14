package by.training.hospital.command;

import by.training.hospital.dto.VisitDTO;
import by.training.hospital.entity.VisitStatus;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.VisitService;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.MakeAppointmentValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MakeAppointmentCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(MakeAppointmentCommand.class);

    private static final long FIFTEEN_MINUTES_IN_MS = 15 * 60 * 1000;

    private VisitService visitService;

    public MakeAppointmentCommand(VisitService visitService) {
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
            Validator validator = new MakeAppointmentValidator();
            ValidationResult validationResult = validator.validate(req);
            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            Long currentUserId = (Long) req.getSession().getAttribute("currentUserId");
            Long date = Long.parseLong(req.getParameter("visitTime"));
            Long doctorUserId = Long.parseLong(req.getParameter("doctorUserId"));
            String visitReason = req.getParameter("visitReason");

            boolean isDateCorrect = isVisitDateCorrect(date);
            if (!isDateCorrect) {
                req.setAttribute("error", "error.incorrect_date_or_time_of_visit");
                RequestUtil.forwardToCommand(req, resp, ApplicationCommandConstants.GET_DOCTORS_CMD);
                return;
            }

            VisitDTO visitDTO = buildVisitDTO(visitReason, doctorUserId, currentUserId, date);
            visitService.create(visitDTO);

            resp.sendRedirect(req.getContextPath() + "?command=" + ApplicationCommandConstants.SHOW_VISITOR_VISITS_CMD);
        } catch (ServiceException e) {
            LOGGER.error("An error occurred during creating a new visit in MakeAppointmentCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        } catch (ParseException e) {
            LOGGER.error("Incorrect date format in MakeAppointmentCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.invalid_date_format");
        }
    }

    private boolean isVisitDateCorrect(Long date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("H-mm");
        String strDate = dateFormat.format(date);
        if (!ShowChooseVisitTimeCommand.workingHours.contains(strDate)) {
            return false;
        }
        Date dateNow = new Date();
        String todayMidnight = new SimpleDateFormat("dd-MM-yyyy").format(dateNow);
        Long datePlusTwoWeeks = new SimpleDateFormat("dd-MM-yyyy")
                .parse(todayMidnight).getTime() + 15 * FindVisitsCommand.ONE_DAY_IN_MS;

        if (!(date > dateNow.getTime() + FIFTEEN_MINUTES_IN_MS && date < datePlusTwoWeeks)) {
            return false;
        }
        return true;
    }

    private VisitDTO buildVisitDTO(String visitReason, Long doctorUserId, Long visitorUserId, Long date) {
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setCause(visitReason);
        visitDTO.setResult(null);
        visitDTO.setDoctorId(doctorUserId);
        visitDTO.setDate(new Date(date));
        visitDTO.setVisitorId(visitorUserId);
        visitDTO.setVisitStatus(VisitStatus.PLANNED);
        return visitDTO;
    }
}
