package by.training.hospital.service;

import by.training.hospital.dao.AddressDAO;
import by.training.hospital.dao.DAOException;
import by.training.hospital.dao.NoConcreteEntityInDatabaseException;
import by.training.hospital.dto.AddressDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class AddressServiceImplTest {
    private AddressService addressService;

    @Before
    public void initPool() throws DAOException, NoConcreteEntityInDatabaseException {
        AddressDTO addressDTO1 = new AddressDTO();
        addressDTO1.setId(1L);
        addressDTO1.setUserId(1L);
        addressDTO1.setRegion("Минская");
        addressDTO1.setCity("Минск");
        addressDTO1.setStreet("Ленина");
        addressDTO1.setHouse("1");
        addressDTO1.setApartment("1");
        AddressDTO addressDTO2 = new AddressDTO();
        addressDTO2.setId(2L);
        addressDTO2.setUserId(2L);
        addressDTO2.setRegion("Минская");
        addressDTO2.setCity("Минск");
        addressDTO2.setStreet("Ленина");
        addressDTO2.setHouse("2");
        addressDTO2.setApartment("2");
        List<AddressDTO> addressList = new ArrayList<>();
        addressList.add(addressDTO1);
        addressList.add(addressDTO2);

        AddressDAO mockAddressDAO = Mockito.mock(AddressDAO.class);
        Mockito.when(mockAddressDAO.create(Mockito.any())).thenReturn(1L);
        Mockito.when(mockAddressDAO.getById(Mockito.anyLong())).thenReturn(addressDTO1);
        Mockito.when(mockAddressDAO.update(Mockito.any())).thenReturn(true);
        Mockito.when(mockAddressDAO.delete(Mockito.anyLong())).thenReturn(true);
        Mockito.when(mockAddressDAO.getAll()).thenReturn(addressList);
        Mockito.when(mockAddressDAO.getByUserId(1L)).thenReturn(addressDTO1);

        addressService = new AddressServiceImpl(mockAddressDAO);
    }

    @Test
    public void shouldReturnNewAddressId() throws ServiceException {
        long newAddressId = addressService.create(new AddressDTO());
        Assert.assertEquals(1L, newAddressId);
    }

    @Test
    public void shouldReturnAddressById() throws ServiceException, NoConcreteDTOException {
        AddressDTO addressDTO = addressService.getById(1L);
        Assert.assertEquals(new Long(1), addressDTO.getId());
        Assert.assertEquals("Минская", addressDTO.getRegion());
    }

    @Test
    public void shouldUpdateAddress() throws ServiceException {
        boolean updateResult = addressService.update(new AddressDTO());
        Assert.assertTrue(updateResult);
    }

    @Test
    public void shouldDeleteAddress() throws ServiceException {
        boolean deleteResult = addressService.delete(1L);
        Assert.assertTrue(deleteResult);
    }

    @Test
    public void shouldReturnAddressList() throws ServiceException {
        List<AddressDTO> addressList = addressService.getAll();
        Assert.assertEquals(2, addressList.size());
    }

    @Test
    public void shouldReturnAddressByUserId() throws ServiceException {
        AddressDTO addressDTO = addressService.getByUserId(1L);
        Assert.assertEquals(new Long(1), addressDTO.getId());
    }

}
