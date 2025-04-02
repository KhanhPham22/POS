package model;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseEntity implements Serializable {
	 // serialVersionUID: A unique identifier used during the serialization and deserialization process.
    // It ensures that a deserialized object matches the version of the class definition.
    // If the class structure changes (e.g., new fields are added), updating this value can prevent
    // issues (like InvalidClassException) when reading old serialized objects.
    private static final long serialVersionUID = 1L;

    private long createdBy = 0;
    private Date createdDate;
  
    private Date lastUpdatedDate;
    private Boolean isDeleted = false;

    // Constructor mặc định (Hibernate cần)
    public BaseEntity() {}

    // Getter và Setter
    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

   

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
