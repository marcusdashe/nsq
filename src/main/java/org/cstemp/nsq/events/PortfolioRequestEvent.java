package org.cstemp.nsq.events;

import jakarta.mail.MessagingException;
import lombok.Data;
import org.cstemp.nsq.config.JwtService;
import org.cstemp.nsq.models.relational.Portfolio;
import org.cstemp.nsq.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableAsync
@Configuration
public class PortfolioRequestEvent {

    @Autowired
    private JwtService jwtUtils;

    @Autowired
    EmailService emailService;

    @Value("${africa.elimi.api.baseUrl}")
    private String baseUrl;

    @Data
    public final static class Event extends ApplicationEvent {

        private List<Portfolio> portfolios;

        public Event(Object source, List<Portfolio> portfolios) {

            super(source);

            this.portfolios = portfolios;
        }

        public List<Portfolio> getPortfolios() {
            return portfolios;
        }
    }

    @Async
    @EventListener
    public void listener(Event event) throws Exception {

        event.getPortfolios().forEach((portfolio) -> {

            //generate jwt token
            String token = jwtUtils.generateJwtToken(portfolio);

            //create context with token
            Context context = new Context();
            context.setVariable("token", token);
            context.setVariable("url", baseUrl + "/invite/accept/" + token);
            context.setVariable("invite", portfolio);

            //create properties map
            Map<String, String> props = new HashMap<>();
            props.put("subject", "Registration For NSQ Assessment (E-Limi) Portfolio");
            props.put("from", "noreply@e-limi.africa");
            props.put("to", portfolio.getCentre().getAdmin().getEmail());

            //forward to email sender
            try {
                emailService.sendHTMLMail("add-assessor", context, props);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

        });
    }
}
