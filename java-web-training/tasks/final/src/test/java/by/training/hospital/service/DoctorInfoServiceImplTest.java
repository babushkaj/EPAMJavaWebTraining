package by.training.hospital.service;

import by.training.hospital.dao.DAOException;
import by.training.hospital.dao.DoctorInfoDAO;
import by.training.hospital.dao.NoConcreteEntityInDatabaseException;
import by.training.hospital.dto.DoctorInfoDTO;
import by.training.hospital.entity.Specialization;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RunWith(JUnit4.class)
public class DoctorInfoServiceImplTest {

    private DoctorInfoService doctorInfoService;

    @Before
    public void initPool() throws DAOException, NoConcreteEntityInDatabaseException {
        DoctorInfoDTO doctorInfoDTO1 = new DoctorInfoDTO();
        doctorInfoDTO1.setId(1L);
        doctorInfoDTO1.setSpec(Specialization.OCULIST);
        DayOfWeek[] workingDays1 = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY};
        doctorInfoDTO1.setWorkingDays(new HashSet<>(Arrays.asList(workingDays1)));
        doctorInfoDTO1.setDescription("Test doctor description1");
        doctorInfoDTO1.setUserId(1L);
        DoctorInfoDTO doctorInfoDTO2 = new DoctorInfoDTO();
        doctorInfoDTO2.setId(2L);
        doctorInfoDTO2.setSpec(Specialization.OTOLARYNGOLOGIST);
        DayOfWeek[] workingDays2 = {DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
        doctorInfoDTO2.setWorkingDays(new HashSet<>(Arrays.asList(workingDays2)));
        doctorInfoDTO2.setDescription("Test doctor description2");
        doctorInfoDTO2.setUserId(2L);
        List<DoctorInfoDTO> addressList = new ArrayList<>();
        addressList.add(doctorInfoDTO1);
        addressList.add(doctorInfoDTO2);

        DoctorInfoDAO mockDoctorInfoDAO = Mockito.mock(DoctorInfoDAO.class);
        Mockito.when(mockDoctorInfoDAO.create(Mockito.any())).thenReturn(3L);
        Mockito.when(mockDoctorInfoDAO.getById(1L)).thenReturn(doctorInfoDTO1);
        Mockito.when(mockDoctorInfoDAO.update(Mockito.any())).thenReturn(true);
        Mockito.when(mockDoctorInfoDAO.delete(Mockito.anyLong())).thenReturn(true);
        Mockito.when(mockDoctorInfoDAO.getAll()).thenReturn(addressList);
        Mockito.when(mockDoctorInfoDAO.getUnblockedDoctorsCount()).thenReturn(2L);

        doctorInfoService = new DoctorInfoServiceImpl(mockDoctorInfoDAO);
    }

    @Test
    public void shouldReturnNewDoctorInfoId() throws ServiceException {
        long newDoctorInfoId = doctorInfoService.create(new DoctorInfoDTO());
        Assert.assertEquals(3L, newDoctorInfoId);
    }

    @Test
    public void shouldReturnDoctorInfoById() throws ServiceException, NoConcreteDTOException {
        DoctorInfoDTO doctorInfoDTO = doctorInfoService.getById(1L);
        Assert.assertEquals(new Long(1), doctorInfoDTO.getId());
        Assert.assertEquals("Test doctor description1", doctorInfoDTO.getDescription());
    }

    @Test
    public void shouldUpdateDoctorInfo() throws ServiceException {
        boolean updateResult = doctorInfoService.update(new DoctorInfoDTO());
        Assert.assertTrue(updateResult);
    }

    @Test
    public void shouldDeleteDoctorInfo() throws ServiceException {
        boolean deleteResult = doctorInfoService.delete(1L);
        Assert.assertTrue(deleteResult);
    }

    @Test
    public void shouldReturnDoctorInfo() throws ServiceException {
        List<DoctorInfoDTO> addressList = doctorInfoService.getAll();
        Assert.assertEquals(2, addressList.size());
    }

    @Test
    public void shouldReturnUnblockedDoctorsCount() throws ServiceException {
       long count = doctorInfoService.getUnblockedDoctorsCount();
        Assert.assertEquals(2, count);
    }

}
