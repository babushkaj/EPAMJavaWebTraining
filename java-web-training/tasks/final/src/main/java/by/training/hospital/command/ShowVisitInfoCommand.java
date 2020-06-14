package by.training.hospital.command;

import by.training.hospital.dto.*;
import by.training.hospital.entity.Role;
import by.training.hospital.service.*;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.ShowVisitInfoValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowVisitInfoCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ShowVisitInfoCommand.class);

    private VisitService visitService;
    private VisitFeedbackService visitFeedbackService;
    private UserService userService;
    private UserInfoService userInfoService;
    private DoctorInfoService doctorInfoService;

    public ShowVisitInfoCommand(VisitService visitService, VisitFeedbackService visitFeedbackService,
                                UserService userService, UserInfoService userInfoService,
                                DoctorInfoService doctorInfoService) {
        this.visitService = visitService;
        this.visitFeedbackService = visitFeedbackService;
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.doctorInfoService = doctorInfoService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Validator validator = new ShowVisitInfoValidator();
            ValidationResult validationResult = validator.validate(req);

            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            String visitIdParameter = req.getParameter("visitId");
            Long visitId = Long.parseLong(visitIdParameter);

            VisitDTO visit = visitService.getById(visitId);

            Long currentUserId = (Long) req.getSession().getAttribute("currentUserId");
            if (currentUserId == visit.getDoctorId() || currentUserId == visit.getVisitorId()) {
                setRequestAttributes(req, visit);
                req.getSession().setAttribute("lastViewedVisitId", visitId);
                RequestUtil.forwardToJsp(req, resp, "visit_info");
            } else {
                RequestUtil.forwardToJsp(req, resp, "error", "error.not_available_visit_info");
            }
        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete user in ShowVisitInfoCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.impossible_get_visit_by_id");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing ShowVisitInfoCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.not_available_visit_info");
        }
    }

    private void setRequestAttributes(HttpServletRequest req, VisitDTO visit) throws NoConcreteDTOException, ServiceException {

        Long doctorId;
        Long visitorId;
        Long currentUserId = (Long) req.getSession().getAttribute("currentUserId");
        Role currentUserRole = (Role) req.getSession().getAttribute("currentUserRole");
        if (currentUserRole == Role.VISITOR) {
            visitorId = currentUserId;
            doctorId = visit.getDoctorId();
        } else {
            visitorId = visit.getVisitorId();
            doctorId = currentUserId;
        }

        UserDTO doctor = userService.getById(doctorId);
        UserInfoDTO doctorUserInfo = userInfoService.getUserInfoByUserId(doctorId);
        DoctorInfoDTO doctorInfo = doctorInfoService.getDoctorInfoByUserId(doctorId);
        doctor.setUserInfo(doctorUserInfo);
        doctor.setDoctorInfo(doctorInfo);

        UserDTO visitor = userService.getById(visitorId);
        UserInfoDTO visitorUserInfo = userInfoService.getUserInfoByUserId(visitorId);
        visitor.setUserInfo(visitorUserInfo);

        VisitFeedbackDTO feedbackDTO = visitFeedbackService.getByVisitId(visit.getId());
        visit.setFeedback(feedbackDTO);

        req.setAttribute("doctor", doctor);
        req.setAttribute("visitor", visitor);
        req.setAttribute("visit", visit);

    }
}
