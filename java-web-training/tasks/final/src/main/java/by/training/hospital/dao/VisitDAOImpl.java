package by.training.hospital.dao;

import by.training.hospital.dto.VisitDTO;
import by.training.hospital.entity.Visit;
import by.training.hospital.entity.VisitStatus;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VisitDAOImpl implements VisitDAO {

    private static final Logger LOGGER = Logger.getLogger(VisitDAOImpl.class);

    private static final String CREATE_VISIT =
            "INSERT INTO visit (cause, result, visit_date, visitor_id, doctor_id, status_id) " +
                    "VALUES (?,?,?,?,?, (SELECT id FROM visit_status WHERE status_name = ?))";

    private static final String GET_VISIT_BY_ID = "SELECT visit.cause, visit.result, visit.visit_date," +
            " visit.visitor_id, visit.doctor_id, visit_status.status_name " +
            "FROM visit " +
            "INNER JOIN visit_status ON visit.status_id=visit_status.id " +
            "WHERE visit.id = ?";

    private static final String UPDATE_VISIT = "UPDATE visit SET cause = ?, result = ?, visit_date = ?, visitor_id = ?, " +
            "doctor_id = ?, status_id = (SELECT id FROM visit_status WHERE status_name = ?) WHERE id = ?";

    private static final String DELETE_VISIT_BY_ID = "DELETE FROM visit WHERE id = ?";

    private static final String GET_ALL_VISITS = "SELECT visit.id, visit.cause, visit.result, visit.visit_date," +
            " visit.visitor_id, visit.doctor_id, visit_status.status_name " +
            "FROM visit " +
            "INNER JOIN visit_status ON visit.status_id=visit_status.id ";

    private static final String GET_VISITS_BY_DOCTOR_ID = "SELECT visit.id, visit.cause, visit.result, visit.visit_date," +
            " visit.visitor_id, visit_status.status_name, user_info.first_name, user_info.last_name " +
            "FROM visit " +
            "INNER JOIN visit_status ON visit.status_id=visit_status.id " +
            "INNER JOIN user_info ON visit.visitor_id=user_info.user_id " +
            "WHERE visit.doctor_id = ?";

    private static final String GET_VISITS_BY_VISITOR_ID = "SELECT visit.id, visit.cause, visit.result, visit.visit_date," +
            " visit.doctor_id, visit_status.status_name, user_info.first_name, user_info.last_name " +
            "FROM visit " +
            "INNER JOIN visit_status ON visit.status_id=visit_status.id " +
            "INNER JOIN user_info ON visit.doctor_id=user_info.user_id " +
            "WHERE visit.visitor_id = ?";

    private ConnectionManager connectionManager;

    public VisitDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public long create(VisitDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_VISIT, Statement.RETURN_GENERATED_KEYS)) {

            Visit entity = fromDTO(dto);

            int i = 0;
            preparedStatement.setString(++i, entity.getCause());
            preparedStatement.setString(++i, entity.getResult());
            preparedStatement.setLong(++i, entity.getDate());
            preparedStatement.setLong(++i, entity.getVisitorId());
            preparedStatement.setLong(++i, entity.getDoctorId());
            preparedStatement.setString(++i, entity.getVisitStatus().toString());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            long id = 0;

            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            }

            return id;

        } catch (SQLException e) {
            LOGGER.error("Error during creating a new visit in DB.", e);
            throw new DAOException("Error during creating a new visit in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public VisitDTO getById(Long id) throws DAOException, NoConcreteEntityInDatabaseException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_VISIT_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String cause = resultSet.getString("visit.cause");
                String result = resultSet.getString("visit.result");
                long date = resultSet.getLong("visit.visit_date");
                long visitorId = resultSet.getLong("visit.visitor_id");
                long doctorId = resultSet.getLong("visit.doctor_id");
                VisitStatus status = VisitStatus.valueOf(resultSet.getString("visit_status.status_name"));
                Visit visit = new Visit();
                visit.setId(id);
                visit.setCause(cause);
                visit.setResult(result);
                visit.setDate(date);
                visit.setVisitorId(visitorId);
                visit.setDoctorId(doctorId);
                visit.setVisitStatus(status);

                return toDTO(visit);
            }
            throw new NoConcreteEntityInDatabaseException("Error during retrieving a visit with id = " + id + ".");

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving a visit from DB.", e);
            throw new DAOException("Error during retrieving a visit from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(VisitDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_VISIT)) {

            Visit entity = fromDTO(dto);
            int i = 0;
            preparedStatement.setString(++i, entity.getCause());
            preparedStatement.setString(++i, entity.getResult());
            preparedStatement.setLong(++i, entity.getDate());
            preparedStatement.setLong(++i, entity.getVisitorId());
            preparedStatement.setLong(++i, entity.getDoctorId());
            preparedStatement.setString(++i, entity.getVisitStatus().toString());
            preparedStatement.setLong(++i, entity.getId());


            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during updating a visit in DB.", e);
            throw new DAOException("Error during updating a visit in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_VISIT_BY_ID)) {

            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during deleting a visit from DB.", e);
            throw new DAOException("Error during deleting a visit from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<VisitDTO> getAll() throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_VISITS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<VisitDTO> visits = new ArrayList<>();
            while (resultSet.next()) {

                long visitId = resultSet.getLong("visit.id");
                String cause = resultSet.getString("visit.cause");
                String result = resultSet.getString("visit.result");
                long date = resultSet.getLong("visit.visit_date");
                long visitorId = resultSet.getLong("visit.visitor_id");
                long doctorId = resultSet.getLong("visit.doctor_id");
                VisitStatus status = VisitStatus.valueOf(resultSet.getString("visit_status.status_name"));
                Visit visit = new Visit();
                visit.setId(visitId);
                visit.setCause(cause);
                visit.setResult(result);
                visit.setDate(date);
                visit.setVisitorId(visitorId);
                visit.setDoctorId(doctorId);
                visit.setVisitStatus(status);

                visits.add(toDTO(visit));
            }
            return visits;
        } catch (SQLException e) {
            LOGGER.error("Error during retrieving all visits from DB.", e);
            throw new DAOException("Error during retrieving all visits from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<VisitDTO> getVisitsByDoctorId(long doctorId) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_VISITS_BY_DOCTOR_ID)) {

            preparedStatement.setLong(1, doctorId);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<VisitDTO> visits = new ArrayList<>();
            while (resultSet.next()) {

                String cause = resultSet.getString("visit.cause");
                String result = resultSet.getString("visit.result");
                long date = resultSet.getLong("visit.visit_date");
                long visitorId = resultSet.getLong("visit.visitor_id");
                long visitId = resultSet.getLong("visit.id");
                VisitStatus status = VisitStatus.valueOf(resultSet.getString("visit_status.status_name"));
                String visitorFirstName = resultSet.getString("user_info.first_name");
                String visitorLastName = resultSet.getString("user_info.last_name");
                Visit visit = new Visit();
                visit.setId(visitId);
                visit.setCause(cause);
                visit.setResult(result);
                visit.setDate(date);
                visit.setVisitorId(visitorId);
                visit.setDoctorId(doctorId);
                visit.setVisitStatus(status);

                VisitDTO visitDTO = toDTO(visit);
                visitDTO.setVisitorFirstName(visitorFirstName);
                visitDTO.setVisitorLastName(visitorLastName);

                visits.add(visitDTO);
            }
            return visits;

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving visits from DB by doctorId.", e);
            throw new DAOException("Error during retrieving visits from DB by doctorId.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<VisitDTO> getVisitsByVisitorId(long visitorId) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_VISITS_BY_VISITOR_ID)) {

            preparedStatement.setLong(1, visitorId);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<VisitDTO> visits = new ArrayList<>();
            while (resultSet.next()) {

                String cause = resultSet.getString("visit.cause");
                String result = resultSet.getString("visit.result");
                long date = resultSet.getLong("visit.visit_date");
                long doctorId = resultSet.getLong("visit.doctor_id");
                long visitId = resultSet.getLong("visit.id");
                VisitStatus status = VisitStatus.valueOf(resultSet.getString("visit_status.status_name"));
                String doctorFirstName = resultSet.getString("user_info.first_name");
                String doctorLastName = resultSet.getString("user_info.last_name");
                Visit visit = new Visit();
                visit.setId(visitId);
                visit.setCause(cause);
                visit.setResult(result);
                visit.setDate(date);
                visit.setVisitorId(visitorId);
                visit.setDoctorId(doctorId);
                visit.setVisitStatus(status);

                VisitDTO visitDTO = toDTO(visit);
                visitDTO.setDoctorFirstName(doctorFirstName);
                visitDTO.setDoctorLastName(doctorLastName);

                visits.add(visitDTO);
            }
            return visits;

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving visits from DB by visitorId.", e);
            throw new DAOException("Error during retrieving visits from DB by visitorId.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    private Visit fromDTO(VisitDTO dto) {

        Visit entity = new Visit();
        entity.setId(dto.getId());
        entity.setCause(dto.getCause());
        entity.setResult(dto.getResult());
        entity.setDate(dto.getDate().getTime());
        entity.setVisitStatus(dto.getVisitStatus());
        entity.setVisitorId(dto.getVisitorId());
        entity.setDoctorId(dto.getDoctorId());

        return entity;
    }

    private VisitDTO toDTO(Visit entity) {

        VisitDTO dto = new VisitDTO();
        dto.setId(entity.getId());
        dto.setCause(entity.getCause());
        dto.setResult(entity.getResult());
        dto.setDate(new Date(entity.getDate()));
        dto.setVisitStatus(entity.getVisitStatus());
        dto.setVisitorId(entity.getVisitorId());
        dto.setDoctorId(entity.getDoctorId());

        return dto;
    }
}

