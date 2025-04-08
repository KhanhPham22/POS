package service;

import java.util.List;
import model.Dashboard;

public interface DashboardService {
    boolean createDashboard(Dashboard dashboard);
    boolean updateDashboard(Dashboard dashboard);
    boolean deleteDashboardById(Long dashboardId);
    boolean deleteDashboard(Dashboard dashboard);
    Dashboard getDashboardById(Long dashboardId);
    List<Dashboard> getAllDashboards();
    Dashboard getLatestDashboardByStoreId(Long storeId);
}

