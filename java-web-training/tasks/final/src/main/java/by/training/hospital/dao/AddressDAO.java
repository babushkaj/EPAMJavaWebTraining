package by.training.hospital.dao;

import by.training.hospital.dto.AddressDTO;

public interface AddressDAO extends CrudDAO<Long, AddressDTO> {
    AddressDTO getByUserId(long userId) throws DAOException;
}
