package by.training.hospital.service;

import by.training.hospital.dto.VisitFeedbackDTO;

import java.util.List;

public interface VisitFeedbackService {
    long create(VisitFeedbackDTO dto) throws ServiceException;

    VisitFeedbackDTO getById(Long id) throws ServiceException, NoConcreteDTOException;

    boolean update(VisitFeedbackDTO dto) throws ServiceException;

    boolean delete(Long id) throws ServiceException;

    List<VisitFeedbackDTO> getAll() throws ServiceException;

    VisitFeedbackDTO getByVisitId(long visitId) throws ServiceException;

    List<VisitFeedbackDTO> getByDoctorUserId(long doctorUserId) throws ServiceException;
}
