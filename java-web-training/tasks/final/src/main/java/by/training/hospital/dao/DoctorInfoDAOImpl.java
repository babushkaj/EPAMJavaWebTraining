package by.training.hospital.dao;

import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.entity.DoctorInfo;
import by.training.hospital.entity.Role;
import by.training.hospital.entity.Specialization;
import by.training.hospital.entity.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DoctorInfoDAOImpl implements DoctorInfoDAO {

    private static final Logger LOGGER = Logger.getLogger(DoctorInfoDAOImpl.class);

    private static final String CREATE_DOCTOR_INFO = "INSERT INTO doctor_info (description, user_id, spec_id) " +
            "VALUES (?,?, (SELECT id FROM specialization WHERE spec_name = ?))";

    private static final String GET_ALL_DOCTOR_INFO = "SELECT doctor_info.id, doctor_info.description," +
            " specialization.spec_name, days_of_week.day_name, doctor_info.user_id " +
            "FROM doctor_info " +
            "INNER JOIN doc_working_days ON doctor_info.id=doc_working_days.doctor_id " +
            "INNER JOIN days_of_week ON doc_working_days.day_id=days_of_week.id " +
            "INNER JOIN specialization ON doctor_info.spec_id=specialization.id";

    private static final String UPDATE_DOCTOR_INFO = "UPDATE doctor_info SET description = ?, user_id = ?, " +
            "spec_id = (SELECT id FROM specialization WHERE spec_name = ?) WHERE id = ?";

    private static final String GET_BY_ID = "SELECT doctor_info.user_id, doctor_info.description, specialization.spec_name, days_of_week.day_name " +
            "FROM doctor_info " +
            "INNER JOIN doc_working_days ON doctor_info.id=doc_working_days.doctor_id " +
            "INNER JOIN days_of_week ON doc_working_days.day_id=days_of_week.id " +
            "INNER JOIN specialization ON doctor_info.spec_id=specialization.id " +
            "WHERE doctor_info.id = ?";

    private static final String DELETE_DOCTOR_INFO_BY_ID = "DELETE FROM doctor_info WHERE id = ?";

    private static final String GET_BY_USER_ID = "SELECT doctor_info.id, doctor_info.description, specialization.spec_name, days_of_week.day_name " +
            "FROM doctor_info " +
            "INNER JOIN doc_working_days ON doctor_info.id=doc_working_days.doctor_id " +
            "INNER JOIN days_of_week ON doc_working_days.day_id=days_of_week.id " +
            "INNER JOIN specialization ON doctor_info.spec_id=specialization.id " +
            "WHERE doctor_info.user_id = ?";

    private static final String CREATE_WORKING_DAYS_FOR_DOCTOR = "INSERT INTO doc_working_days (doctor_id, day_id) " +
            "VALUES (?, (SELECT id FROM days_of_week WHERE day_name = ?))";

    private static final String DELETE_WORKING_DAYS_BY_DOCTOR_INFO_ID = "DELETE FROM doc_working_days WHERE doctor_id = ?";

    private static final String GET_ALL_UNBLOCKED_DOCTORS_WITH_INFO =
            "SELECT user_account.id, doctor_info.id AS doc_info_id, user_info.id AS user_info_id, user_account.login," +
                    " user_account.password, user_account.is_blocked, user_role.role_name, " +
                    "doctor_info.description, specialization.spec_name , days_of_week.day_name, " +
                    "user_info.first_name, user_info.last_name, user_info.email, user_info.phone " +
                    "FROM user_account " +
                    "INNER JOIN user_role ON user_account.role_id=user_role.id " +
                    "INNER JOIN doctor_info ON doctor_info.user_id=user_account.id " +
                    "INNER JOIN user_info ON user_info.user_id=user_account.id " +
                    "INNER JOIN doc_working_days ON doctor_info.id=doc_working_days.doctor_id " +
                    "INNER JOIN days_of_week ON doc_working_days.day_id=days_of_week.id " +
                    "INNER JOIN specialization ON doctor_info.spec_id=specialization.id " +
                    "WHERE user_account.is_blocked = false " +
                    "ORDER BY user_account.id";

    private static final String GET_ALL_UNBLOCKED_DOCTORS_WITH_INFO_FROM_TO =
            "SELECT user_account.id, doctor_info.id AS doc_info_id, user_info.id AS user_info_id, user_account.login," +
                    " user_account.password, user_account.is_blocked, user_role.role_name, " +
                    "doctor_info.description, specialization.spec_name , days_of_week.day_name, " +
                    "user_info.first_name, user_info.last_name, user_info.email, user_info.phone " +
                    "FROM user_account " +
                    "INNER JOIN user_role ON user_account.role_id=user_role.id " +
                    "INNER JOIN doctor_info ON doctor_info.user_id=user_account.id " +
                    "INNER JOIN user_info ON user_info.user_id=user_account.id " +
                    "INNER JOIN doc_working_days ON doctor_info.id=doc_working_days.doctor_id " +
                    "INNER JOIN days_of_week ON doc_working_days.day_id=days_of_week.id " +
                    "INNER JOIN specialization ON doctor_info.spec_id=specialization.id " +
                    "WHERE user_account.is_blocked = false " +
                    "ORDER BY user_account.id LIMIT ?,?";

    private static final String GET_DOCTOR_INFO_COUNT = "SELECT count(*) AS count FROM doctor_info " +
            "INNER JOIN user_account ON " +
            "doctor_info.user_id = user_account.id " +
            "where user_account.is_blocked = false ";

    private ConnectionManager connectionManager;

    public DoctorInfoDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public long create(DoctorInfoDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_DOCTOR_INFO, Statement.RETURN_GENERATED_KEYS)) {

            DoctorInfo entity = fromDTO(dto);

            int i = 0;
            preparedStatement.setString(++i, entity.getDescription());
            preparedStatement.setLong(++i, entity.getUserId());
            preparedStatement.setString(++i, entity.getSpec().toString());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            long id = 0;

            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            }

            return id;

        } catch (SQLException e) {
            LOGGER.error("Error during creating a new doctor info in DB.", e);
            throw new DAOException("Error during creating a new doctor info in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public DoctorInfoDTO getById(Long id) throws DAOException, NoConcreteEntityInDatabaseException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String docDesc = resultSet.getString("doctor_info.description");
                Specialization docSpec = Specialization.valueOf(resultSet.getString("specialization.spec_name"));
                long userId = resultSet.getLong("doctor_info.user_id");

                DoctorInfo doctorInfo = new DoctorInfo();
                doctorInfo.setId(id);
                doctorInfo.setDescription(docDesc);
                doctorInfo.setSpec(docSpec);
                doctorInfo.setUserId(userId);

                DoctorInfoDTO doctorInfoDTO = toDTO(doctorInfo);

                Set<DayOfWeek> workingDays = new HashSet<>();
                DayOfWeek day = DayOfWeek.valueOf(resultSet.getString("days_of_week.day_name"));
                workingDays.add(day);

                while (resultSet.next()) {
                    day = DayOfWeek.valueOf(resultSet.getString("days_of_week.day_name"));
                    workingDays.add(day);
                }

                doctorInfoDTO.setWorkingDays(workingDays);

                return doctorInfoDTO;
            }

            throw new NoConcreteEntityInDatabaseException("Error during retrieving an doctor info with id = " + id + ".");

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving doctor info from DB.", e);
            throw new DAOException("Error during retrieving doctor info from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(DoctorInfoDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DOCTOR_INFO)) {

            DoctorInfo entity = fromDTO(dto);
            int i = 0;
            preparedStatement.setString(++i, entity.getDescription());
            preparedStatement.setLong(++i, entity.getUserId());
            preparedStatement.setString(++i, entity.getSpec().toString());
            preparedStatement.setLong(++i, entity.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during updating a doctor info in DB.", e);
            throw new DAOException("Error during updating a doctor info in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DOCTOR_INFO_BY_ID)) {

            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during deleting a doctor info from DB.", e);
            throw new DAOException("Error during deleting a doctor info from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<DoctorInfoDTO> getAll() throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_DOCTOR_INFO)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<DoctorInfoDTO> infoList = new ArrayList<>();

            if (resultSet.next()) {
                DoctorInfo doctorInfo = new DoctorInfo();
                long docInfoId = resultSet.getLong("doctor_info.id");
                String docDesc = resultSet.getString("doctor_info.description");
                Specialization docSpec = Specialization.valueOf(resultSet.getString("specialization.spec_name"));
                long userId = resultSet.getLong("doctor_info.user_id");

                doctorInfo.setId(docInfoId);
                doctorInfo.setDescription(docDesc);
                doctorInfo.setSpec(docSpec);
                doctorInfo.setUserId(userId);

                Set<DayOfWeek> workingDays = new HashSet<>();
                DayOfWeek day = DayOfWeek.valueOf(resultSet.getString("days_of_week.day_name"));
                workingDays.add(day);

                while (resultSet.next()) {
                    if (doctorInfo.getId() == resultSet.getLong("doctor_info.id")) {
                        day = DayOfWeek.valueOf(resultSet.getString("days_of_week.day_name"));
                        workingDays.add(day);
                    } else {
                        DoctorInfoDTO doctorInfoDTO = toDTO(doctorInfo);
                        doctorInfoDTO.setWorkingDays(workingDays);
                        infoList.add(doctorInfoDTO);

                        doctorInfo = new DoctorInfo();

                        docInfoId = resultSet.getLong("doctor_info.id");
                        docDesc = resultSet.getString("doctor_info.description");
                        docSpec = Specialization.valueOf(resultSet.getString("specialization.spec_name"));
                        userId = resultSet.getLong("doctor_info.user_id");

                        doctorInfo.setId(docInfoId);
                        doctorInfo.setDescription(docDesc);
                        doctorInfo.setSpec(docSpec);
                        doctorInfo.setUserId(userId);

                        workingDays = new HashSet<>();
                        day = DayOfWeek.valueOf(resultSet.getString("days_of_week.day_name"));
                        workingDays.add(day);
                    }
                }

                DoctorInfoDTO doctorInfoDTO = toDTO(doctorInfo);
                doctorInfoDTO.setWorkingDays(workingDays);
                infoList.add(doctorInfoDTO);
                return infoList;
            }

            return null;

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving all doctor info from DB.", e);
            throw new DAOException("Error during retrieving all doctor info from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public DoctorInfoDTO getDoctorInfoByUserId(long userId) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_USER_ID)) {

            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                long docInfoId = resultSet.getLong("doctor_info.id");
                String docDesc = resultSet.getString("doctor_info.description");
                Specialization docSpec = Specialization.valueOf(resultSet.getString("specialization.spec_name"));

                DoctorInfo doctorInfo = new DoctorInfo();
                doctorInfo.setId(docInfoId);
                doctorInfo.setDescription(docDesc);
                doctorInfo.setSpec(docSpec);
                doctorInfo.setUserId(userId);

                DoctorInfoDTO doctorInfoDTO = toDTO(doctorInfo);

                Set<DayOfWeek> workingDays = new HashSet<>();
                DayOfWeek day = DayOfWeek.valueOf(resultSet.getString("days_of_week.day_name"));
                workingDays.add(day);

                while (resultSet.next()) {
                    day = DayOfWeek.valueOf(resultSet.getString("days_of_week.day_name"));
                    workingDays.add(day);
                }

                doctorInfoDTO.setWorkingDays(workingDays);

                return doctorInfoDTO;
            }

            return null;

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving doctor info from DB by userId.", e);
            throw new DAOException("Error during retrieving doctor info from DB by userId.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean createWorkingDaysForDoctor(Long doctorInfoId, Set<DayOfWeek> days) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_WORKING_DAYS_FOR_DOCTOR)) {

            for (DayOfWeek day : days) {
                int i = 0;

                preparedStatement.setLong(++i, doctorInfoId);
                preparedStatement.setString(++i, day.toString());

                preparedStatement.addBatch();
            }

            int[] count = preparedStatement.executeBatch();

            return count.length > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during creating a new working days for doctor in DB.", e);
            throw new DAOException("Error during creating a new working days for doctor in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }

    }

    @Override
    public boolean deleteWorkingDaysByDoctorInfoId(Long doctorInfoId) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_WORKING_DAYS_BY_DOCTOR_INFO_ID)) {

            preparedStatement.setLong(1, doctorInfoId);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during deleting working days for concrete doctor from DB.", e);
            throw new DAOException("Error during deleting working days for concrete doctor from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<UserDTO> getAllUnblockedDoctorsWithInfo() throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_UNBLOCKED_DOCTORS_WITH_INFO)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSetToUserDTOList(resultSet);

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving users with doctor info from DB.", e);
            throw new DAOException("Error during retrieving users with doctor info from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<UserDTO> getAllUnblockedDoctorsWithInfoFromTo(Long from, Long count) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_UNBLOCKED_DOCTORS_WITH_INFO_FROM_TO)) {

            preparedStatement.setLong(1, from);
            preparedStatement.setLong(2, count);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSetToUserDTOList(resultSet);

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving users with doctor info from DB.", e);
            throw new DAOException("Error during retrieving users with doctor info from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public Long getUnblockedDoctorsCount() throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_DOCTOR_INFO_COUNT)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("count");
            }
            return 0L;

        } catch (SQLException e) {
            LOGGER.error("Error during getting count of unblocked doctors from DB.", e);
            throw new DAOException("Error during getting count of unblocked doctors from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    private List<UserDTO> resultSetToUserDTOList(ResultSet resultSet) throws SQLException {
        List<UserDTO> users = new ArrayList<>();
        if (resultSet.next()) {

            User user = new User();
            long userId = resultSet.getLong("user_account.id");
            String userLogin = resultSet.getString("user_account.login");
            String userPass = resultSet.getString("user_account.password");
            String userRole = resultSet.getString("user_role.role_name");
            boolean userIsBlocked = resultSet.getBoolean("user_account.is_blocked");
            user.setId(userId);
            user.setLogin(userLogin);
            user.setPassword(userPass);
            user.setRole(Role.valueOf(userRole.toUpperCase()));
            user.setBlocked(userIsBlocked);

            UserDTO userDTO = toDoctorWithInfoDTO(user);

            UserInfoDTO userInfoDTO = new UserInfoDTO();
            long userInfoId = resultSet.getLong("user_info_id");
            String firstName = resultSet.getString("user_info.first_name");
            String lastName = resultSet.getString("user_info.last_name");
            String email = resultSet.getString("user_info.email");
            String phone = resultSet.getString("user_info.phone");
            userInfoDTO.setId(userInfoId);
            userInfoDTO.setUserId(userId);
            userInfoDTO.setFirstName(firstName);
            userInfoDTO.setLastName(lastName);
            userInfoDTO.setEmail(email);
            userInfoDTO.setPhone(phone);

            userDTO.setUserInfo(userInfoDTO);

            long docInfoId = resultSet.getLong("doc_info_id");
            String docDesc = resultSet.getString("doctor_info.description");
            Specialization docSpec = Specialization.valueOf(resultSet.getString("specialization.spec_name"));

            DoctorInfoDTO doctorInfoDTO = new DoctorInfoDTO();

            doctorInfoDTO.setId(docInfoId);
            doctorInfoDTO.setDescription(docDesc);
            doctorInfoDTO.setSpec(docSpec);
            doctorInfoDTO.setUserId(userId);

            Set<DayOfWeek> workingDays = new HashSet<>();
            DayOfWeek day = DayOfWeek.valueOf(resultSet.getString("days_of_week.day_name"));
            workingDays.add(day);

            while (resultSet.next()) {
                if (userDTO.getId() == resultSet.getLong("user_account.id")) {
                    day = DayOfWeek.valueOf(resultSet.getString("days_of_week.day_name"));
                    workingDays.add(day);
                } else {
                    doctorInfoDTO.setWorkingDays(workingDays);
                    userDTO.setDoctorInfo(doctorInfoDTO);

                    users.add(userDTO);

                    user = new User();
                    userId = resultSet.getLong("user_account.id");
                    userLogin = resultSet.getString("user_account.login");
                    userPass = resultSet.getString("user_account.password");
                    userRole = resultSet.getString("user_role.role_name");
                    userIsBlocked = resultSet.getBoolean("user_account.is_blocked");
                    user.setId(userId);
                    user.setLogin(userLogin);
                    user.setPassword(userPass);
                    user.setRole(Role.valueOf(userRole.toUpperCase()));
                    user.setBlocked(userIsBlocked);

                    userDTO = toDoctorWithInfoDTO(user);

                    userInfoDTO = new UserInfoDTO();
                    userInfoId = resultSet.getLong("user_info_id");
                    firstName = resultSet.getString("user_info.first_name");
                    lastName = resultSet.getString("user_info.last_name");
                    email = resultSet.getString("user_info.email");
                    phone = resultSet.getString("user_info.phone");
                    userInfoDTO.setId(userInfoId);
                    userInfoDTO.setUserId(userId);
                    userInfoDTO.setFirstName(firstName);
                    userInfoDTO.setLastName(lastName);
                    userInfoDTO.setEmail(email);
                    userInfoDTO.setPhone(phone);

                    userDTO.setUserInfo(userInfoDTO);

                    docInfoId = resultSet.getLong("doc_info_id");
                    docDesc = resultSet.getString("doctor_info.description");
                    docSpec = Specialization.valueOf(resultSet.getString("specialization.spec_name"));

                    doctorInfoDTO = new DoctorInfoDTO();

                    doctorInfoDTO.setId(docInfoId);
                    doctorInfoDTO.setDescription(docDesc);
                    doctorInfoDTO.setSpec(docSpec);
                    doctorInfoDTO.setUserId(userId);

                    workingDays = new HashSet<>();
                    day = DayOfWeek.valueOf(resultSet.getString("days_of_week.day_name"));
                    workingDays.add(day);
                }
            }
            doctorInfoDTO.setWorkingDays(workingDays);
            userDTO.setDoctorInfo(doctorInfoDTO);
            users.add(userDTO);
        }
        return users;
    }

    private DoctorInfo fromDTO(DoctorInfoDTO dto) {

        DoctorInfo entity = new DoctorInfo();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        entity.setSpec(dto.getSpec());
        entity.setUserId(dto.getUserId());

        return entity;
    }

    private DoctorInfoDTO toDTO(DoctorInfo entity) {

        DoctorInfoDTO dto = new DoctorInfoDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setSpec(entity.getSpec());
        dto.setUserId(entity.getUserId());

        return dto;
    }

    private UserDTO toDoctorWithInfoDTO(User entity) {

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setLogin(entity.getLogin());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setBlocked(entity.isBlocked());

        return dto;
    }

}
