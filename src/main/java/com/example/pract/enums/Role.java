package com.example.pract.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.pract.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    ADMIN(Collections.emptySet()),
    USER(Collections.emptySet());

    @Getter
    private final Set<Permission> permissions;

    public List<GrantedAuthority> getAuthorities() {
        return Stream.concat(
                        getPermissions().stream()
                                .map(permission -> (GrantedAuthority) new SimpleGrantedAuthority(permission.name())),
                        Stream.of((GrantedAuthority) new SimpleGrantedAuthority(this.name()))
                )
                .collect(Collectors.toList());
    }
}
