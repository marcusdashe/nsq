package org.cstemp.nsq.models.relational;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cstemp.nsq.models.BaseModel;
import org.cstemp.nsq.models.Role;
import org.cstemp.nsq.util.AppEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author ChibuezeHarry & Marcus Dashe
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User  extends BaseModel implements UserDetails {

    @Transient
    public static final AppEncoder encoder = new AppEncoder();

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Size(max = 254)
    @Column(name = "photo_url")
    private String photoUrl;

    @Size(max = 255)
    @Column(name = "first_name")
    private String firstname;

    @Size(max = 255)
    @Column(name = "last_name")
    private String lastname;

    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "email")
    private String email;

    @Pattern(regexp = "^\\(?(\\d{4})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message = "Invalid phone format, should be as 0xxxxxxxxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 15)
    @Column(name = "phone")
    private String phone;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "verified")
    private Boolean verified;
    private Long idd;

    @Transient
    public String getFullName() {
        return firstname + " " + lastname;
    }

    @Transient
    public List<String> getRolesList() {
        return Collections.arrayToList(role.name().split(","));
    }

//    public void addRole(String role) {
//        if ("".equals(this.roles)) {
//            this.roles = role.toUpperCase();
//        } else {
//            this.roles += "," + role.toUpperCase();
//        }
//    }

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Profile profile;

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public User(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
