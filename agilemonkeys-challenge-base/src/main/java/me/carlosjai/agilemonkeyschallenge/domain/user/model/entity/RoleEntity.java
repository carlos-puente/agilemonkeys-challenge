package me.carlosjai.agilemonkeyschallenge.domain.user.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class RoleEntity {

  @Id
  @GeneratedValue(generator = "seqRole")
  @GenericGenerator(name = "seqRole", parameters = {
      @Parameter(name = "sequence_name", value = "roles_seq"),
      @Parameter(name = "increment_size", value = "1")
  })
  private Long sk;
  private String name;
}
