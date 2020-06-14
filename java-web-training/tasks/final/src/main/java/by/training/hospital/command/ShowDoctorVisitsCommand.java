package by.training.hospital.command;

import by.training.hospital.dto.VisitDTO;
import by.training.hospital.entity.VisitStatus;
import by.training.hospital.service.ServiceException;
import by.training.hospital.service.VisitService;
import by.training.hospital.util.RequestUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ShowDoctorVisitsCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ShowDoctorVisitsCommand.class);

    private VisitService visitService;

    public ShowDoctorVisitsCommand(VisitService visitService) {
        this.visitService = visitService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("currentUserId");
        try {
            String reqFilterParameter = req.getParameter("choseVisits");
            if (reqFilterParameter == null) {
                reqFilterParameter = "allDoc";
            }
            List<VisitDTO> visitsByDoctorId = visitService.getVisitByDoctorId(userId);

            List<VisitDTO> filteredVisits = filterVisitsByRequestParameter(reqFilterParameter, visitsByDoctorId);

            List<VisitDTO> visitsToPage = filteredVisits.stream()
                    .sorted(Comparator.comparing(VisitDTO::getDate))
                    .collect(Collectors.toList());

            req.getSession().setAttribute("lastChosenVisitsGroup", reqFilterParameter);
            req.setAttribute("lastChosenVisitsGroup", reqFilterParameter);
            req.setAttribute("visits", visitsToPage);
            RequestUtil.forwardToJsp(req, resp, "all_user_visits");

        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during retrieving all visits for doctor with userId = " +
                    +userId + " processing ShowDoctorVisitsCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private List<VisitDTO> filterVisitsByRequestParameter(String reqFilterParameter, List<VisitDTO> visits) throws ServiceException {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date today = formatter.parse(formatter.format(new Date()));
            Date tomorrow = new Date(today.getTime() + FindVisitsCommand.ONE_DAY_IN_MS);
            switch (reqFilterParameter) {
                case "today": {
                    return visits.stream()
                            .filter(v -> v.getDate().after(today) && v.getDate().before(tomorrow))
                            .filter(v -> v.getVisitStatus() != VisitStatus.CANCELED)
                            .collect(Collectors.toList());
                }
                default:
                    return visits;
            }
        } catch (ParseException e) {
            throw new ServiceException("An error has occurred during parsing date in ShowDoctorVisitsCommand.", e);
        }
    }
}
