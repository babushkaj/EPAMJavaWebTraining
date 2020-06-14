package by.training.hospital.command;

import java.util.HashSet;
import java.util.Set;

public class ApplicationCommandConstants {

    public static final String REGISTRATION_CMD = "REGISTRATION";
    public static final String LOGIN_CMD = "LOGIN";
    public static final String LOGOUT_CMD = "LOGOUT";
    public static final String SHOW_PROFILE_CMD = "SHOW_PROFILE";
    public static final String SHOW_USERS_CMD = "SHOW_USERS";
    public static final String SHOW_EDIT_USER_CMD = "SHOW_EDIT_USER";
    public static final String EDIT_USER_CMD = "EDIT_USER";
    public static final String GET_DOCTORS_CMD = "GET_DOCTORS";
    public static final String BLOCK_USER_CMD = "BLOCK_USER";
    public static final String CHOOSE_DATE_CMD = "CHOOSE_DATE";
    public static final String CHOOSE_TIME_CMD = "CHOOSE_TIME";
    public static final String WRITE_REASON_CMD = "WRITE_REASON";
    public static final String MAKE_APPOINTMENT_CMD = "MAKE_APPOINTMENT";
    public static final String SHOW_VISITOR_VISITS_CMD = "SHOW_VISITOR_VISITS";
    public static final String SHOW_DOCTOR_VISITS_CMD = "SHOW_DOCTOR_VISITS";
    public static final String SHOW_VISIT_INFO_CMD = "SHOW_VISIT_INFO";
    public static final String FINISH_VISIT_CMD = "FINISH_VISIT";
    public static final String LEAVE_VISITOR_FEEDBACK_CMD = "LEAVE_VISITOR_FEEDBACK";
    public static final String LEAVE_DOCTOR_FEEDBACK_CMD = "LEAVE_DOCTOR_FEEDBACK";
    public static final String SHOW_DOCTOR_FEEDBACKS_CMD = "SHOW_DOCTOR_FEEDBACKS";
    public static final String CANCEL_VISIT_CMD = "CANCEL_VISIT";
    public static final String CHANGE_PASSWORD_CMD = "CHANGE_PASSWORD";
    public static final String FIND_DOCTORS_CMD = "FIND_DOCTORS";
    public static final String FIND_VISITS_CMD = "FIND_VISITS";
    public static final String ERR_CMD = "ERR_CMD";

    public static Set<String> commands = new HashSet<>();

    static {
        commands.add(REGISTRATION_CMD);
        commands.add(LOGIN_CMD);
        commands.add(LOGOUT_CMD);
        commands.add(SHOW_PROFILE_CMD);
        commands.add(SHOW_USERS_CMD);
        commands.add(SHOW_EDIT_USER_CMD);
        commands.add(EDIT_USER_CMD);
        commands.add(GET_DOCTORS_CMD);
        commands.add(BLOCK_USER_CMD);
        commands.add(CHOOSE_DATE_CMD);
        commands.add(CHOOSE_TIME_CMD);
        commands.add(WRITE_REASON_CMD);
        commands.add(MAKE_APPOINTMENT_CMD);
        commands.add(SHOW_VISITOR_VISITS_CMD);
        commands.add(SHOW_DOCTOR_VISITS_CMD);
        commands.add(SHOW_VISIT_INFO_CMD);
        commands.add(FINISH_VISIT_CMD);
        commands.add(LEAVE_VISITOR_FEEDBACK_CMD);
        commands.add(LEAVE_DOCTOR_FEEDBACK_CMD);
        commands.add(SHOW_DOCTOR_FEEDBACKS_CMD);
        commands.add(CANCEL_VISIT_CMD);
        commands.add(CHANGE_PASSWORD_CMD);
        commands.add(FIND_DOCTORS_CMD);
        commands.add(FIND_VISITS_CMD);
    }

}
