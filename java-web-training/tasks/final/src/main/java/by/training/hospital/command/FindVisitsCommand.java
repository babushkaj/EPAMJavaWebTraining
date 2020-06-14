package by.training.hospital.command;

import by.training.hospital.dto.VisitDTO;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.VisitService;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.ValidationUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FindVisitsCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(FindVisitsCommand.class);

    static final Long ONE_DAY_IN_MS = 24 * 60 * 60 * 1000L;

    private VisitService visitService;

    public FindVisitsCommand(VisitService visitService) {
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
        RequestUtil.forwardToJsp(req, resp, "find_visits");
    }

    private void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Long userId = (Long) req.getSession().getAttribute("currentUserId");
        String firstNameParameter = req.getParameter("firstName");
        String lastNameParameter = req.getParameter("lastName");
        String dateParameter = req.getParameter("dateInput");
        try {
            List<VisitDTO> visits = visitService.getVisitByDoctorId(userId);

            if (firstNameParameter != null && !firstNameParameter.isEmpty()) {
                visits = filterByFirstName(firstNameParameter, visits);
            }
            if (lastNameParameter != null && !lastNameParameter.isEmpty()) {
                visits = filterByLastName(lastNameParameter, visits);
            }
            if (dateParameter != null && !dateParameter.isEmpty()) {
                Date visitDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateParameter);
                visits = filterByDate(visitDate, visits);
            }

            req.setAttribute("visits", visits);
            req.setAttribute("showOnlySearchResult", true);

            RequestUtil.forwardToJsp(req, resp, "all_user_visits");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing FindVisitsCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        } catch (ParseException e) {
            LOGGER.error("Incorrect date format '" + dateParameter + "' in FindVisitsCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.invalid_date_format");
        }

    }

    private List<VisitDTO> filterByFirstName(String firstName, List<VisitDTO> visits) {
        return visits.stream()
                .filter(v -> ValidationUtil.thisStringMatchesRegex(v.getVisitorFirstName(), firstName))
                .collect(Collectors.toList());
    }

    private List<VisitDTO> filterByLastName(String lastName, List<VisitDTO> visits) {
        return visits.stream()
                .filter(v -> ValidationUtil.thisStringMatchesRegex(v.getVisitorLastName(), lastName))
                .collect(Collectors.toList());
    }

    private List<VisitDTO> filterByDate(Date visitDate, List<VisitDTO> visits) {
        return visits.stream()
                .filter(v -> v.getDate().after(visitDate))
                .filter(v -> v.getDate().before(new Date(visitDate.getTime() + ONE_DAY_IN_MS)))
                .collect(Collectors.toList());
    }
}
