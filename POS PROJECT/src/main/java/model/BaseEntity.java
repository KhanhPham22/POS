package model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
public abstract class BaseEntity implements Serializable {

	 // serialVersionUID: A unique identifier used during the serialization and deserialization process.
    // It ensures that a deserialized object matches the version of the class definition.
    // If the class structure changes (e.g., new fields are added), updating this value can prevent
    // issues (like InvalidClassException) when reading old serialized objects.
	private static final long serialVersionUID = 1L;


	//@Column(name="created_by")
	private long createdBy=0;

	
    @CreatedDate
    @Column(name="created_date",updatable = false)
	private Date createdDate;

    @Column(name="last_updated_by")
	private long lastUpdatedBy=0;

	@LastModifiedDate
    @Column(name = "updated_date")
	private Date lastUpdatedDate;
	

	@Column(name="is_deleted")
	private Boolean isDeleted =false;

	 // Explicit setters for JPA listeners
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
