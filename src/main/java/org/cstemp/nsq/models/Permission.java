package org.cstemp.nsq.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    MANAGER_READ("management:read"),
    MANAGER_CREATE("management:create"),
    MANAGER_UPDATE("management:update"),
    MANAGER_DELETE("management:delete"),

    TRAINEE_READ("trainee:read"),
    TRAINEE_CREATE("trainee:create"),
    TRAINEE_UPDATE("trainee:update"),
    TRAINEE_DELETE("trainee:delete"),

    SUPERVISOR_READ("supervisor:read"),
    SUPERVISOR_CREATE("supervisor:create"),
    SUPERVISOR_UPDATE("supervisor:update"),
    SUPERVISOR_DELETE("supervisor:delete"),

    ASSESSOR_READ("assessor:read"),
    ASSESSOR_CREATE("assessor:create"),
    ASSESSOR_UPDATE("assessor:update"),
    ASSESSOR_DELETE("assessor:delete"),

    INTERNAL_VERIFIER_READ("internal_verifier:read"),
    INTERNAL_VERIFIER_CREATE("internal_verifier:create"),
    INTERNAL_VERIFIER_UPDATE("internal_verifier:update"),
    INTERNAL_VERIFIER_DELETE("internal_verifier:delete"),

    CENTRE_ADMIN_READ("center_admin:read"),
    CENTRE_ADMIN_CREATE("center_admin:create"),
    CENTRE_ADMIN_UPDATE("center_admin:update"),
    CENTRE_ADMIN_DELETE("center_admin:delete"),

    EXTERNAL_VERIFIER_READ("external_verifier:read"),
    EXTERNAL_VERIFIER_CREATE("external_verifier:create"),
    EXTERNAL_VERIFIER_UPDATE("external_verifier:update"),
    EXTERNAL_VERIFIER_DELETE("external_verifier:delete"),

    PROGRAMME_ADMIN_READ("programme_admin:read"),
    PROGRAMME_ADMIN_CREATE("programme_admin:create"),
    PROGRAMME_ADMIN_UPDATE("programme_admin:update"),
    PROGRAMME_ADMIN_DELETE("programme_admin:delete");

    @Getter
    private final String permission;
}