package pl.pateman.springbootreactbootstrapauthstarter.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JSONAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final boolean rememberMe;

    public JSONAuthenticationToken(Object principal, Object credentials, boolean rememberMe) {
        super(principal, credentials);
        this.rememberMe = rememberMe;
    }

    public JSONAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, boolean rememberMe) {
        super(principal, credentials, authorities);
        this.rememberMe = rememberMe;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }
}
