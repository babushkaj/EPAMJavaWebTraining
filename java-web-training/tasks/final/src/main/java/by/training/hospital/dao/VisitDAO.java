package by.training.hospital.dao;

import by.training.hospital.dto.VisitDTO;

import java.util.List;

public interface VisitDAO extends CrudDAO<Long, VisitDTO> {
    List<VisitDTO> getVisitsByDoctorId(long doctorId) throws DAOException;

    List<VisitDTO> getVisitsByVisitorId(long userId) throws DAOException;
}
