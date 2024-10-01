package me.carlosjai.agilemonkeyschallenge.auth.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.RoleEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails extends UserEntity implements UserDetails {

  private final String username;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;

  public CustomUserDetails(UserEntity byUsername) {
    username = byUsername.getUsername();
    password = byUsername.getPassword();
    List<GrantedAuthority> auths = new ArrayList<>();

    for (RoleEntity roleEntity : byUsername.getRoleEntities()) {

      auths.add(new SimpleGrantedAuthority(roleEntity.getName().toUpperCase()));
    }
    authorities = auths;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.unmodifiableCollection(authorities);
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}