package com.billpayment.billpaydemo.auditable;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/* used to specify that the class itself is not an entity but its attributes can be mapped
in the same way as an entity,however this mappings will apply only to its subclasses*/
@MappedSuperclass
/*is used to configure AuditingEntityListener which contains the @PrePersist and @PreUpdate
 methods in order to capture auditing information*/
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable<T> {
    @CreatedBy
    @Column
//            (columnDefinition = "bigint default 1", updatable = false)
    protected T createdBy;

    @CreatedDate
    @Column(columnDefinition = "timestamp default '2020-04-10 20:47:05.967394'", updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedBy
    @Column
//            (columnDefinition = "bigint default 1")
    protected T lastModifiedBy;

    @LastModifiedDate
    @Column(columnDefinition = "timestamp default '2020-04-10 20:47:05.967394'")
    protected LocalDateTime lastModifiedDate;
}
