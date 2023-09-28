package org.cstemp.nsq.events;


import jakarta.mail.MessagingException;
import lombok.Data;
import org.cstemp.nsq.config.JwtService;
import org.cstemp.nsq.exception.BaseException;
import org.cstemp.nsq.models.relational.RoleRequest;
import org.cstemp.nsq.models.relational.User;
import org.cstemp.nsq.services.AssessorService;
import org.cstemp.nsq.services.EmailService;
import org.cstemp.nsq.services.InternalVerifierService;
import org.cstemp.nsq.util.NinasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@EnableAsync
@Configuration
public class RoleRequestEvent {

//    @Autowired
//    private JwtUtilities jwtUtils;

    @Autowired
    private JwtService jwtUtils;

    @Autowired
    EmailService emailService;

    @Autowired
    AssessorService assessorService;

    @Autowired
    InternalVerifierService internalVerifierService;

    @Value("${africa.elimi.api.baseUrl}")
    private String baseUrl;

    @Data
    public final static class Event extends ApplicationEvent {

        private final RoleRequest roleRequest;

        public Event(Object source, RoleRequest roleRequest) {
            super(source);
            this.roleRequest = roleRequest;
        }
        public RoleRequest getRoleRequest() {
            return roleRequest;
        }
    }

    @Async
    @EventListener
    @Transactional
    public void listener(Event event) throws BaseException {

        RoleRequest roleRequest = event.getRoleRequest();
        //create context with token

        User centreAdmin = roleRequest.getCentre().getAdmin();

        if (centreAdmin != null) {
            Context context = new Context();
            context.setVariable("url", baseUrl);
            context.setVariable("request", roleRequest);

            //create properties map
            Map<String, String> props = new HashMap<>();
            String title = NinasUtil.camelToTitleCase(roleRequest.getRole().toLowerCase());
            props.put("subject", title + " Request To Perform NSQ Assessment (E-Limi)");
            props.put("from", "noreply@e-limi.africa");
            props.put("to", centreAdmin.getEmail());
            try {
                emailService.sendHTMLMail("add-assessor", context, props);
            } catch (MessagingException e) {
                throw new BaseException(e.getLocalizedMessage());
            }
        }

        if ("ASSESSOR".equalsIgnoreCase(roleRequest.getRole())) {

            if (roleRequest.getCentre().getAutoAcceptAssessor()) {
                assessorService.acceptAssessorRequest(roleRequest.getId());
            }
        } else if ("INTERNAL_VERIFIER".equalsIgnoreCase(roleRequest.getRole())) {

            if (roleRequest.getCentre().getAutoAcceptInternalVerifier()) {
                internalVerifierService.acceptInternalVerifierRequest(roleRequest.getId());
            }
        } else {

        }
    }
}
