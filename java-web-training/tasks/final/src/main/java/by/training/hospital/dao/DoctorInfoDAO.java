package by.training.hospital.dao;

import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.dto.UserDTO;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface DoctorInfoDAO extends CrudDAO<Long, DoctorInfoDTO> {
    DoctorInfoDTO getDoctorInfoByUserId(long userId) throws DAOException;

    boolean createWorkingDaysForDoctor(Long doctorInfoId, Set<DayOfWeek> days) throws DAOException;

    boolean deleteWorkingDaysByDoctorInfoId(Long doctorInfoId) throws DAOException;

    List<UserDTO> getAllUnblockedDoctorsWithInfo() throws DAOException;

    List<UserDTO> getAllUnblockedDoctorsWithInfoFromTo(Long from, Long count) throws DAOException;

    Long getUnblockedDoctorsCount() throws DAOException;
}
