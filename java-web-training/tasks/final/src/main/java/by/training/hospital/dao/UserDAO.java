package by.training.hospital.dao;

import by.training.hospital.dto.UserDTO;
import by.training.hospital.entity.Role;

import java.util.List;

public interface UserDAO extends CrudDAO<Long, UserDTO> {
    UserDTO getUserByLogin(String login) throws DAOException;

    boolean isLoginUnique(String login) throws DAOException;

    List<UserDTO> getAllUsersWithUserInfoByRoleFromTo(Long from, Long count, Role role) throws DAOException;

    List<UserDTO> getAllUsersWithUserInfoFromTo(Long from, Long count) throws DAOException;

    Long getUserCountByRole(Role role) throws DAOException;

    Long getAllUsersCount() throws DAOException;

}
