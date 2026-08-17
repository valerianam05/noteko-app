package com.example.demo.security.principal;

import com.example.demo.entity.AppUser;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserPrincipal implements UserDetails {

  private final UUID userId;
  private final String email;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;

  public UserPrincipal(AppUser user) {
    this.userId = user.getId();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
  }

  public String getRole() {
    return authorities.iterator().next().getAuthority().replace("ROLE_", "");
  }

  @Override
  public String getUsername() {
    return email;
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
