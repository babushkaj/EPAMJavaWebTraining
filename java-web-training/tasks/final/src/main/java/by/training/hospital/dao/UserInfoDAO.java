package by.training.hospital.dao;

import by.training.hospital.dto.UserInfoDTO;

public interface UserInfoDAO extends CrudDAO<Long, UserInfoDTO> {
    UserInfoDTO getUserInfoByUserId(long userId) throws DAOException;

    boolean isEmailUnique(String email) throws DAOException;
}
