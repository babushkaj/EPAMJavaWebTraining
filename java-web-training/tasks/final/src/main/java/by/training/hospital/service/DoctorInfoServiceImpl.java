package by.training.hospital.service;

import by.training.hospital.dao.DAOException;
import by.training.hospital.dao.DoctorInfoDAO;
import by.training.hospital.dao.NoConcreteEntityInDatabaseException;
import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;
import org.apache.log4j.Logger;

import java.util.List;

public class DoctorInfoServiceImpl implements DoctorInfoService {

    private static final Logger LOGGER = Logger.getLogger(DoctorInfoServiceImpl.class);

    private DoctorInfoDAO doctorInfoDAO;

    public DoctorInfoServiceImpl(DoctorInfoDAO doctorInfoDAO) {
        this.doctorInfoDAO = doctorInfoDAO;
    }

    public long create(DoctorInfoDTO dto) throws ServiceException {
        try {
            return doctorInfoDAO.create(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during creating a new doctor info in DB.", e);
            throw new ServiceException("Error during creating a new doctor info in DB.", e);
        }
    }

    public DoctorInfoDTO getById(Long id) throws ServiceException, NoConcreteDTOException {
        try {
            return doctorInfoDAO.getById(id);
        } catch (NoConcreteEntityInDatabaseException e) {
            LOGGER.error("Error during retrieving an doctor info with id = " + id + ".", e);
            throw new NoConcreteDTOException("Error during retrieving an doctor info with id = " + id + ".", e);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving doctor info from DB.", e);
            throw new ServiceException("Error during retrieving doctor info from DB.", e);
        }
    }

    public boolean update(DoctorInfoDTO dto) throws ServiceException {
        try {
            return doctorInfoDAO.update(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during updating a doctor info in DB.", e);
            throw new ServiceException("Error during updating a doctor info in DB.", e);
        }
    }

    public boolean delete(Long id) throws ServiceException {
        try {
            return doctorInfoDAO.delete(id);
        } catch (DAOException e) {
            LOGGER.error("Error during deleting a doctor info from DB.", e);
            throw new ServiceException("Error during deleting a doctor info from DB.", e);
        }
    }

    public List<DoctorInfoDTO> getAll() throws ServiceException {
        try {
            return doctorInfoDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving all doctor info from DB.", e);
            throw new ServiceException("Error during retrieving all doctor info from DB.", e);
        }
    }

    public DoctorInfoDTO getDoctorInfoByUserId(long userId) throws ServiceException {
        try {
            return doctorInfoDAO.getDoctorInfoByUserId(userId);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving doctor info from DB by userId.", e);
            throw new ServiceException("Error during retrieving doctor info from DB by userId.", e);
        }
    }

    public List<UserDTO> getAllUnblockedDoctorsWithInfo() throws ServiceException {
        try {
            return doctorInfoDAO.getAllUnblockedDoctorsWithInfo();
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving users with doctor info from DB.", e);
            throw new ServiceException("Error during retrieving users with doctor info from DB.", e);
        }
    }

    public List<UserDTO> getAllUnblockedDoctorsWithInfoFromTo(Long from, long count) throws ServiceException {
        try {
            return doctorInfoDAO.getAllUnblockedDoctorsWithInfoFromTo(from, count);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving users with doctor info from DB.", e);
            throw new ServiceException("Error during retrieving users with doctor info from DB.", e);
        }
    }

    public Long getUnblockedDoctorsCount() throws ServiceException {
        try {
            return doctorInfoDAO.getUnblockedDoctorsCount();
        } catch (DAOException e) {
            LOGGER.error("Error during getting count of unblocked doctors from DB.", e);
            throw new ServiceException("Error during getting count of unblocked doctors from DB.", e);
        }
    }

}
