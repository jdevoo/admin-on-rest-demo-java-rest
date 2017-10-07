package aor.demo.crud;

import aor.demo.crud.enums.Role;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Where(clause="published=1")
@Table( uniqueConstraints =  {
    @UniqueConstraint(columnNames = {"email"}),
})
public class PlatformUser implements Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String name;
    public String email;
    public String location;
    public String role = getUserRole().getRole().toString().toLowerCase();
    public String password;
    public boolean published = true;

    @Transient
    private List<GrantedAuthority> authorities;

    public PlatformUser(){}

    public PlatformUser(String email, List<GrantedAuthority> authorities) {
        this.email = email;
        this.authorities = authorities;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public static PlatformUser create(String email, List<GrantedAuthority> authorities) {
        if (StringUtils.isBlank(email)) throw new IllegalArgumentException("Email is blank: " + email);
        return new PlatformUser(email, authorities);
    }

    public Set<GrantedAuthority> getAuthorities() {
        UserRole role = this.getUserRole();
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role.getRole().authority()));
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public UserRole getUserRole() {
        return new UserRole(id, Role.ADMINISTRATOR);

    }

}
