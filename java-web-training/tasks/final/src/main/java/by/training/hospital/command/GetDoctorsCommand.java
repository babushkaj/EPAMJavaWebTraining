package by.training.hospital.command;

import by.training.hospital.dto.UserDTO;
import by.training.hospital.service.DoctorInfoService;
import by.training.hospital.service.ServiceException;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.GetDoctorsValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GetDoctorsCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(GetDoctorsCommand.class);

    private static final long DOCTORS_ON_PAGE = 2;
    private static final long ROWS_IN_DB_FOR_ONE_DOCTOR = 5;

    private DoctorInfoService doctorInfoService;

    public GetDoctorsCommand(DoctorInfoService doctorInfoService) {
        this.doctorInfoService = doctorInfoService;
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Validator validator = new GetDoctorsValidator();
            ValidationResult validationResult = validator.validate(req);

            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            String pageNumberParameter = req.getParameter("pageNumber");
            long pageNumber = 1;
            if (pageNumberParameter != null) {
                pageNumber = Long.parseLong(pageNumberParameter);
            }

            Long from = ROWS_IN_DB_FOR_ONE_DOCTOR * DOCTORS_ON_PAGE * (pageNumber - 1);

            Long totalDoctorsCount = doctorInfoService.getUnblockedDoctorsCount();
            List<UserDTO> doctors = doctorInfoService.getAllUnblockedDoctorsWithInfoFromTo(from, DOCTORS_ON_PAGE * ROWS_IN_DB_FOR_ONE_DOCTOR);

            Long maxPage = totalDoctorsCount / DOCTORS_ON_PAGE;
            if (totalDoctorsCount % DOCTORS_ON_PAGE != 0) {
                maxPage++;
            }

            req.setAttribute("doctors", doctors);
            req.setAttribute("currentPage", pageNumber);
            req.setAttribute("maxPage", maxPage);

            RequestUtil.forwardToJsp(req, resp, "doctors");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing GetDoctorsCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }
}
