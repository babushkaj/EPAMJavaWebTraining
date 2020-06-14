package by.training.hospital.service;

import by.training.hospital.dao.DAOException;
import by.training.hospital.dao.NoConcreteEntityInDatabaseException;
import by.training.hospital.dao.UserInfoDAO;
import by.training.hospital.dto.UserInfoDTO;
import org.apache.log4j.Logger;

import java.util.List;

public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger LOGGER = Logger.getLogger(UserInfoServiceImpl.class);

    private UserInfoDAO userInfoDAO;

    public UserInfoServiceImpl(UserInfoDAO userInfoDAO) {
        this.userInfoDAO = userInfoDAO;
    }

    public long create(UserInfoDTO dto) throws ServiceException {
        try {
            return userInfoDAO.create(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during creating a new user info in DB.", e);
            throw new ServiceException("Error during creating a new user info in DB.", e);
        }
    }

    public UserInfoDTO getById(Long id) throws ServiceException, NoConcreteDTOException {
        try {
            return userInfoDAO.getById(id);
        } catch (NoConcreteEntityInDatabaseException e) {
            LOGGER.error("Error during retrieving an user info with id = " + id + ".", e);
            throw new NoConcreteDTOException("Error during retrieving an user info with id = " + id + ".", e);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving an user info from DB.", e);
            throw new ServiceException("Error during retrieving an user info from DB.", e);
        }
    }

    public boolean update(UserInfoDTO dto) throws ServiceException {
        try {
            return userInfoDAO.update(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during updating an user info in DB.", e);
            throw new ServiceException("Error during updating an user info in DB.", e);
        }
    }

    public boolean delete(Long id) throws ServiceException {
        try {
            return userInfoDAO.delete(id);
        } catch (DAOException e) {
            LOGGER.error("Error during deleting an user info from DB.", e);
            throw new ServiceException("Error during deleting an user info from DB.", e);
        }
    }

    public List<UserInfoDTO> getAll() throws ServiceException {
        try {
            return userInfoDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving all user info from DB.", e);
            throw new ServiceException("Error during retrieving all user info from DB.", e);
        }
    }

    public UserInfoDTO getUserInfoByUserId(long userId) throws ServiceException {
        try {
            return userInfoDAO.getUserInfoByUserId(userId);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving a user info from DB by userId.", e);
            throw new ServiceException("Error during retrieving a user info from DB by userId.", e);
        }
    }

    public boolean isEmailUnique(String email) throws ServiceException {
        try {
            return userInfoDAO.isEmailUnique(email);
        } catch (DAOException e) {
            LOGGER.error("Error during checking a user's email in DB.", e);
            throw new ServiceException("Error during checking a user's email in DB.", e);
        }
    }
}
