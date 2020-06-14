package by.training.hospital.service;

import by.training.hospital.dao.DAOException;
import by.training.hospital.dao.NoConcreteEntityInDatabaseException;
import by.training.hospital.dao.VisitFeedbackDAO;
import by.training.hospital.dto.VisitFeedbackDTO;
import org.apache.log4j.Logger;

import java.util.List;

public class VisitFeedbackServiceImpl implements VisitFeedbackService {

    private static final Logger LOGGER = Logger.getLogger(VisitFeedbackServiceImpl.class);

    private VisitFeedbackDAO visitFeedbackDAO;

    public VisitFeedbackServiceImpl(VisitFeedbackDAO visitFeedbackDAO) {
        this.visitFeedbackDAO = visitFeedbackDAO;
    }

    public long create(VisitFeedbackDTO dto) throws ServiceException {
        try {
            return visitFeedbackDAO.create(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during creating a new visit feedback in DB.", e);
            throw new ServiceException("Error during creating a new visit feedback in DB.", e);
        }
    }

    public VisitFeedbackDTO getById(Long id) throws ServiceException, NoConcreteDTOException {
        try {
            return visitFeedbackDAO.getById(id);
        } catch (NoConcreteEntityInDatabaseException e) {
            LOGGER.error("Error during retrieving a visit feedback with id = " + id + ".", e);
            throw new NoConcreteDTOException("Error during retrieving a visit feedback with id = " + id + ".", e);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving a visit feedback from DB.", e);
            throw new ServiceException("Error during retrieving a visit feedback from DB.", e);
        }
    }

    public boolean update(VisitFeedbackDTO dto) throws ServiceException {
        try {
            return visitFeedbackDAO.update(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during updating a visit feedback in DB.", e);
            throw new ServiceException("Error during updating a visit feedback in DB.", e);
        }
    }

    public boolean delete(Long id) throws ServiceException {
        try {
            return visitFeedbackDAO.delete(id);
        } catch (DAOException e) {
            LOGGER.error("Error during deleting a visit feedback from DB.", e);
            throw new ServiceException("Error during deleting a visit feedback from DB.", e);
        }
    }

    public List<VisitFeedbackDTO> getAll() throws ServiceException {
        try {
            return visitFeedbackDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving all visit feedbacks from DB.", e);
            throw new ServiceException("Error during retrieving all visit feedbacks from DB.", e);
        }
    }

    public VisitFeedbackDTO getByVisitId(long visitId) throws ServiceException {
        try {
            return visitFeedbackDAO.getByVisitId(visitId);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving a visit feedback from DB by visitId.", e);
            throw new ServiceException("Error during retrieving a visit feedback from DB by visitId.", e);
        }
    }

    public List<VisitFeedbackDTO> getByDoctorUserId(long doctorUserId) throws ServiceException {
        try {
            return visitFeedbackDAO.getByDoctorUserId(doctorUserId);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving a visit feedback from DB by doctorUserId.", e);
            throw new ServiceException("Error during retrieving a visit feedback from DB by doctorUserId.", e);
        }
    }

}
