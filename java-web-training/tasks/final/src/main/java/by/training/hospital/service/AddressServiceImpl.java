package by.training.hospital.service;

import by.training.hospital.dao.AddressDAO;
import by.training.hospital.dao.DAOException;
import by.training.hospital.dao.NoConcreteEntityInDatabaseException;
import by.training.hospital.dto.AddressDTO;
import org.apache.log4j.Logger;

import java.util.List;

public class AddressServiceImpl implements AddressService {

    private static final Logger LOGGER = Logger.getLogger(AddressServiceImpl.class);

    private AddressDAO addressDAO;

    public AddressServiceImpl(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public long create(AddressDTO dto) throws ServiceException {
        try {
            return addressDAO.create(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during creating a new address in DB.", e);
            throw new ServiceException("Error during creating a new address in DB.", e);
        }
    }

    public AddressDTO getById(Long id) throws ServiceException, NoConcreteDTOException {
        try {
            return addressDAO.getById(id);
        } catch (NoConcreteEntityInDatabaseException e) {
            LOGGER.error("Error during retrieving an address with id = " + id + ".", e);
            throw new NoConcreteDTOException("Error during retrieving an address with id = " + id + ".", e);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving an address from DB.", e);
            throw new ServiceException("Error during retrieving an address from DB.", e);
        }
    }

    public boolean update(AddressDTO dto) throws ServiceException {
        try {
            return addressDAO.update(dto);
        } catch (DAOException e) {
            LOGGER.error("Error during updating an address in DB.", e);
            throw new ServiceException("Error during updating an address in DB.", e);
        }
    }

    public boolean delete(Long id) throws ServiceException {
        try {
            return addressDAO.delete(id);
        } catch (DAOException e) {
            LOGGER.error("Error during deleting an address from DB.", e);
            throw new ServiceException("Error during deleting an address from DB.", e);
        }
    }

    public List<AddressDTO> getAll() throws ServiceException {
        try {
            return addressDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving all addresses from DB.", e);
            throw new ServiceException("Error during retrieving all addresses from DB.", e);
        }
    }

    public AddressDTO getByUserId(long userId) throws ServiceException {
        try {
            return addressDAO.getByUserId(userId);
        } catch (DAOException e) {
            LOGGER.error("Error during retrieving an address from DB by userId.", e);
            throw new ServiceException("Error during retrieving an address from DB by userId.", e);
        }
    }
}
