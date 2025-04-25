package service;

import java.util.List;
import model.Dashboard;

public interface DashboardService {
    boolean createDashboard(Dashboard dashboard);
    
    boolean updateDashboard(Dashboard dashboard);
    
    boolean deleteDashboardById(long dashboardId);
    
    boolean deleteDashboard(Dashboard dashboard);
    
    Dashboard getDashboardById(long dashboardId);
    
    List<Dashboard> getAllDashboards();
    
    Dashboard getLatestDashboardByStoreId(long storeId);
    
    Dashboard getDashboardByStoreName(String storeName);
}

