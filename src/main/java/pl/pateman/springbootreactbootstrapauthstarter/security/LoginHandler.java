package pl.pateman.springbootreactbootstrapauthstarter.security;

import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LoginHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    private final Gson gson;

    public LoginHandler() {
        gson = new Gson();
    }

    private void sendJSONResponse(HttpServletResponse response, int status, Object content) throws IOException {
        String stringContent = gson.toJson(content);
        byte[] bytes = stringContent.getBytes(StandardCharsets.UTF_8);

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.flushBuffer();
    }

    private Authentication createDummyAuthentication() {
        return new UsernamePasswordAuthenticationToken("", "");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        sendJSONResponse(response, HttpServletResponse.SC_UNAUTHORIZED, createDummyAuthentication());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        sendJSONResponse(response, HttpServletResponse.SC_OK, authentication);
    }
}
