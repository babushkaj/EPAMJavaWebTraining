package by.training.hospital.service;

import by.training.hospital.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    long create(AddressDTO dto) throws ServiceException;

    AddressDTO getById(Long id) throws ServiceException, NoConcreteDTOException;

    boolean update(AddressDTO dto) throws ServiceException;

    boolean delete(Long id) throws ServiceException;

    List<AddressDTO> getAll() throws ServiceException;

    AddressDTO getByUserId(long userId) throws ServiceException;
}
