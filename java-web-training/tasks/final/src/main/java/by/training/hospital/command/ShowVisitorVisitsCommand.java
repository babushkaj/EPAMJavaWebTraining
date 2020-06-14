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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShowVisitorVisitsCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ShowVisitorVisitsCommand.class);

    private VisitService visitService;

    public ShowVisitorVisitsCommand(VisitService visitService) {
        this.visitService = visitService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("currentUserId");
        try {
            String reqFilterParameter = req.getParameter("choseVisits");
            if (reqFilterParameter == null) {
                reqFilterParameter = "all";
            }
            List<VisitDTO> visitsByVisitorId = visitService.getVisitByVisitorId(userId);

            List<VisitDTO> filteredVisits = filterVisitsByRequestParameter(reqFilterParameter, visitsByVisitorId);

            List<VisitDTO> visitsToPage = filteredVisits.stream()
                    .sorted(Comparator.comparing(VisitDTO::getDate))
                    .collect(Collectors.toList());

            req.getSession().setAttribute("lastChosenVisitsGroup", reqFilterParameter);
            req.setAttribute("lastChosenVisitsGroup", reqFilterParameter);
            req.setAttribute("visits", visitsToPage);
            RequestUtil.forwardToJsp(req, resp, "all_user_visits");

        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during retrieving all visits for visitor with userId = " +
                    +userId + " processing ShowVisitorVisitsCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private List<VisitDTO> filterVisitsByRequestParameter(String reqFilterParameter, List<VisitDTO> visits) {
        switch (reqFilterParameter) {
            case "planned": {
                return visits.stream()
                        .filter(v -> v.getVisitStatus() == VisitStatus.PLANNED)
                        .collect(Collectors.toList());
            }
            case "completed": {
                return visits.stream()
                        .filter(v -> v.getVisitStatus() == VisitStatus.COMPLETED)
                        .collect(Collectors.toList());
            }
            case "canceled": {
                return visits.stream()
                        .filter(v -> v.getVisitStatus() == VisitStatus.CANCELED)
                        .collect(Collectors.toList());
            }
            default:
                return visits;
        }
    }
}
