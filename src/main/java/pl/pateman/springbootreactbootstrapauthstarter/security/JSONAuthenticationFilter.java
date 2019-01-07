package pl.pateman.springbootreactbootstrapauthstarter.security;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Gson gson;

    public JSONAuthenticationFilter() {
        gson = new Gson();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;

        try (InputStream is = request.getInputStream();
             InputStreamReader isr = new InputStreamReader(is);
             JsonReader jsonReader = new JsonReader(isr)) {
            JSONAuthenticationForm form = gson.fromJson(jsonReader, JSONAuthenticationForm.class);

            authRequest = new JSONAuthenticationToken(form.getUsername(), form.getPassword(), form.isRememberMe());
        } catch (IOException e) {
            authRequest = new JSONAuthenticationToken("", "", false);
        }
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private class JSONAuthenticationForm {
        private String username;
        private String password;
        private boolean rememberMe;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isRememberMe() {
            return rememberMe;
        }

        public void setRememberMe(boolean rememberMe) {
            this.rememberMe = rememberMe;
        }
    }
}