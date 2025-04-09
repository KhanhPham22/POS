package service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DashboardDao;
import model.Dashboard;

public class DashboardServiceImpl implements DashboardService {

    private static final Logger Log = LogManager.getLogger(DashboardServiceImpl.class);
    private final DashboardDao dashboardDao;

    public DashboardServiceImpl(DashboardDao dashboardDao) {
        this.dashboardDao = new DashboardDao();
    }

    @Override
    public boolean createDashboard(Dashboard dashboard) {
        try {
            return dashboardDao.create(dashboard);
        } catch (Exception e) {
            Log.error("Failed to create dashboard", e);
            return false;
        }
    }

    @Override
    public boolean updateDashboard(Dashboard dashboard) {
        try {
            return dashboardDao.update(dashboard);
        } catch (Exception e) {
            Log.error("Failed to update dashboard", e);
            return false;
        }
    }

    @Override
    public boolean deleteDashboardById(Long dashboardId) {
        try {
            return dashboardDao.deleteById(dashboardId);
        } catch (Exception e) {
            Log.error("Failed to delete dashboard with ID: " + dashboardId, e);
            return false;
        }
    }

    @Override
    public boolean deleteDashboard(Dashboard dashboard) {
        try {
            return dashboardDao.delete(dashboard);
        } catch (Exception e) {
            Log.error("Failed to delete dashboard", e);
            return false;
        }
    }

    @Override
    public Dashboard getDashboardById(Long dashboardId) {
        try {
            return dashboardDao.findById(dashboardId);
        } catch (Exception e) {
            Log.error("Failed to retrieve dashboard with ID: " + dashboardId, e);
            return null;
        }
    }

    @Override
    public List<Dashboard> getAllDashboards() {
        try {
            return dashboardDao.findAll();
        } catch (Exception e) {
            Log.error("Failed to retrieve all dashboards", e);
            return null;
        }
    }

    @Override
    public Dashboard getLatestDashboardByStoreId(Long storeId) {
        try {
            return dashboardDao.findLatestDashboardByStoreId(storeId);
        } catch (Exception e) {
            Log.error("Failed to retrieve latest dashboard for store ID: " + storeId, e);
            return null;
        }
    }
}

