package org.cstemp.nsq.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.cstemp.nsq.models.Permission.*;

@RequiredArgsConstructor
public enum Role {

    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_CREATE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    MANAGER_READ,
                    MANAGER_CREATE,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    TRAINEE_READ,
                    TRAINEE_CREATE,
                    TRAINEE_UPDATE,
                    TRAINEE_DELETE,
                    SUPERVISOR_READ,
                    SUPERVISOR_CREATE,
                    SUPERVISOR_UPDATE,
                    SUPERVISOR_DELETE,
                    ASSESSOR_READ,
                    ASSESSOR_CREATE,
                    ASSESSOR_UPDATE,
                    ASSESSOR_DELETE,
                    INTERNAL_VERIFIER_READ,
                    INTERNAL_VERIFIER_CREATE,
                    INTERNAL_VERIFIER_UPDATE,
                    INTERNAL_VERIFIER_DELETE,
                    CENTRE_ADMIN_READ,
                    CENTRE_ADMIN_CREATE,
                    CENTRE_ADMIN_UPDATE,
                    CENTRE_ADMIN_DELETE,
                    EXTERNAL_VERIFIER_READ,
                    EXTERNAL_VERIFIER_CREATE,
                    EXTERNAL_VERIFIER_UPDATE,
                    EXTERNAL_VERIFIER_DELETE,
                    PROGRAMME_ADMIN_READ,
                    PROGRAMME_ADMIN_CREATE,
                    PROGRAMME_ADMIN_UPDATE,
                    PROGRAMME_ADMIN_DELETE
            )
    ),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    ),
    TRAINEE(Set.of(
            TRAINEE_READ,
            TRAINEE_CREATE,
            TRAINEE_UPDATE,
            TRAINEE_DELETE
    )),

    SUPERVISOR(Set.of(
            SUPERVISOR_READ,
            SUPERVISOR_CREATE,
            SUPERVISOR_UPDATE,
            SUPERVISOR_DELETE
    )),
    ASSESSOR(Set.of(
            ASSESSOR_READ,
            ASSESSOR_CREATE,
            ASSESSOR_UPDATE,
            ASSESSOR_DELETE
    )),
    INTERNAL_VERIFIER(
            Set.of(
                    INTERNAL_VERIFIER_READ,
                    INTERNAL_VERIFIER_CREATE,
                    INTERNAL_VERIFIER_UPDATE,
                    INTERNAL_VERIFIER_DELETE
            )
    ),
    CENTRE_ADMIN(Set.of(
            CENTRE_ADMIN_READ,
            CENTRE_ADMIN_CREATE,
            CENTRE_ADMIN_UPDATE,
            CENTRE_ADMIN_DELETE
    )),
    EXTERNAL_VERIFIER(
            Set.of(
                    EXTERNAL_VERIFIER_READ,
                    EXTERNAL_VERIFIER_CREATE,
                    EXTERNAL_VERIFIER_UPDATE,
                    EXTERNAL_VERIFIER_DELETE
            )
    ),
    PROGRAMME_ADMIN(
            Set.of(
                    PROGRAMME_ADMIN_READ,
                    PROGRAMME_ADMIN_CREATE,
                    PROGRAMME_ADMIN_UPDATE,
                    PROGRAMME_ADMIN_DELETE
            )
    );

//    USER(Collections.emptySet()),


    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}

