package by.training.hospital.context;

import by.training.hospital.command.*;
import by.training.hospital.dao.*;
import by.training.hospital.service.*;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ApplicationContext {

    private static final Logger LOGGER = Logger.getLogger(ApplicationContext.class);

    private static final String POOL_INIT_ERROR = "Error during ConnectionPool initialization.";
    private static final String POOL_DESTROYING_ERROR = "Error during ConnectionPool destroying.";
    private static final String DB_PROPERTY_NAME = "database";

    private static ReentrantLock lock = new ReentrantLock();

    private static ApplicationContext instance;

    private Map<String, Command> commands = new HashMap<>();

    private ApplicationContext() {
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new ApplicationContext();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public void init() throws ContextException {
        try {
            ConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
            connectionPool.init(DB_PROPERTY_NAME, 5);
            TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
            ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager);
            UserDAO userDAO = new UserDAOImpl(connectionManager);
            AddressDAO addressDAO = new AddressDAOImpl(connectionManager);
            DoctorInfoDAO doctorInfoDAO = new DoctorInfoDAOImpl(connectionManager);
            UserInfoDAO userInfoDAO = new UserInfoDAOImpl(connectionManager);
            VisitDAO visitDAO = new VisitDAOImpl(connectionManager);
            VisitFeedbackDAO visitFeedbackDAO = new VisitFeedbackDAOImpl(connectionManager);

            UserService userService = new UserServiceImpl(userDAO, userInfoDAO, addressDAO, doctorInfoDAO, transactionManager);
            AddressService addressService = new AddressServiceImpl(addressDAO);
            DoctorInfoService doctorInfoService = new DoctorInfoServiceImpl(doctorInfoDAO);
            UserInfoService userInfoService = new UserInfoServiceImpl(userInfoDAO);
            VisitFeedbackService visitFeedbackService = new VisitFeedbackServiceImpl(visitFeedbackDAO);
            VisitService visitService = new VisitServiceImpl(visitDAO);

            Command blockUserCommand = new BlockUserCommand(userService);
            Command cancelVisitCommand = new CancelVisitCommand(visitService);
            Command changePasswordCommand = new ChangePasswordCommand(userService);
            Command editUserCommand = new EditUserCommand(userService, userInfoService, addressService, doctorInfoService);
            Command errorCommand = new ErrorCommand();
            Command finishVisitCommand = new FinishVisitCommand(visitService);
            Command getDoctorsCommand = new GetDoctorsCommand(doctorInfoService);
            Command leaveDoctorFeedbackCommand = new LeaveDoctorFeedbackCommand(visitFeedbackService, visitService);
            Command leaveVisitorFeedbackCommand = new LeaveVisitorFeedbackCommand(visitFeedbackService, visitService);
            Command loginCommand = new LoginCommand(userService);
            Command logoutCommand = new LogoutCommand();
            Command makeAppointmentCommand = new MakeAppointmentCommand(visitService);
            Command registrationCommand = new RegistrationCommand(userService, userInfoService);
            Command showAllUsersCommand = new ShowAllUsersCommand(userService);
            Command showChooseVisitDateCommand = new ShowChooseVisitDateCommand(userService, userInfoService, doctorInfoService);
            Command showChooseVisitTimeCommand = new ShowChooseVisitTimeCommand(visitService, doctorInfoService, userService, userInfoService);
            Command showDoctorFeedbacksCommand = new ShowDoctorFeedbacksCommand(visitFeedbackService, userService);
            Command showDoctorVisitCommand = new ShowDoctorVisitsCommand(visitService);
            Command showEditUserPageCommand = new ShowEditUserPageCommand(userService, addressService, userInfoService, doctorInfoService);
            Command showProfileCommand = new ShowProfileCommand(userService, addressService, userInfoService, doctorInfoService);
            Command showVisitInfoCommand = new ShowVisitInfoCommand(visitService, visitFeedbackService, userService, userInfoService, doctorInfoService);
            Command showVisitorVisitsCommand = new ShowVisitorVisitsCommand(visitService);
            Command showWriteVisitReasonCommand = new ShowWriteVisitReasonCommand(visitService);
            Command findDoctorCommand = new FindDoctorCommand(doctorInfoService);
            Command findVisitsCommand = new FindVisitsCommand(visitService);

            commands.put(ApplicationCommandConstants.BLOCK_USER_CMD, blockUserCommand);
            commands.put(ApplicationCommandConstants.CANCEL_VISIT_CMD, cancelVisitCommand);
            commands.put(ApplicationCommandConstants.CHANGE_PASSWORD_CMD, changePasswordCommand);
            commands.put(ApplicationCommandConstants.EDIT_USER_CMD, editUserCommand);
            commands.put(ApplicationCommandConstants.ERR_CMD, errorCommand);
            commands.put(ApplicationCommandConstants.FINISH_VISIT_CMD, finishVisitCommand);
            commands.put(ApplicationCommandConstants.GET_DOCTORS_CMD, getDoctorsCommand);
            commands.put(ApplicationCommandConstants.LEAVE_DOCTOR_FEEDBACK_CMD, leaveDoctorFeedbackCommand);
            commands.put(ApplicationCommandConstants.LEAVE_VISITOR_FEEDBACK_CMD, leaveVisitorFeedbackCommand);
            commands.put(ApplicationCommandConstants.LOGIN_CMD, loginCommand);
            commands.put(ApplicationCommandConstants.LOGOUT_CMD, logoutCommand);
            commands.put(ApplicationCommandConstants.MAKE_APPOINTMENT_CMD, makeAppointmentCommand);
            commands.put(ApplicationCommandConstants.REGISTRATION_CMD, registrationCommand);
            commands.put(ApplicationCommandConstants.SHOW_USERS_CMD, showAllUsersCommand);
            commands.put(ApplicationCommandConstants.CHOOSE_DATE_CMD, showChooseVisitDateCommand);
            commands.put(ApplicationCommandConstants.CHOOSE_TIME_CMD, showChooseVisitTimeCommand);
            commands.put(ApplicationCommandConstants.SHOW_DOCTOR_FEEDBACKS_CMD, showDoctorFeedbacksCommand);
            commands.put(ApplicationCommandConstants.SHOW_DOCTOR_VISITS_CMD, showDoctorVisitCommand);
            commands.put(ApplicationCommandConstants.SHOW_EDIT_USER_CMD, showEditUserPageCommand);
            commands.put(ApplicationCommandConstants.SHOW_PROFILE_CMD, showProfileCommand);
            commands.put(ApplicationCommandConstants.SHOW_VISIT_INFO_CMD, showVisitInfoCommand);
            commands.put(ApplicationCommandConstants.SHOW_VISITOR_VISITS_CMD, showVisitorVisitsCommand);
            commands.put(ApplicationCommandConstants.WRITE_REASON_CMD, showWriteVisitReasonCommand);
            commands.put(ApplicationCommandConstants.FIND_DOCTORS_CMD, findDoctorCommand);
            commands.put(ApplicationCommandConstants.FIND_VISITS_CMD, findVisitsCommand);
        } catch (ConnectionPoolException e) {
            LOGGER.error(POOL_INIT_ERROR, e);
            throw new ContextException(POOL_INIT_ERROR, e);
        }
    }

    public void destroy() throws ContextException {
        if (ConnectionPoolImpl.getInstance() != null) {
            try {
                ConnectionPoolImpl.getInstance().destroy();
            } catch (ConnectionPoolException e) {
                LOGGER.error(POOL_DESTROYING_ERROR, e);
                throw new ContextException(POOL_DESTROYING_ERROR, e);
            }
        }
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
