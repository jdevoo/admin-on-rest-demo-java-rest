package demo.reactAdmin.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    static final Logger LOG = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);

    //public JWTLoginFilter(String url, AuthenticationManager authManager) {
    public JWTLoginFilter(AuthenticationManager authManager) {
        setFilterProcessesUrl(WebSecurityConfig.LOGIN_ENDPOINT);
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req, HttpServletResponse res) {
        String username;
        String password;
        try {
            AccountCredentials creds = new ObjectMapper()
                .readValue(req.getInputStream(), AccountCredentials.class);
            username = creds.getUsername();
            password = creds.getPassword();
            LOG.debug("username = "+username);
            LOG.debug("password = "+password);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password,
                            Collections.emptyList()
                    )
            );
        } catch (IOException ex) {
            throw new BadCredentialsException("username or password missing");
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {
        TokenAuthenticationService
                .addAuthentication(res, auth.getName());
    }

}
