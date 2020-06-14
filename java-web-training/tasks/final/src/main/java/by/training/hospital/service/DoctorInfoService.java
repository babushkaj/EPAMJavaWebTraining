package by.training.hospital.service;

import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;

import java.util.List;

public interface DoctorInfoService {
    long create(DoctorInfoDTO dto) throws ServiceException;

    DoctorInfoDTO getById(Long id) throws ServiceException, NoConcreteDTOException;

    boolean update(DoctorInfoDTO dto) throws ServiceException;

    boolean delete(Long id) throws ServiceException;

    List<DoctorInfoDTO> getAll() throws ServiceException;

    DoctorInfoDTO getDoctorInfoByUserId(long userId) throws ServiceException;

    List<UserDTO> getAllUnblockedDoctorsWithInfo() throws ServiceException;

    List<UserDTO> getAllUnblockedDoctorsWithInfoFromTo(Long from, long count) throws ServiceException;

    Long getUnblockedDoctorsCount() throws ServiceException;
}
