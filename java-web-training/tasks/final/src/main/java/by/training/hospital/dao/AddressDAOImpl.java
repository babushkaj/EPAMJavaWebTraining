package by.training.hospital.dao;

import by.training.hospital.dto.AddressDTO;
import by.training.hospital.entity.Address;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAOImpl implements AddressDAO {

    private static final Logger LOGGER = Logger.getLogger(AddressDAOImpl.class);

    private static final String CREATE_ADDRESS = "INSERT INTO address (region, city, street, house, apartment, user_id) VALUES (?,?,?,?,?,?)";
    private static final String GET_ADDRESS_BY_ID = "SELECT region, city, street, house, apartment, user_id FROM address WHERE id = ?";
    private static final String UPDATE_ADDRESS = "UPDATE address SET region = ?, city = ?, street = ?, house = ?, apartment = ?, user_id = ? WHERE id = ?";
    private static final String DELETE_ADDRESS_BY_ID = "DELETE FROM address WHERE id = ?";
    private static final String GET_ALL_ADDRESSES = "SELECT id, region, city, street, house, apartment, user_id FROM address";

    private static final String GET_ADDRESS_BY_USER_ID = "SELECT id, region, city, street, house, apartment FROM address WHERE user_id = ?";

    private ConnectionManager connectionManager;

    public AddressDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public long create(AddressDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ADDRESS, Statement.RETURN_GENERATED_KEYS)) {

            Address entity = fromDTO(dto);

            int i = 0;
            preparedStatement.setString(++i, entity.getRegion());
            preparedStatement.setString(++i, entity.getCity());
            preparedStatement.setString(++i, entity.getStreet());
            preparedStatement.setString(++i, entity.getHouse());
            preparedStatement.setString(++i, entity.getApartment());
            preparedStatement.setLong(++i, entity.getUserId());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            long id = 0;

            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            }
            return id;

        } catch (SQLException e) {
            LOGGER.error("Error during creating a new address in DB.", e);
            throw new DAOException("Error during creating a new address in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public AddressDTO getById(Long id) throws DAOException, NoConcreteEntityInDatabaseException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ADDRESS_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Address address = new Address();
                String region = resultSet.getString("region");
                String city = resultSet.getString("city");
                String street = resultSet.getString("street");
                String house = resultSet.getString("house");
                String apartment = resultSet.getString("apartment");
                long userId = resultSet.getLong("user_id");
                address.setId(id);
                address.setRegion(region);
                address.setCity(city);
                address.setStreet(street);
                address.setHouse(house);
                address.setApartment(apartment);
                address.setUserId(userId);

                return toDTO(address);
            }
            throw new NoConcreteEntityInDatabaseException("Error during retrieving an address with id = " + id + ".");

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving an address from DB.", e);
            throw new DAOException("Error during retrieving an address from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(AddressDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ADDRESS)) {

            Address entity = fromDTO(dto);

            int i = 0;
            preparedStatement.setString(++i, entity.getRegion());
            preparedStatement.setString(++i, entity.getCity());
            preparedStatement.setString(++i, entity.getStreet());
            preparedStatement.setString(++i, entity.getHouse());
            preparedStatement.setString(++i, entity.getApartment());
            preparedStatement.setLong(++i, entity.getUserId());
            preparedStatement.setLong(++i, entity.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during updating an address in DB.", e);
            throw new DAOException("Error during updating an address in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ADDRESS_BY_ID)) {

            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during deleting an address from DB.", e);
            throw new DAOException("Error during deleting an address from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<AddressDTO> getAll() throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ADDRESSES)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<AddressDTO> addresses = new ArrayList<>();
            while (resultSet.next()) {

                long id = resultSet.getLong("id");
                String region = resultSet.getString("region");
                String city = resultSet.getString("city");
                String street = resultSet.getString("street");
                String house = resultSet.getString("house");
                String apartment = resultSet.getString("apartment");
                long userId = resultSet.getLong("user_id");
                Address address = new Address();
                address.setId(id);
                address.setRegion(region);
                address.setCity(city);
                address.setStreet(street);
                address.setHouse(house);
                address.setApartment(apartment);
                address.setUserId(userId);

                addresses.add(toDTO(address));

            }
            return addresses;
        } catch (SQLException e) {
            LOGGER.error("Error during retrieving all addresses from DB.", e);
            throw new DAOException("Error during retrieving all addresses from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public AddressDTO getByUserId(long userId) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ADDRESS_BY_USER_ID)) {

            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                long id = resultSet.getLong("id");
                String region = resultSet.getString("region");
                String city = resultSet.getString("city");
                String street = resultSet.getString("street");
                String house = resultSet.getString("house");
                String apartment = resultSet.getString("apartment");
                Address address = new Address();
                address.setId(id);
                address.setRegion(region);
                address.setCity(city);
                address.setStreet(street);
                address.setHouse(house);
                address.setApartment(apartment);
                address.setUserId(userId);

                return toDTO(address);
            }

            return null;

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving an address from DB by userId.", e);
            throw new DAOException("Error during retrieving an address from DB by userId.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    private Address fromDTO(AddressDTO dto) {

        Address entity = new Address();
        entity.setId(dto.getId());
        entity.setRegion(dto.getRegion());
        entity.setCity(dto.getCity());
        entity.setStreet(dto.getStreet());
        entity.setHouse(dto.getHouse());
        entity.setApartment(dto.getApartment());
        entity.setUserId(dto.getUserId());

        return entity;
    }

    private AddressDTO toDTO(Address entity) {

        AddressDTO dto = new AddressDTO();
        dto.setId(entity.getId());
        dto.setRegion(entity.getRegion());
        dto.setCity(entity.getCity());
        dto.setStreet(entity.getStreet());
        dto.setHouse(entity.getHouse());
        dto.setApartment(entity.getApartment());
        dto.setUserId(entity.getUserId());

        return dto;
    }
}
