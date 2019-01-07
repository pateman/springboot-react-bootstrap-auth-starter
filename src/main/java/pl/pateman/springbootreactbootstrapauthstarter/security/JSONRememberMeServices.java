package pl.pateman.springbootreactbootstrapauthstarter.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JSONRememberMeServices extends TokenBasedRememberMeServices {
    public JSONRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
        setAlwaysRemember(true);
    }

    protected boolean shouldProcessRememberMe(Authentication authentication) {
        if (!(authentication instanceof JSONAuthenticationToken)) {
            return true;
        }

        return ((JSONAuthenticationToken) authentication).isRememberMe();
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        if (shouldProcessRememberMe(successfulAuthentication)) {
            super.onLoginSuccess(request, response, successfulAuthentication);
        }
    }
}
