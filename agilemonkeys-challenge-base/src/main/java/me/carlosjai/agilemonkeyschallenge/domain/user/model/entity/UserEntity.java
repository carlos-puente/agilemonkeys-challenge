package me.carlosjai.agilemonkeyschallenge.domain.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.carlosjai.agilemonkeyschallenge.domain.auditable.Auditable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"user\"")
public class UserEntity extends Auditable<String> {

  @Serial
  private static final long serialVersionUID = 4331261927806747730L;
  @Id
  @GeneratedValue(generator = "seqUser")
  @GenericGenerator(name = "seqUser", parameters = {
      @Parameter(name = "sequence_name", value = "user_seq"),
      @Parameter(name = "increment_size", value = "1")
  })
  private Long sk;

  private String username;
  private String password;
  @Column(name = "full_name")
  private String fullName;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_sk"), inverseJoinColumns = @JoinColumn(name = "role_sk"))
  private Set<RoleEntity> roleEntities = new HashSet<>();

  public void addRole(RoleEntity roleEntity) {
    roleEntities.add(roleEntity);
  }

}
