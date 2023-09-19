/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.models;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
public class Enums {

    public enum TokenType {
        BEARER
    }
    public enum EmploymentType {

        VOLUNTEER,
        FULL_TIME,
        PART_TIME,
        CONTRACT,
        INTERN,
    }

    public enum Role {

        ADMINISTRATOR,
        MANAGER,
        TRAINEE,
        SUPERVISOR,
        ASSESSOR,
        INTERNAL_VERIFIER,
        CENTRE_ADMIN,
        EXTERNAL_VERIFIER,
        PROGRAMME_ADMIN
    }

    public enum LogType {

        TEXT,
        MEDIA

    }

    public enum TimeSpan {

        DAILY,
        WEEKLY

    }

    public enum AssessmentRating {
        BELOW_SATISFACTORY,
        SATISFACTORY,
        BEYOND_SATISACTORY
    }

    public enum ActivityType {

        LOGIN,
        REGISTER,
        VIEW,
        EDIT,

    }

    public enum AssignmentType {

        LOGIN,
        REGISTER,
        VIEW,
        EDIT,

    }

    public enum Status {

        UNVERIFIED,
        VERIFIED,
        INVALID,
        VALID,
        ASSESSED,
        UNASSESSED,
        SUFFICIENT,
        INSUFFICIENT,
        DISABLED,
        ENABLED,
        INACTIVE,
        ACTIVE,
        SUSPENDED,
        MONITORED
    }

}
