package pl.pateman.springbootreactbootstrapauthstarter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.pateman.springbootreactbootstrapauthstarter.security.JSONAuthenticationFilter;
import pl.pateman.springbootreactbootstrapauthstarter.security.JSONAuthenticationProvider;
import pl.pateman.springbootreactbootstrapauthstarter.security.JSONLoginHandler;
import pl.pateman.springbootreactbootstrapauthstarter.security.JSONRememberMeServices;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/api/login";
    private static final String LOGOUT_URL = "/api/logout";
    private static final String REMEMBER_ME_KEY = "uniqueAndSecret";

    private final JSONLoginHandler loginHandler;
    private final MessageSource messageSource;

    @Autowired
    public WebSecurityConfig(JSONLoginHandler loginHandler, MessageSource messageSource) {
        this.loginHandler = loginHandler;
        this.messageSource = messageSource;
    }

    private JSONAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JSONAuthenticationFilter filter = new JSONAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(loginHandler);
        filter.setAuthenticationFailureHandler(loginHandler);
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl(LOGIN_URL);
        filter.setRememberMeServices(rememberMeServices());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable();

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/*.json", "/static/**").permitAll()
                .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.POST, LOGOUT_URL).permitAll()
                .antMatchers("/api/me").permitAll()
                .antMatchers("/api/**").authenticated();

        http.formLogin()
                .permitAll();

        http.logout()
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessUrl("/api/me")
                .permitAll();

        http.addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> builder = auth.inMemoryAuthentication();

        builder.withUser("admin")
                .password(encoder().encode("admin"))
                .roles("ADMIN");
        builder.withUser("user")
                .password(encoder().encode("user"))
                .roles("USER");

        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new JSONRememberMeServices(REMEMBER_ME_KEY, userDetailsService());
    }

    @Bean
    public UserCache userCache() {
        return new NullUserCache();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        JSONAuthenticationProvider authenticationProvider = new JSONAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setMessageSource(messageSource);
        authenticationProvider.setUserCache(userCache());
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }
}
