package by.training.hospital.dao;

import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.entity.UserInfo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserInfoDAOImpl implements UserInfoDAO {

    private static final Logger LOGGER = Logger.getLogger(UserInfoDAOImpl.class);

    private static final String CREATE_USER_INFO = "INSERT INTO user_info (first_name, last_name, email, phone, user_id) VALUES (?,?,?,?,?)";
    private static final String GET_USER_INFO_BY_ID = "SELECT first_name, last_name, email, phone, user_id FROM user_info WHERE id = ?";
    private static final String UPDATE_USER_INFO = "UPDATE user_info SET first_name = ?, last_name = ?, email = ?, phone = ?, user_id = ? WHERE id = ?";
    private static final String DELETE_USER_INFO_BY_ID = "DELETE FROM user_info WHERE id = ?";
    private static final String GET_ALL_USER_INFO = "SELECT id, first_name, last_name, email, phone, user_id FROM user_info";

    private static final String GET_USER_INFO_BY_USER_ID = "SELECT id, first_name, last_name, email, phone FROM user_info WHERE user_id = ?";
    private static final String GET_USER_INFO_BY_EMAIL = "SELECT id, first_name, last_name, email, phone FROM user_info WHERE email = ?";

    private ConnectionManager connectionManager;

    public UserInfoDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public long create(UserInfoDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER_INFO, Statement.RETURN_GENERATED_KEYS)) {

            UserInfo entity = fromDTO(dto);

            int i = 0;
            preparedStatement.setString(++i, entity.getFirstName());
            preparedStatement.setString(++i, entity.getLastName());
            preparedStatement.setString(++i, entity.getEmail());
            preparedStatement.setString(++i, entity.getPhone());
            preparedStatement.setLong(++i, entity.getUserId());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            long id = 0;

            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            }

            return id;

        } catch (SQLException e) {
            LOGGER.error("Error during creating a new user info in DB.", e);
            throw new DAOException("Error during creating a new user info in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public UserInfoDTO getById(Long id) throws DAOException, NoConcreteEntityInDatabaseException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_INFO_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                long userId = resultSet.getLong("user_id");
                UserInfo userInfo = new UserInfo();
                userInfo.setId(id);
                userInfo.setFirstName(firstName);
                userInfo.setLastName(lastName);
                userInfo.setEmail(email);
                userInfo.setPhone(phone);
                userInfo.setUserId(userId);
                return toDTO(userInfo);

            }

            throw new NoConcreteEntityInDatabaseException("Error during retrieving an user info with id = " + id + ".");

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving an user info from DB.", e);
            throw new DAOException("Error during retrieving an user info from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(UserInfoDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_INFO)) {

            UserInfo entity = fromDTO(dto);

            int i = 0;
            preparedStatement.setString(++i, entity.getFirstName());
            preparedStatement.setString(++i, entity.getLastName());
            preparedStatement.setString(++i, entity.getEmail());
            preparedStatement.setString(++i, entity.getPhone());
            preparedStatement.setLong(++i, entity.getUserId());
            preparedStatement.setLong(++i, entity.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during updating an user info in DB.", e);
            throw new DAOException("Error during updating an user info in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_INFO_BY_ID)) {

            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during deleting an user info from DB.", e);
            throw new DAOException("Error during deleting an user info from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<UserInfoDTO> getAll() throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USER_INFO)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<UserInfoDTO> userInfoList = new ArrayList<>();
            while (resultSet.next()) {

                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                long userId = resultSet.getLong("user_id");
                UserInfo userInfo = new UserInfo();
                userInfo.setId(id);
                userInfo.setFirstName(firstName);
                userInfo.setLastName(lastName);
                userInfo.setEmail(email);
                userInfo.setPhone(phone);
                userInfo.setUserId(userId);

                userInfoList.add(toDTO(userInfo));
            }
            return userInfoList;
        } catch (SQLException e) {
            LOGGER.error("Error during retrieving all user info from DB.", e);
            throw new DAOException("Error during retrieving all user info from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public UserInfoDTO getUserInfoByUserId(long userId) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_INFO_BY_USER_ID)) {

            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                UserInfo userInfo = new UserInfo();
                userInfo.setId(id);
                userInfo.setFirstName(firstName);
                userInfo.setLastName(lastName);
                userInfo.setEmail(email);
                userInfo.setPhone(phone);
                userInfo.setUserId(userId);

                return toDTO(userInfo);
            }

            return null;

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving a user info from DB by userId.", e);
            throw new DAOException("Error during retrieving a user info from DB by userId.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    public boolean isEmailUnique(String email) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_INFO_BY_EMAIL)) {

            int i = 0;
            preparedStatement.setString(++i, email);

            return !preparedStatement.executeQuery().next();

        } catch (SQLException e) {
            LOGGER.error("Error during checking a user's email in DB.", e);
            throw new DAOException("Error during checking a user's email in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    private UserInfo fromDTO(UserInfoDTO dto) {

        UserInfo entity = new UserInfo();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setUserId(dto.getUserId());

        return entity;
    }

    private UserInfoDTO toDTO(UserInfo entity) {

        UserInfoDTO dto = new UserInfoDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setUserId(entity.getUserId());

        return dto;
    }
}
