package by.training.hospital.service;

import by.training.hospital.dto.UserInfoDTO;

import java.util.List;

public interface UserInfoService {
    long create(UserInfoDTO dto) throws ServiceException;

    UserInfoDTO getById(Long id) throws ServiceException, NoConcreteDTOException;

    boolean update(UserInfoDTO dto) throws ServiceException;

    boolean delete(Long id) throws ServiceException;

    List<UserInfoDTO> getAll() throws ServiceException;

    UserInfoDTO getUserInfoByUserId(long userId) throws ServiceException;

    boolean isEmailUnique(String email) throws ServiceException;
}
