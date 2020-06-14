package by.training.hospital.service;

import by.training.hospital.dao.*;
import by.training.hospital.dto.AddressDTO;
import by.training.hospital.dto.UserDTO;
import by.training.hospital.dto.UserInfoDTO;
import by.training.hospital.entity.Role;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class UserServiceImplTest {

    private UserService userService;
    private TransactionManager mockTransactionManager;

    @Before
    public void initPool() throws DAOException, NoConcreteEntityInDatabaseException {
        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setId(1L);
        expectedUserDTO.setLogin("testUser");
        expectedUserDTO.setPassword("testUser");
        expectedUserDTO.setRole(Role.VISITOR);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(1L);
        userInfoDTO.setId(1L);
        userInfoDTO.setEmail("useremail@mail.ru");
        userInfoDTO.setPhone("44-1234567");
        userInfoDTO.setLastName("lastName");
        userInfoDTO.setFirstName("firstName");
        expectedUserDTO.setUserInfo(userInfoDTO);
        List<UserDTO> userList = new ArrayList<>();
        userList.add(expectedUserDTO);
        UserDAO mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.create(Mockito.any())).thenReturn(1L);
        Mockito.when(mockUserDAO.getById(Mockito.anyLong())).thenReturn(expectedUserDTO);
        Mockito.when(mockUserDAO.update(Mockito.any())).thenReturn(true);
        Mockito.when(mockUserDAO.delete(Mockito.anyLong())).thenReturn(true);
        Mockito.when(mockUserDAO.getAll()).thenReturn(userList);
        Mockito.when(mockUserDAO.getUserByLogin("testUser")).thenReturn(expectedUserDTO);
        Mockito.when(mockUserDAO.getUserByLogin("unknownUser")).thenThrow(new DAOException());
        Mockito.when(mockUserDAO.isLoginUnique(Mockito.anyString())).thenReturn(true);

        UserInfoDAO mockUserINfoDAO = Mockito.mock(UserInfoDAO.class);
        Mockito.when(mockUserINfoDAO.create(Mockito.any())).thenReturn(1L);

        AddressDAO mockAddressDAO = Mockito.mock(AddressDAO.class);
        Mockito.when(mockAddressDAO.create(Mockito.any())).thenReturn(1L);

        DoctorInfoDAO mockDoctorInfoDAO = Mockito.mock(DoctorInfoDAO.class);

        mockTransactionManager = Mockito.mock(TransactionManager.class);
        userService = new UserServiceImpl(mockUserDAO, mockUserINfoDAO, mockAddressDAO, mockDoctorInfoDAO, mockTransactionManager);

    }

    @Test
    public void shouldReturnNewUserId() throws ServiceException {
        long newUserId = userService.create(new UserDTO());
        Assert.assertEquals(1L, newUserId);
    }

    @Test
    public void shouldReturnUserById() throws ServiceException, NoConcreteDTOException {
        UserDTO userDTO = userService.getById(1L);
        Assert.assertEquals(new Long(1), userDTO.getId());
        Assert.assertEquals("testUser", userDTO.getLogin());
    }

    @Test
    public void shouldUpdateUser() throws ServiceException {
        boolean updateResult = userService.update(new UserDTO());
        Assert.assertTrue(updateResult);
    }

    @Test
    public void shouldDeleteUser() throws ServiceException {
        boolean deleteResult = userService.delete(1L);
        Assert.assertTrue(deleteResult);
    }

    @Test
    public void shouldReturnUserList() throws ServiceException {
        List<UserDTO> users = userService.getAll();
        Assert.assertEquals(1, users.size());
    }

    @Test
    public void shouldReturnUserByLogin() throws ServiceException {
        UserDTO user = userService.getUserByLogin("testUser");
        Assert.assertEquals(new Long(1L), user.getId());
    }

    @Test (expected = ServiceException.class)
    public void shouldThrowServiceException() throws ServiceException {
        userService.getUserByLogin("unknownUser");
    }

    @Test
    public void shouldSaveNewUserWithInfoInTransaction() throws ServiceException {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.VISITOR);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userDTO.setUserInfo(userInfoDTO);
        AddressDTO addressDTO = new AddressDTO();
        userDTO.setAddress(addressDTO);

        userService.saveNewUser(userDTO);

        Mockito.verify(mockTransactionManager, Mockito.times(1)).beginTransaction();
        Mockito.verify(mockTransactionManager, Mockito.times(1)).commitTransaction();
    }

    @Test
    public void shouldUpdateNewUserWithInfoInTransaction() throws ServiceException {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.VISITOR);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userDTO.setUserInfo(userInfoDTO);
        AddressDTO addressDTO = new AddressDTO();
        userDTO.setAddress(addressDTO);

        userService.updateUserWithInfo(userDTO);

        Mockito.verify(mockTransactionManager, Mockito.times(1)).beginTransaction();
        Mockito.verify(mockTransactionManager, Mockito.times(1)).commitTransaction();
    }

}
