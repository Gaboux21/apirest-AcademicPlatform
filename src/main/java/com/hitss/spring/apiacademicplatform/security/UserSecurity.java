package com.hitss.spring.apiacademicplatform.security;

import com.hitss.spring.apiacademicplatform.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("userSecurity") // El nombre "userSecurity" debe coincidir con @PreAuthorize
public class UserSecurity {

    public boolean hasStudentId(Authentication authentication, Long studentId) {
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            Long currentUserId = ((UserDetailsImpl) userDetails).getId();
            return currentUserId != null && currentUserId.equals(studentId);
        }
        return false;
    }
}
