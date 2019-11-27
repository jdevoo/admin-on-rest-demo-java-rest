package demo.reactAdmin.auth;

import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.GenericFilterBean;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                         HttpServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        Authentication authentication = TokenAuthenticationService
                .getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}
