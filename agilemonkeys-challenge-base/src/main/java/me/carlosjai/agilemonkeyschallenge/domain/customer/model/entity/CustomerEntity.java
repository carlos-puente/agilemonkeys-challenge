package me.carlosjai.agilemonkeyschallenge.domain.customer.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.carlosjai.agilemonkeyschallenge.domain.auditable.Auditable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class CustomerEntity extends Auditable<String> {

  @Id
  @GeneratedValue(generator = "seqCustomer")
  @GenericGenerator(name = "seqCustomer", parameters = {
      @Parameter(name = "sequence_name", value = "customer_seq"),
      @Parameter(name = "increment_size", value = "1")
  })
  private Long sk;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  @Column(name = "picture_name")
  private String picture;
  @Column(name = "picture_bytes")
  private byte[] pictureBytes;
}
