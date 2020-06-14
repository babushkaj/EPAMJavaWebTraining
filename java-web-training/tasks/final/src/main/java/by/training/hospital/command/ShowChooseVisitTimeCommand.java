package by.training.hospital.command;

import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.dto.VisitDTO;
import by.training.hospital.entity.VisitStatus;
import by.training.hospital.service.*;
import by.training.hospital.util.RequestUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class ShowChooseVisitTimeCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(ShowChooseVisitTimeCommand.class);
    static final Set<String> workingHours = new HashSet<>();

    static {
        workingHours.add("16-30");
        workingHours.add("16-00");
        workingHours.add("15-30");
        workingHours.add("15-00");
        workingHours.add("14-30");
        workingHours.add("14-00");
        workingHours.add("13-30");
        workingHours.add("13-00");
        workingHours.add("12-30");
        workingHours.add("12-00");
        workingHours.add("11-30");
        workingHours.add("11-00");
        workingHours.add("10-30");
        workingHours.add("10-00");
        workingHours.add("9-30");
        workingHours.add("9-00");
    }

    private VisitService visitService;
    private DoctorInfoService doctorInfoService;
    private UserService userService;
    private UserInfoService userInfoService;

    public ShowChooseVisitTimeCommand(VisitService visitService, DoctorInfoService doctorInfoService,
                                      UserService userService, UserInfoService userInfoService) {
        this.visitService = visitService;
        this.doctorInfoService = doctorInfoService;
        this.userService = userService;
        this.userInfoService = userInfoService;
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
        String date = req.getParameter("dateInput");
        try {
            Date visitDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            Long currentUserId = (Long) req.getSession().getAttribute("currentUserId");
            Long doctorUserId = Long.parseLong(req.getParameter("doctorUserId"));

            boolean hasMoreVisits = hasMoreVisitsToThisDoctorOnTheSameDay(currentUserId, doctorUserId, visitDate);

            UserDTO userDTO = getDoctorWithInfo(doctorUserId);
            DayOfWeek dayOfWeek = getDayOfWeekFromDate(visitDate);

            if (hasMoreVisits) {
                req.setAttribute("doc", userDTO);
                req.setAttribute("error", "error.the_second_visit");
                RequestUtil.forwardToJsp(req, resp, "choose_date");
                return;
            } else if (!userDTO.getDoctorInfo().getWorkingDays().contains(dayOfWeek)) {
                req.setAttribute("doc", userDTO);
                req.setAttribute("error", "error.not_working_day");
                RequestUtil.forwardToJsp(req, resp, "choose_date");
                return;
            }

            List<VisitDTO> allVisits = visitService.getVisitByDoctorId(doctorUserId);
            List<Long> allPlannedAndCompletedOnThisDay = allVisitsTimeForDoctorThisDay(allVisits, visitDate);
            Map<String, Long> allPossibleVisitsTime = allPossibleVisitsForOneDay(date);
            Map<String, Long> visitsToPage = allAvailableVisitsTimeToChoose(allPlannedAndCompletedOnThisDay, allPossibleVisitsTime);

            req.setAttribute("visitsMap", visitsToPage);
            req.setAttribute("doctorUserId", doctorUserId);
            RequestUtil.forwardToJsp(req, resp, "choose_time");

        } catch (ParseException e) {
            LOGGER.error("User tried to enter incorrect formatted date (" + date + ") in ShowChooseVisitTimeCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.invalid_date_format");
        } catch (NoConcreteDTOException e) {
            LOGGER.error("An error occurred while trying to get a concrete user in ShowChooseVisitTimeCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        } catch (ServiceException e) {
            LOGGER.error("An error has occurred during processing ShowChooseVisitTimeCommand.", e);
            RequestUtil.forwardToJsp(req, resp, "error", "error.incorrect_request_processing");
        }
    }

    private boolean hasMoreVisitsToThisDoctorOnTheSameDay(Long currentUserId, Long doctorUserId, Date visitDate) throws ServiceException {
        List<VisitDTO> allUserVisits = visitService.getVisitByVisitorId(currentUserId);

        List<VisitDTO> filteredVisits = allUserVisits.stream()
                .filter(v -> v.getDate().after(visitDate))
                .filter(v -> v.getDate().before(new Date(visitDate.getTime() + FindVisitsCommand.ONE_DAY_IN_MS)))
                .filter(v -> v.getDoctorId() == doctorUserId)
                .filter(v -> v.getVisitStatus() != VisitStatus.CANCELED)
                .collect(Collectors.toList());

        return !filteredVisits.isEmpty();
    }

    private List<Long> allVisitsTimeForDoctorThisDay(List<VisitDTO> allVisits, Date visitDate) {
        return allVisits.stream().filter(v -> v.getVisitStatus() == VisitStatus.PLANNED)
                .filter(v -> v.getDate().getTime() > visitDate.getTime())
                .filter(v -> v.getDate().getTime() < (visitDate.getTime() + FindVisitsCommand.ONE_DAY_IN_MS))
                .map(v -> v.getDate().getTime())
                .collect(Collectors.toList());
    }

    private Map<String, Long> allPossibleVisitsForOneDay(String date) throws ParseException {
        Map<String, Long> allPossibleVisit = new HashMap<>();
        for (String time : workingHours) {
            Date oneVisitDateAndTime = new SimpleDateFormat("dd-MM-yyyy HH-mm").parse(date + " " + time);
            allPossibleVisit.put(time, oneVisitDateAndTime.getTime());
        }
        return allPossibleVisit;
    }

    private Map<String, Long> allAvailableVisitsTimeToChoose(List<Long> allPlannedAndCompletedOnThisDay,
                                                             Map<String, Long> allPossibleVisitsTime) {
        Long timestampNow = new Date().getTime() + 15 * 60 * 1000;
        return allPossibleVisitsTime.entrySet().stream()
                .filter(entry -> !(allPlannedAndCompletedOnThisDay.contains(entry.getValue())))
                .filter(entry -> entry.getValue() > timestampNow)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private UserDTO getDoctorWithInfo(Long doctorUserId) throws NoConcreteDTOException, ServiceException {
        UserDTO userDTO = userService.getById(doctorUserId);
        UserInfoDTO userInfoDTO = userInfoService.getUserInfoByUserId(doctorUserId);
        DoctorInfoDTO doctorInfoDTO = doctorInfoService.getDoctorInfoByUserId(doctorUserId);
        userDTO.setUserInfo(userInfoDTO);
        userDTO.setDoctorInfo(doctorInfoDTO);
        return userDTO;
    }

    private DayOfWeek getDayOfWeekFromDate(Date date) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        String dayOfWeekString = simpleDateformat.format(date).toUpperCase();
        return DayOfWeek.valueOf(dayOfWeekString);
    }
}
