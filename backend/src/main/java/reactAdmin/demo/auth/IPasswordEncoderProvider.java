package reactAdmin.demo.auth;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface IPasswordEncoderProvider {
    PasswordEncoder getEncoder();
}
