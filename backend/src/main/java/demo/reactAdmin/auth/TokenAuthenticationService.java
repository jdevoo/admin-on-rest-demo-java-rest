package demo.reactAdmin.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static java.util.Collections.emptyList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TokenAuthenticationService {

    static final long EXPIRATIONTIME = 864_000_000; // 10 days
    // see http://www.allkeysgenerator.com/
    static final String SECRET = "H@McQeThWmZq4t7w!z%C*F-JaNdRgUjXn2r5u8x/A?D(G+KbPeShVmYp3s6v9y$B";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "X-Authorization";
    static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationService.class);

    static void addAuthentication(HttpServletResponse res, String username) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
        try {
            res.getOutputStream().write(("{\"token\": \""+ JWT +"\"}").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String header = request.getHeader(HEADER_STRING);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            String token = header.replace(TOKEN_PREFIX, "").trim();
            LOG.debug("token "+token);
            if (!token.equals("null")) {
                // parse the token.
                String user = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();

                return user != null ?
                        new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
                        null;
            }
        }
        return null;
    }

}
