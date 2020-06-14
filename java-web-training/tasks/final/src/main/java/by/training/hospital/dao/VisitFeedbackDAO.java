package by.training.hospital.dao;

import by.training.hospital.dto.VisitFeedbackDTO;

import java.util.List;

public interface VisitFeedbackDAO extends CrudDAO<Long, VisitFeedbackDTO> {
    VisitFeedbackDTO getByVisitId(long visitId) throws DAOException;

    List<VisitFeedbackDTO> getByDoctorUserId(long doctorUserId) throws DAOException;
}
