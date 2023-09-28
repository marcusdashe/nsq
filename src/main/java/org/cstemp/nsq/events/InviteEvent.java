package org.cstemp.nsq.events;


import jakarta.mail.MessagingException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cstemp.nsq.config.JwtService;
import org.cstemp.nsq.models.relational.Invite;
import org.cstemp.nsq.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.thymeleaf.context.Context;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableAsync
@Configuration
public class InviteEvent {

    @Autowired
    private JwtService jwtUtils;
//
    @Autowired
    EmailService emailService;

    @Value("${africa.elimi.api.baseUrl}")
    private String baseUrl;


    @Data
    public final static class Event extends ApplicationEvent {

        private List<Invite> assessorInvites;

        public Event(Object source, List<Invite> assessorInviteList) {

            super(source);

            assessorInvites = assessorInviteList;
        }

        public Event(Object source, Clock clock, List<Invite> assessorInvites) {
            super(source, clock);
            this.assessorInvites = assessorInvites;
        }

        public List<Invite> getInvites() {
            return assessorInvites;
        }
    }

    @Async
    @EventListener
    public void listener(Event event) throws Exception {

        event.getInvites().forEach((invite) -> {

            //generate jwt token
            String token = jwtUtils.generateJwtToken(invite);

            //create context with token
            Context context = new Context();
            context.setVariable("token", token);
            context.setVariable("url", baseUrl + "/invite/accept/" + token);
            context.setVariable("invite", invite);

            //create properties map
            Map<String, String> props = new HashMap<>();
            props.put("subject", "Invitation For NSQ Assessment (E-Limi)");
            props.put("from", "noreply@e-limi.africa");
            props.put("to", invite.getEmail());

            System.out.println(invite.getEmail());
            //forward to email sender
            try {
                emailService.sendHTMLMail("add-assessor", context, props);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

        });
    }
}
