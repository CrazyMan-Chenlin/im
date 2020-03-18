package com.roy.auth.server.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author chenlin
 */
@Data
public class Role implements GrantedAuthority {

    private Long id;

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
