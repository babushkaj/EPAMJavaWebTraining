package by.training.hospital.service;

import by.training.hospital.dto.UserDTO;
import by.training.hospital.entity.Role;

import java.util.List;

public interface UserService {

    long create(UserDTO dto) throws ServiceException;

    UserDTO getById(Long id) throws ServiceException, NoConcreteDTOException;

    boolean update(UserDTO dto) throws ServiceException;

    boolean delete(Long id) throws ServiceException;

    List<UserDTO> getAll() throws ServiceException;

    UserDTO getUserByLogin(String login) throws ServiceException;

    boolean isLoginUnique(String login) throws ServiceException;

    void saveNewUser(UserDTO user) throws ServiceException;

    void updateUserWithInfo(UserDTO user) throws ServiceException;

    List<UserDTO> getAllUsersWithUserInfoByRoleFromTo(Long from, Long count, Role role1) throws ServiceException;

    List<UserDTO> getAllUsersWithUserInfoFromTo(Long from, Long count) throws ServiceException;

    Long getUserCountByRole(Role role) throws ServiceException;

    Long getAllUsersCount() throws ServiceException;
}
