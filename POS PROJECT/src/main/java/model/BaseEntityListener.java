package model;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class BaseEntityListener {

    @PrePersist
    public void setCreatedOn(BaseEntity baseEntity) {
        baseEntity.setCreatedDate(new Date());
    }

    @PreUpdate
    public void setUpdatedOn(BaseEntity baseEntity) {
        baseEntity.setLastUpdatedDate(new Date());
    }
}
