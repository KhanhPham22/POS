package service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Dashboard;
import dao.DashboardDao;
import service.DashboardServiceImpl;

public class DashboardServiceImplTest {
	private DashboardDao mockDashboardDao;
    private DashboardServiceImpl dashboardService;

    @BeforeEach
    public void setUp() {
        mockDashboardDao = mock(DashboardDao.class);
        dashboardService = new DashboardServiceImpl(mockDashboardDao);
    }
    
    @Test
    public void testCreateDashboard() throws Exception {
        Dashboard dashboard = new Dashboard();
        when(mockDashboardDao.create(dashboard)).thenReturn(true);

        boolean result = dashboardService.createDashboard(dashboard);

        assertTrue(result);
        verify(mockDashboardDao).create(dashboard);
    }

    @Test
    public void testUpdateDashboard() throws Exception {
        Dashboard dashboard = new Dashboard();
        when(mockDashboardDao.update(dashboard)).thenReturn(true);

        boolean result = dashboardService.updateDashboard(dashboard);

        assertTrue(result);
        verify(mockDashboardDao).update(dashboard);
    }

    @Test
    public void testDeleteDashboardById() throws Exception {
        long id = 1L;
        when(mockDashboardDao.deleteById(id)).thenReturn(true);

        boolean result = dashboardService.deleteDashboardById(id);

        assertTrue(result);
        verify(mockDashboardDao).deleteById(id);
    }
    
    @Test
    public void testDeleteDashboard() throws Exception {
        Dashboard dashboard = new Dashboard();
        when(mockDashboardDao.delete(dashboard)).thenReturn(true);

        boolean result = dashboardService.deleteDashboard(dashboard);

        assertTrue(result);
        verify(mockDashboardDao).delete(dashboard);
    }

    @Test
    public void testGetDashboardById() throws Exception {
        long id = 1L;
        Dashboard dashboard = new Dashboard();
        when(mockDashboardDao.findById(id)).thenReturn(dashboard);

        Dashboard result = dashboardService.getDashboardById(id);

        assertNotNull(result);
        assertEquals(dashboard, result);
        verify(mockDashboardDao).findById(id);
    }

//    @Test
//    public void testGetAllDashboards() throws Exception {
//        List<Dashboard> dashboards = Arrays.asList(new Dashboard(), new Dashboard());
//        when(mockDashboardDao.findAll()).thenReturn(dashboards);
//
//        List<Dashboard> result = dashboardService.getAllDashboards();
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        verify(mockDashboardDao).findAll();
//    }

    @Test
    public void testGetLatestDashboardByStoreId() throws Exception {
        long storeId = 1L;
        Dashboard dashboard = new Dashboard();
        when(mockDashboardDao.findLatestDashboardByStoreId(storeId)).thenReturn(dashboard);

        Dashboard result = dashboardService.getLatestDashboardByStoreId(storeId);

        assertNotNull(result);
        assertEquals(dashboard, result);
        verify(mockDashboardDao).findLatestDashboardByStoreId(storeId);
    }

    @Test
    public void testGetDashboardByStoreName() throws Exception {
        String storeName = "My Store";
        Dashboard dashboard = new Dashboard();
        when(mockDashboardDao.getDashboardByStoreName(storeName)).thenReturn(dashboard);

        Dashboard result = dashboardService.getDashboardByStoreName(storeName);

        assertNotNull(result);
        assertEquals(dashboard, result);
        verify(mockDashboardDao).getDashboardByStoreName(storeName);
    }

    // --- Exception tests ---

    @Test
    public void testCreateDashboardException() throws Exception {
        Dashboard dashboard = new Dashboard();
        when(mockDashboardDao.create(dashboard)).thenThrow(new RuntimeException("DB error"));

        boolean result = dashboardService.createDashboard(dashboard);

        assertFalse(result);
        verify(mockDashboardDao).create(dashboard);
    }

    @Test
    public void testUpdateDashboardException() throws Exception {
        Dashboard dashboard = new Dashboard();
        when(mockDashboardDao.update(dashboard)).thenThrow(new RuntimeException("DB error"));

        boolean result = dashboardService.updateDashboard(dashboard);

        assertFalse(result);
        verify(mockDashboardDao).update(dashboard);
    }

    @Test
    public void testDeleteDashboardByIdException() throws Exception {
        long id = 1L;
        when(mockDashboardDao.deleteById(id)).thenThrow(new RuntimeException("DB error"));

        boolean result = dashboardService.deleteDashboardById(id);

        assertFalse(result);
        verify(mockDashboardDao).deleteById(id);
    }

    @Test
    public void testDeleteDashboardException() throws Exception {
        Dashboard dashboard = new Dashboard();
        when(mockDashboardDao.delete(dashboard)).thenThrow(new RuntimeException("DB error"));

        boolean result = dashboardService.deleteDashboard(dashboard);

        assertFalse(result);
        verify(mockDashboardDao).delete(dashboard);
    }

    @Test
    public void testGetDashboardByIdException() throws Exception {
        long id = 1L;
        when(mockDashboardDao.findById(id)).thenThrow(new RuntimeException("DB error"));

        Dashboard result = dashboardService.getDashboardById(id);

        assertNull(result);
        verify(mockDashboardDao).findById(id);
    }

//    @Test
//    public void testGetAllDashboardsException() throws Exception {
//        when(mockDashboardDao.findAll()).thenThrow(new RuntimeException("DB error"));
//
//        List<Dashboard> result = dashboardService.getAllDashboards();
//
//        assertNull(result);
//        verify(mockDashboardDao).findAll();
//    }

    @Test
    public void testGetLatestDashboardByStoreIdException() throws Exception {
        long storeId = 1L;
        when(mockDashboardDao.findLatestDashboardByStoreId(storeId)).thenThrow(new RuntimeException("DB error"));

        Dashboard result = dashboardService.getLatestDashboardByStoreId(storeId);

        assertNull(result);
        verify(mockDashboardDao).findLatestDashboardByStoreId(storeId);
    }

    @Test
    public void testGetDashboardByStoreNameException() throws Exception {
        String storeName = "Store A";
        when(mockDashboardDao.getDashboardByStoreName(storeName)).thenThrow(new RuntimeException("DB error"));

        Dashboard result = dashboardService.getDashboardByStoreName(storeName);

        assertNull(result);
        verify(mockDashboardDao).getDashboardByStoreName(storeName);
    }
}
