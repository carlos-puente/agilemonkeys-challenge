package me.carlosjai.agilemonkeyschallenge.domain.auditable;

import static jakarta.persistence.TemporalType.TIMESTAMP;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable<U> {

  @CreatedBy
  protected U createdBy;
  @CreatedDate
  @Temporal(TIMESTAMP)
  protected Date creationDate;
  @LastModifiedBy
  protected U lastModifiedBy;
  @LastModifiedDate
  @Temporal(TIMESTAMP)
  protected Date lastModifiedDate;

}

