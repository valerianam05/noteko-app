package com.example.demo.security.authorizer;

import com.example.demo.security.principal.UserPrincipal;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("selfOrAdminChecker")
public class SelfOrAdminAuthorizationChecker {

  public boolean isSelfOrAdmin(UUID targetUserId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return false;
    }

    Object principal = authentication.getPrincipal();
    if (!(principal instanceof UserPrincipal currentUser)) {
      return false;
    }

    boolean isAdmin =
        currentUser.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

    boolean isSelf = currentUser.getUserId().equals(targetUserId);

    return isAdmin || isSelf;
  }
}
