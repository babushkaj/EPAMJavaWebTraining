package by.training.hospital.service;

import by.training.hospital.dao.*;
import by.training.hospital.dto.AddressDTO;
import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.entity.Role;
import org.apache.log4j.Logger;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    private TransactionManager transactionManager;
    private UserDAO userDAO;
    private UserInfoDAO userInfoDAO;
    private AddressDAO addressDAO;
    private DoctorInfoDAO doctorInfoDAO;

    public UserServiceImpl(UserDAO userDAO, UserInfoDAO userInfoDAO, AddressDAO addressDAO,
                           DoctorInfoDAO doctorInfoDAO, TransactionManager transactionManager) {
        this.userDAO = userDAO;
        this.userInfoDAO = userInfoDAO;
        this.addressDAO = addressDAO;
        this.doctorInfoDAO = doctorInfoDAO;
        this.transactionManager = transactionManager;
    }

    @Override
    public long create(UserDTO dto) throws ServiceException {
        try {
            return userDAO.create(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during creating a new user in DB.", e);
            throw new ServiceException("Error during creating a new user in DB.", e);
        }
    }

    @Override
    public UserDTO getById(Long id) throws ServiceException, NoConcreteDTOException {
        try {
            return userDAO.getById(id);
        } catch (NoConcreteEntityInDatabaseException e) {
            LOGGER.error("Error during retrieving an user with id = " + id + ".", e);
            throw new NoConcreteDTOException("Error during retrieving an user with id = " + id + ".", e);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving a user from DB.", e);
            throw new ServiceException("Error during retrieving a user from DB.", e);
        }
    }

    @Override
    public boolean update(UserDTO dto) throws ServiceException {
        try {
            return userDAO.update(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during updating a user in DB.", e);
            throw new ServiceException("Error during updating a user in DB.", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        try {
            return userDAO.delete(id);
        } catch (DAOException e) {
            LOGGER.error("Error during deleting a user from DB.", e);
            throw new ServiceException("Error during deleting a user from DB.", e);
        }
    }

    @Override
    public List<UserDTO> getAll() throws ServiceException {
        try {
            return userDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving all users from DB.", e);
            throw new ServiceException("Error during retrieving all users from DB.", e);
        }
    }

    @Override
    public UserDTO getUserByLogin(String login) throws ServiceException {
        try {
            return userDAO.getUserByLogin(login);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving a user from DB by login.", e);
            throw new ServiceException("Error during retrieving a user from DB by login.", e);
        }
    }

    @Override
    public boolean isLoginUnique(String login) throws ServiceException {
        try {
            return userDAO.isLoginUnique(login);
        } catch (DAOException e) {
            LOGGER.error("Error during checking a user's login in DB.", e);
            throw new ServiceException("Error during checking a user's login in DB.", e);
        }
    }

    @Override
    public void saveNewUser(UserDTO user) throws ServiceException {
        try {
            transactionManager.beginTransaction();
            long userId = userDAO.create(user);
            UserInfoDTO userInfoDTO = user.getUserInfo();
            userInfoDTO.setUserId(userId);
            userInfoDAO.create(userInfoDTO);
            AddressDTO addressDTO = user.getAddress();
            addressDTO.setUserId(userId);
            addressDAO.create(addressDTO);
            if (user.getRole() == Role.DOCTOR) {
                DoctorInfoDTO doctorInfoDTO = user.getDoctorInfo();
                doctorInfoDTO.setUserId(userId);
                Long doctorInfoId = doctorInfoDAO.create(doctorInfoDTO);
                doctorInfoDAO.createWorkingDaysForDoctor(doctorInfoId, doctorInfoDTO.getWorkingDays());
            }
            transactionManager.commitTransaction();
        } catch (DAOException e) {
            transactionManager.rollback();
            throw new ServiceException("Error during saving a new user in DB.", e);
        }
    }

    @Override
    public void updateUserWithInfo(UserDTO user) throws ServiceException {
        try {
            transactionManager.beginTransaction();
            userDAO.update(user);
            UserInfoDTO userInfoDTO = user.getUserInfo();
            userInfoDAO.update(userInfoDTO);
            AddressDTO addressDTO = user.getAddress();
            addressDAO.update(addressDTO);
            if (user.getRole() == Role.DOCTOR) {
                DoctorInfoDTO doctorInfoDTO = user.getDoctorInfo();
                doctorInfoDAO.update(doctorInfoDTO);
                doctorInfoDAO.deleteWorkingDaysByDoctorInfoId(doctorInfoDTO.getId());
                doctorInfoDAO.createWorkingDaysForDoctor(doctorInfoDTO.getId(), doctorInfoDTO.getWorkingDays());
            }
            transactionManager.commitTransaction();
        } catch (DAOException e) {
            transactionManager.rollback();
            throw new ServiceException("Error during updating a user in DB.", e);
        }
    }

    @Override
    public List<UserDTO> getAllUsersWithUserInfoByRoleFromTo(Long from, Long count, Role role) throws ServiceException {
        try {
            return userDAO.getAllUsersWithUserInfoByRoleFromTo(from, count, role);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving users with info by role from DB.", e);
            throw new ServiceException("Error during retrieving users with info by role from DB.", e);
        }
    }

    @Override
    public List<UserDTO> getAllUsersWithUserInfoFromTo(Long from, Long count) throws ServiceException {
        try {
            return userDAO.getAllUsersWithUserInfoFromTo(from, count);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving users with info from DB.", e);
            throw new ServiceException("Error during retrieving users with info from DB.", e);
        }
    }

    @Override
    public Long getUserCountByRole(Role role) throws ServiceException {
        try {
            return userDAO.getUserCountByRole(role);
        } catch (DAOException e) {
            LOGGER.error("Error during getting count of users by role from DB.", e);
            throw new ServiceException("Error during getting count of users by role from DB.", e);
        }
    }

    @Override
    public Long getAllUsersCount() throws ServiceException {
        try {
            return userDAO.getAllUsersCount();
        } catch (DAOException e) {
            LOGGER.error("Error during getting count of all users except ADMIN from DB.", e);
            throw new ServiceException("Error during getting count of all users except ADMIN from DB.", e);
        }
    }
}
