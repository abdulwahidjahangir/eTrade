package com.backend.eTrade.security.service;

import com.backend.eTrade.models.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {

    private static final Long serialVersionUID = 1L;

    @Getter
    private Long id;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String email;

    private boolean isVerified;
    private boolean isBlocked;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String firstName, String lastName, String email, boolean isVerified, boolean isBlocked,
                           String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isVerified = isVerified;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName().name());

        return new UserDetailsImpl(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isVerified(),
                user.isBlocked(),
                user.getPassword(),
                List.of(authority)
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return firstName + " " + lastName;
    }
}
