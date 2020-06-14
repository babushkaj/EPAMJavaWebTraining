package by.training.hospital.dao;

import by.training.hospital.dto.VisitFeedbackDTO;
import by.training.hospital.entity.VisitFeedback;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitFeedbackDAOImpl implements VisitFeedbackDAO {

    private static final Logger LOGGER = Logger.getLogger(VisitFeedbackDAOImpl.class);

    private static final String CREATE_VISIT_FEEDBACK =
            "INSERT INTO visit_feedback (visitor_mess, doctor_mess, visit_id) " +
                    "VALUES (?,?,?)";

    private static final String GET_VISIT_FEEDBACK_BY_ID =
            "SELECT visitor_mess, doctor_mess, visit_id FROM visit_feedback  WHERE id = ?";

    private static final String UPDATE_VISIT_FEEDBACK = "UPDATE visit_feedback SET visitor_mess = ?, doctor_mess = ?, " +
            "visit_id = ? WHERE id = ?";

    private static final String DELETE_VISIT_FEEDBACK_BY_ID = "DELETE FROM visit_feedback WHERE id = ?";

    private static final String GET_ALL_VISIT_FEEDBACKS = "SELECT id, visitor_mess, doctor_mess, visit_id FROM visit_feedback";

    private static final String GET_VISIT_FEEDBACK_BY_VISIT_ID =
            "SELECT visitor_mess, doctor_mess, id FROM visit_feedback WHERE visit_id = ?";

    private static final String GET_VISIT_FEEDBACKS_BY_DOCTOR_USER_ID =
            "SELECT visit_feedback.id, visit_feedback.visitor_mess, visit_feedback.doctor_mess, visit_feedback.visit_id," +
                    " user_info.first_name, user_info.last_name " +
                    "FROM visit_feedback " +
                    "JOIN visit ON visit_feedback.visit_id = visit.id " +
                    "JOIN user_account ON visit.doctor_id = user_account.id " +
                    "JOIN user_info ON visit.visitor_id = user_info.user_id " +
                    "WHERE user_account.id = ?";

    private ConnectionManager connectionManager;

    public VisitFeedbackDAOImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public long create(VisitFeedbackDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_VISIT_FEEDBACK, Statement.RETURN_GENERATED_KEYS)) {

            VisitFeedback entity = fromDTO(dto);

            int i = 0;
            preparedStatement.setString(++i, entity.getVisitorMess());
            preparedStatement.setString(++i, entity.getDoctorMess());
            preparedStatement.setLong(++i, entity.getVisitId());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            long id = 0;

            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            }

            return id;

        } catch (SQLException e) {
            LOGGER.error("Error during creating a new visit feedback in DB.", e);
            throw new DAOException("Error during creating a new visit feedback in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public VisitFeedbackDTO getById(Long id) throws DAOException, NoConcreteEntityInDatabaseException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_VISIT_FEEDBACK_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String visitorMessage = resultSet.getString("visitor_mess");
                String doctorMessage = resultSet.getString("doctor_mess");
                long visitId = resultSet.getLong("visit_id");
                VisitFeedback visitFeedback = new VisitFeedback();
                visitFeedback.setId(id);
                visitFeedback.setVisitorMess(visitorMessage);
                visitFeedback.setDoctorMess(doctorMessage);
                visitFeedback.setVisitId(visitId);

                return toDTO(visitFeedback);
            }
            throw new NoConcreteEntityInDatabaseException("Error during retrieving a visit feedback with id = " + id + ".");

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving a visit feedback from DB.", e);
            throw new DAOException("Error during retrieving a visit feedback from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(VisitFeedbackDTO dto) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_VISIT_FEEDBACK)) {

            VisitFeedback entity = fromDTO(dto);
            int i = 0;
            preparedStatement.setString(++i, entity.getVisitorMess());
            preparedStatement.setString(++i, entity.getDoctorMess());
            preparedStatement.setLong(++i, entity.getVisitId());
            preparedStatement.setLong(++i, entity.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during updating a visit feedback in DB.", e);
            throw new DAOException("Error during updating a visit feedback in DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_VISIT_FEEDBACK_BY_ID)) {

            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Error during deleting a visit feedback from DB.", e);
            throw new DAOException("Error during deleting a visit feedback from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<VisitFeedbackDTO> getAll() throws DAOException {

        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_VISIT_FEEDBACKS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<VisitFeedbackDTO> visitFeedbacks = new ArrayList<>();
            while (resultSet.next()) {
                long visitFeedbackId = resultSet.getLong("id");
                String visitorMessage = resultSet.getString("visitor_mess");
                String doctorMessage = resultSet.getString("doctor_mess");
                long visitId = resultSet.getLong("visit_id");

                VisitFeedback visitFeedback = new VisitFeedback();
                visitFeedback.setId(visitFeedbackId);
                visitFeedback.setVisitorMess(visitorMessage);
                visitFeedback.setDoctorMess(doctorMessage);
                visitFeedback.setVisitId(visitId);

                visitFeedbacks.add(toDTO(visitFeedback));
            }
            return visitFeedbacks;
        } catch (SQLException e) {
            LOGGER.error("Error during retrieving all visit feedbacks from DB.", e);
            throw new DAOException("Error during retrieving all visit feedbacks from DB.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public VisitFeedbackDTO getByVisitId(long visitId) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_VISIT_FEEDBACK_BY_VISIT_ID)) {

            preparedStatement.setLong(1, visitId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String visitorMessage = resultSet.getString("visitor_mess");
                String doctorMessage = resultSet.getString("doctor_mess");
                long id = resultSet.getLong("id");
                VisitFeedback visitFeedback = new VisitFeedback();
                visitFeedback.setId(id);
                visitFeedback.setVisitorMess(visitorMessage);
                visitFeedback.setDoctorMess(doctorMessage);
                visitFeedback.setVisitId(visitId);

                return toDTO(visitFeedback);
            }
            return null;

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving a visit feedback from DB by visitId.", e);
            throw new DAOException("Error during retrieving a visit feedback from DB by visitId.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    @Override
    public List<VisitFeedbackDTO> getByDoctorUserId(long doctorUserId) throws DAOException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_VISIT_FEEDBACKS_BY_DOCTOR_USER_ID)) {

            preparedStatement.setLong(1, doctorUserId);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<VisitFeedbackDTO> feedbacks = new ArrayList<>();
            while (resultSet.next()) {

                String visitorMessage = resultSet.getString("visit_feedback.visitor_mess");
                String doctorMessage = resultSet.getString("visit_feedback.doctor_mess");
                long id = resultSet.getLong("visit_feedback.id");
                long visitId = resultSet.getLong("visit_feedback.visit_id");
                String visitorFirstName = resultSet.getString("user_info.first_name");
                String visitorLastName = resultSet.getString("user_info.last_name");
                VisitFeedback visitFeedback = new VisitFeedback();
                visitFeedback.setId(id);
                visitFeedback.setVisitorMess(visitorMessage);
                visitFeedback.setDoctorMess(doctorMessage);
                visitFeedback.setVisitId(visitId);

                VisitFeedbackDTO visitFeedbackDTO = toDTO(visitFeedback);
                visitFeedbackDTO.setVisitorFirstName(visitorFirstName);
                visitFeedbackDTO.setVisitorLastName(visitorLastName);

                feedbacks.add(visitFeedbackDTO);
            }
            return feedbacks;

        } catch (SQLException e) {
            LOGGER.error("Error during retrieving a visit feedbacks from DB by doctorUserId.", e);
            throw new DAOException("Error during retrieving a visit feedback from DB by doctorUserId.", e);
        } finally {
            connectionManager.releaseConnection(connection);
        }
    }

    private VisitFeedback fromDTO(VisitFeedbackDTO dto) {

        VisitFeedback entity = new VisitFeedback();
        entity.setId(dto.getId());
        entity.setVisitorMess(dto.getVisitorMess());
        entity.setDoctorMess(dto.getDoctorMess());
        entity.setVisitId(dto.getVisitId());

        return entity;
    }

    private VisitFeedbackDTO toDTO(VisitFeedback entity) {

        VisitFeedbackDTO dto = new VisitFeedbackDTO();
        dto.setId(entity.getId());
        dto.setVisitorMess(entity.getVisitorMess());
        dto.setDoctorMess(entity.getDoctorMess());
        dto.setVisitId(entity.getVisitId());

        return dto;
    }
}
