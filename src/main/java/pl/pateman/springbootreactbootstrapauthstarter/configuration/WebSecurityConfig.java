package pl.pateman.springbootreactbootstrapauthstarter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.pateman.springbootreactbootstrapauthstarter.security.JSONAuthenticationFilter;
import pl.pateman.springbootreactbootstrapauthstarter.security.LoginHandler;
import pl.pateman.springbootreactbootstrapauthstarter.security.RestAuthenticationEntryPoint;

import static pl.pateman.springbootreactbootstrapauthstarter.security.SecurityRole.ADMIN;
import static pl.pateman.springbootreactbootstrapauthstarter.security.SecurityRole.USER;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final LoginHandler loginHandler;

    @Autowired
    public WebSecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint, LoginHandler loginHandler) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.loginHandler = loginHandler;
    }

    private JSONAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JSONAuthenticationFilter filter = new JSONAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(loginHandler);
        filter.setAuthenticationFailureHandler(loginHandler);
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl(LOGIN_URL);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable();

        http.exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint);

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/*.json", "/static/**").permitAll()
                .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.POST, LOGOUT_URL).permitAll()
                .antMatchers("/api/**").authenticated();

        http.formLogin()
                .permitAll();

        http.logout()
                .permitAll();

        http.addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> builder = auth.inMemoryAuthentication();

        builder.withUser("admin")
                .password(encoder().encode("admin"))
                .roles(String.valueOf(ADMIN));
        builder.withUser("user")
                .password(encoder().encode("user"))
                .roles(String.valueOf(USER));
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
