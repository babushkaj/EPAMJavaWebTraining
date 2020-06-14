package by.training.hospital.service;

import by.training.hospital.dao.DAOException;
import by.training.hospital.dao.NoConcreteEntityInDatabaseException;
import by.training.hospital.dao.VisitDAO;
import by.training.hospital.dto.VisitDTO;
import org.apache.log4j.Logger;

import java.util.List;

public class VisitServiceImpl implements VisitService {

    private static final Logger LOGGER = Logger.getLogger(VisitServiceImpl.class);

    private VisitDAO visitDAO;

    public VisitServiceImpl(VisitDAO visitDAO) {
        this.visitDAO = visitDAO;
    }

    public long create(VisitDTO dto) throws ServiceException {
        try {
            return visitDAO.create(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during creating a new visit in DB.", e);
            throw new ServiceException("Error during creating a new visit in DB.", e);
        }
    }

    public VisitDTO getById(Long id) throws ServiceException, NoConcreteDTOException {
        try {
            return visitDAO.getById(id);
        } catch (NoConcreteEntityInDatabaseException e) {
            LOGGER.error("Error during retrieving a visit with id = " + id + ".", e);
            throw new NoConcreteDTOException("Error during retrieving a visit with id = " + id + ".", e);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving a visit from DB.", e);
            throw new ServiceException("Error during retrieving a visit from DB.", e);
        }
    }

    public boolean update(VisitDTO dto) throws ServiceException {
        try {
            return visitDAO.update(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during updating a visit in DB.", e);
            throw new ServiceException("Error during updating a visit in DB.", e);
        }
    }

    public boolean delete(Long id) throws ServiceException {
        try {
            return visitDAO.delete(id);
        } catch (DAOException e) {
            LOGGER.error("Error during deleting a visit from DB.", e);
            throw new ServiceException("Error during deleting a visit from DB.", e);
        }
    }

    public List<VisitDTO> getAll() throws ServiceException {
        try {
            return visitDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving all visits from DB.", e);
            throw new ServiceException("Error during retrieving all visits from DB.", e);
        }
    }

    public List<VisitDTO> getVisitByDoctorId(long doctorId) throws ServiceException {
        try {
            return visitDAO.getVisitsByDoctorId(doctorId);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving visits from DB by doctorId.", e);
            throw new ServiceException("Error during retrieving visits from DB by doctorId.", e);
        }
    }

    public List<VisitDTO> getVisitByVisitorId(long visitorId) throws ServiceException {
        try {
            return visitDAO.getVisitsByVisitorId(visitorId);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving visits from DB by visitorId.", e);
            throw new ServiceException("Error during retrieving a visit from DB by visitorId.", e);
        }
    }
}
