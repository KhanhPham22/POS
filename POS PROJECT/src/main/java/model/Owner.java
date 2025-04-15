package model;

import java.util.UUID;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Owner extends Person { 

    private String ownerNumber;
    private String description;
    private String loginUsername;
    private String loginPassword;
    private Set<Dashboard> dashboards = new HashSet<>();
    private Set<Warehouse> warehouseImports = new HashSet<>();
    
    public Owner() {
    }

    public void generateOwnerId() {
        this.ownerNumber = "OWN-" + UUID.randomUUID().toString();
    }

    // Getter and Setter for ownerNumber
    public String getOwnerNumber() {
        return ownerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        this.ownerNumber = ownerNumber;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for loginUsername
    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    // Getter and Setter for loginPassword
    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
    
    public Set<Dashboard> getDashboards(){
    	return dashboards;
    }
    
    
    public void setDashboards(Set<Dashboard>dashboards) {
    	this.dashboards = dashboards;
    }
    
    public Set<Warehouse> getWarehouseImports(){
    	 return warehouseImports;
    }
    
    public void setWarehouseImports(Set<Warehouse> warehouseImports) {
        this.warehouseImports = warehouseImports;
    }
}
