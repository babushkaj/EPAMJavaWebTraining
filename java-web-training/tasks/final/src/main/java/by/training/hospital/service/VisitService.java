package by.training.hospital.service;

import by.training.hospital.dto.VisitDTO;

import java.util.List;

public interface VisitService {
    long create(VisitDTO dto) throws ServiceException;

    VisitDTO getById(Long id) throws ServiceException, NoConcreteDTOException;

    boolean update(VisitDTO dto) throws ServiceException;

    boolean delete(Long id) throws ServiceException;

    List<VisitDTO> getAll() throws ServiceException;

    List<VisitDTO> getVisitByDoctorId(long doctorId) throws ServiceException;

    List<VisitDTO> getVisitByVisitorId(long visitorId) throws ServiceException;
}
