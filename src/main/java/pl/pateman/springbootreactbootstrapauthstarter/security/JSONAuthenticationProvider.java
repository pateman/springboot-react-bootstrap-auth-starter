package pl.pateman.springbootreactbootstrapauthstarter.security;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class JSONAuthenticationProvider extends DaoAuthenticationProvider {
    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        Authentication successAuthentication = super.createSuccessAuthentication(principal, authentication, user);
        return copySuccessfulAuth(authentication, successAuthentication);
    }

    private Authentication copySuccessfulAuth(Authentication originalAuth, Authentication successAuthentication) {
        Authentication result = successAuthentication;
        if (originalAuth instanceof JSONAuthenticationToken) {
            result = new JSONAuthenticationToken(
                    successAuthentication.getPrincipal(),
                    successAuthentication.getCredentials(),
                    successAuthentication.getAuthorities(),
                    ((JSONAuthenticationToken) originalAuth).isRememberMe());
            ((JSONAuthenticationToken) result).setDetails(originalAuth.getDetails());
        }
        return result;
    }
}
