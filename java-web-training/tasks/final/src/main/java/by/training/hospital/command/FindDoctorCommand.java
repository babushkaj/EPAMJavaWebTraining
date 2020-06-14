package by.training.hospital.command;

import by.training.hospital.dto.UserDTO;
import by.training.hospital.entity.Specialization;
import by.training.hospital.service.DoctorInfoService;
import by.training.hospital.service.ServiceException;
import by.training.hospital.util.RequestUtil;
import by.training.hospital.validator.FindDoctorValidator;
import by.training.hospital.validator.ValidationResult;
import by.training.hospital.validator.ValidationUtil;
import by.training.hospital.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FindDoctorCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(FindDoctorCommand.class);

    private DoctorInfoService doctorInfoService;

    public FindDoctorCommand(DoctorInfoService doctorInfoService) {
        this.doctorInfoService = doctorInfoService;
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
        RequestUtil.forwardToJsp(req, resp, "find_doctor");
    }

    private void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            String firstNameParameter = req.getParameter("firstName");
            String lastNameParameter = req.getParameter("lastName");
            Set<DayOfWeek> workingDays = new HashSet<>();
            for (DayOfWeek d : DayOfWeek.values()) {
                String day = req.getParameter(d.name());
                if (day != null && !day.isEmpty()) {
                    workingDays.add(d);
                }
            }
            String specSelectParameter = req.getParameter("specSelect");

            Validator validator = new FindDoctorValidator();
            ValidationResult validationResult = validator.validate(req);
            if (!validationResult.isValid()) {
                RequestUtil.setRequestParametersFromValidationResult(req, validationResult);
                RequestUtil.forwardToJsp(req, resp, "error");
                return;
            }

            List<UserDTO> doctors = doctorInfoService.getAllUnblockedDoctorsWithInfo();

            if (firstNameParameter != null && !firstNameParameter.isEmpty()) {
                doctors = filterByFirstName(firstNameParameter, doctors);
            }
            if (lastNameParameter != null && !lastNameParameter.isEmpty()) {
                doctors = filterByLastName(lastNameParameter, doctors);
            }
            if (!workingDays.isEmpty()) {
                doctors = filterByWorkingDays(workingDays, doctors);
            }
            if (specSelectParameter != null && !specSelectParameter.isEmpty()) {
                doctors = filterBySpecialization(Specialization.valueOf(specSelectParameter), doctors);
            }

            req.setAttribute("doctors", doctors);
            req.setAttribute("showPagination", false);
            RequestUtil.forwardToJsp(req, resp, "doctors");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing FindDoctorCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private List<UserDTO> filterByFirstName(String firstName, List<UserDTO> doctors) {
        return doctors.stream()
                .filter(d -> ValidationUtil.thisStringMatchesRegex(d.getUserInfo().getFirstName(), firstName))
                .collect(Collectors.toList());
    }

    private List<UserDTO> filterByLastName(String lastName, List<UserDTO> doctors) {
        return doctors.stream()
                .filter(d -> ValidationUtil.thisStringMatchesRegex(d.getUserInfo().getLastName(), lastName))
                .collect(Collectors.toList());
    }

    private List<UserDTO> filterByWorkingDays(Set<DayOfWeek> workingDays, List<UserDTO> doctors) {
        List<UserDTO> result = new ArrayList<>();
        for (UserDTO doctor : doctors) {
            for (DayOfWeek doctorWorkingDay : doctor.getDoctorInfo().getWorkingDays()) {
                if (workingDays.contains(doctorWorkingDay)) {
                    result.add(doctor);
                    break;
                }
            }
        }
        return result;
    }

    private List<UserDTO> filterBySpecialization(Specialization spec, List<UserDTO> doctors) {
        return doctors.stream()
                .filter(d -> d.getDoctorInfo().getSpec() == spec)
                .collect(Collectors.toList());
    }
}
