package by.training.hospital.dao;

import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.entity.Role;
import by.training.hospital.entity.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);

    private static final String CREATE_USER = "INSERT INTO user_account (login, password, role_id) " +
            "VALUES (?,?, (SELECT id FROM user_role WHERE role_name = ?))";
    private static final String GET_USER_BY_LOGIN =
            "SELECT user_account.id, user_account.login, user_account.password, user_account.is_blocked, user_role.role_name " +
                    "FROM user_account " +
                    "INNER JOIN user_role ON user_account.role_id=user_role.id " +
                    "WHERE user_account.login = ?";
    private static final String GET_USER_BY_ID = "SELECT user_account.login, user_account.password, user_account.is_blocked," +
            " user_role.role_name " +
            "FROM user_account " +
            "INNER JOIN user_role ON user_account.role_id=user_role.id " +
            "WHERE user_account.id = ?";
    private static final String UPDATE_USER = "UPDATE user_account SET login = ?, password = ?, role_id = " +
            "(SELECT id FROM user_role WHERE role_name = ?), is_blocked = ? WHERE id = ?";
    private static final String DELETE_USER_BY_ID = "DELETE FROM user_account WHERE id = ?";
    private static final String GET_ALL_USERS = "SELECT user_account.id, user_account.login, user_account.password," +
            " user_account.is_blocked, user_role.role_name " +
            "FROM user_account " +
            "INNER JOIN user_role ON user_account.role_id=user_role.id " +
            "WHERE user_role.role_name != 'ADMIN'";

    private static final String GET_ALL_USERS_WITH_USER_INFO_BY_ROLE_FROM_TO = "SELECT user_account.id, user_account.login, user_account.password, " +
            "user_account.is_blocked, user_role.role_name, user_info.id, user_info.first_name, user_info.last_name, user_info.email, " +
            "user_info.phone " +
            "FROM user_account " +
            "INNER JOIN user_role ON user_account.role_id=user_role.id " +
            "INNER JOIN user_info ON user_account.id=user_info.user_id " +
            "WHERE user_role.role_name = ? " +
            "ORDER BY user_account.id LIMIT ?, ?";

    private static final String GET_ALL_USERS_WITH_USER_INFO_FROM_TO = "SELECT user_account.id, user_account.login, user_account.password, " +
            "user_account.is_blocked, user_role.role_name, user_info.id, user_info.first_name, user_info.last_name, user_info.email, " +
            "user_info.phone " +
            "FROM user_account " +
            "INNER JOIN user_role ON user_account.role_id=user_role.id " +
            "INNER JOIN user_info ON user_account.id=user_info.user_id " +
            "WHERE user_role.role_name != 'ADMIN' " +
            "ORDER BY user_account.id LIMIT ?, ?";

    private static final String GET_USER_COUNT_BY_ROLE = "SELECT count(*) AS count FROM user_account " +
            "INNER JOIN user_role ON user_account.role_id=user_role.id " +
            "WHERE user_role.role_name = ?";

    private static final String GET_USER_COUNT_EXCEPT_ADMIN = "SELECT count(*) AS count FROM user_account " +
            "INNER JOIN user_role ON user_account.role_id=user_role.id " +
            "WHERE user_role.role_name != 'ADMIN'";

    private ConnectionManager connectionManager;

    public UserDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public long create(UserDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {

            User entity = fromDTO(dto);

            int i = 0;
            preparedStatement.setString(++i, entity.getLogin());
            preparedStatement.setString(++i, entity.getPassword());
            preparedStatement.setString(++i, entity.getRole().toString());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            long id = 0;

            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            }

            return id;

        } catch (SQLException e) {
            LOGGER.error("Error during creating a new user in DB.", e);
            throw new DAOException("Error during creating a new user in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public UserDTO getById(Long id) throws DAOException, NoConcreteEntityInDatabaseException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String userLogin = resultSet.getString("user_account.login");
                String userPass = resultSet.getString("user_account.password");
                String userRole = resultSet.getString("user_role.role_name");
                boolean userIsBlocked = resultSet.getBoolean("user_account.is_blocked");
                User user = new User();
                user.setId(id);
                user.setLogin(userLogin);
                user.setPassword(userPass);
                user.setRole(Role.valueOf(userRole.toUpperCase()));
                user.setBlocked(userIsBlocked);
                return toDTO(user);

            }

            throw new NoConcreteEntityInDatabaseException("Error during retrieving an user with id = " + id + ".");

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving a user from DB.", e);
            throw new DAOException("Error during retrieving a user from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(UserDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {

            User entity = fromDTO(dto);
            int i = 0;
            preparedStatement.setString(++i, entity.getLogin());
            preparedStatement.setString(++i, entity.getPassword());
            preparedStatement.setString(++i, entity.getRole().toString());
            preparedStatement.setBoolean(++i, entity.isBlocked());
            preparedStatement.setLong(++i, entity.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during updating a user in DB.", e);
            throw new DAOException("Error during updating a user in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID)) {

            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during deleting a user from DB.", e);
            throw new DAOException("Error during deleting a user from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<UserDTO> getAll() throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<UserDTO> users = new ArrayList<>();
            while (resultSet.next()) {

                long userId = resultSet.getLong("user_account.id");
                String userLogin = resultSet.getString("user_account.login");
                String userPass = resultSet.getString("user_account.password");
                String userRole = resultSet.getString("user_role.role_name");
                boolean userIsBlocked = resultSet.getBoolean("user_account.is_blocked");
                User user = new User();
                user.setId(userId);
                user.setLogin(userLogin);
                user.setPassword(userPass);
                user.setRole(Role.valueOf(userRole.toUpperCase()));
                user.setBlocked(userIsBlocked);
                users.add(toDTO(user));

            }
            return users;
        } catch (SQLException e) {
            LOGGER.error("Error during retrieving all users from DB.", e);
            throw new DAOException("Error during retrieving all users from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public UserDTO getUserByLogin(String login) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_LOGIN)) {

            int i = 0;
            preparedStatement.setString(++i, login);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                long userId = resultSet.getLong("user_account.id");
                String userLogin = resultSet.getString("user_account.login");
                String userPass = resultSet.getString("user_account.password");
                Role userRole = Role.valueOf(resultSet.getString("user_role.role_name").toUpperCase());
                boolean userIsBlocked = resultSet.getBoolean("user_account.is_blocked");
                User user = new User();
                user.setId(userId);
                user.setLogin(userLogin);
                user.setPassword(userPass);
                user.setBlocked(userIsBlocked);
                user.setRole(userRole);

                return toDTO(user);

            }

            return null;

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving a user from DB by login.", e);
            throw new DAOException("Error during retrieving a user from DB by login.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    public boolean isLoginUnique(String login) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_LOGIN)) {

            int i = 0;
            preparedStatement.setString(++i, login);

            return !preparedStatement.executeQuery().next();

        } catch (SQLException e) {
            LOGGER.error("Error during checking a user's login in DB.", e);
            throw new DAOException("Error during checking a user's login in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<UserDTO> getAllUsersWithUserInfoByRoleFromTo(Long from, Long count, Role role) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS_WITH_USER_INFO_BY_ROLE_FROM_TO)) {

            preparedStatement.setString(1, role.toString());
            preparedStatement.setLong(2, from);
            preparedStatement.setLong(3, count);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSetToUserDTOList(resultSet);

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving users with info by role from DB.", e);
            throw new DAOException("Error during retrieving users with info by role from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<UserDTO> getAllUsersWithUserInfoFromTo(Long from, Long count) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS_WITH_USER_INFO_FROM_TO)) {

            preparedStatement.setLong(1, from);
            preparedStatement.setLong(2, count);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSetToUserDTOList(resultSet);

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving users with info from DB.", e);
            throw new DAOException("Error during retrieving users with info from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public Long getUserCountByRole(Role role) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_COUNT_BY_ROLE)) {

            preparedStatement.setString(1, role.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("count");
            }
            return 0L;

        } catch (SQLException e) {
            LOGGER.error("Error during getting count of users by role from DB.", e);
            throw new DAOException("Error during getting count of users by role from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public Long getAllUsersCount() throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_COUNT_EXCEPT_ADMIN)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("count");
            }
            return 0L;

        } catch (SQLException e) {
            LOGGER.error("Error during getting count of all users except ADMIN from DB.", e);
            throw new DAOException("Error during getting count of all users except ADMIN from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    private List<UserDTO> resultSetToUserDTOList(ResultSet resultSet) throws SQLException {
        List<UserDTO> users = new ArrayList<>();
        while (resultSet.next()) {

            long userId = resultSet.getLong("user_account.id");
            String userLogin = resultSet.getString("user_account.login");
            String userPass = resultSet.getString("user_account.password");
            String userRole = resultSet.getString("user_role.role_name");
            boolean userIsBlocked = resultSet.getBoolean("user_account.is_blocked");
            long userInfoId = resultSet.getLong("user_info.id");
            String firstName = resultSet.getString("user_info.first_name");
            String lastName = resultSet.getString("user_info.last_name");
            String phone = resultSet.getString("user_info.phone");
            String email = resultSet.getString("user_info.email");
            User user = new User();
            user.setId(userId);
            user.setLogin(userLogin);
            user.setPassword(userPass);
            user.setRole(Role.valueOf(userRole.toUpperCase()));
            user.setBlocked(userIsBlocked);
            UserDTO userDTO = toDTO(user);

            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.setId(userInfoId);
            userInfoDTO.setFirstName(firstName);
            userInfoDTO.setLastName(lastName);
            userInfoDTO.setPhone(phone);
            userInfoDTO.setEmail(email);

            userDTO.setUserInfo(userInfoDTO);
            users.add(userDTO);
        }
        return users;
    }

    private User fromDTO(UserDTO dto) {

        User entity = new User();
        entity.setId(dto.getId());
        entity.setLogin(dto.getLogin());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setBlocked(dto.isBlocked());

        return entity;
    }

    private UserDTO toDTO(User entity) {

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setLogin(entity.getLogin());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setBlocked(entity.isBlocked());

        return dto;
    }

}
