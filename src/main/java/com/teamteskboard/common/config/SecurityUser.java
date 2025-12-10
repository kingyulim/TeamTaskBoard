package com.teamteskboard.common.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class SecurityUser {

    private final Long id;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;

    public SecurityUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = new HashSet<>(authorities);
    }

}
