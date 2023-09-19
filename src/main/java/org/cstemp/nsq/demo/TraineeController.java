package org.cstemp.nsq.demo;


import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trainee")
@PreAuthorize("hasRole('TRAINEE')")
public class TraineeController {

    @GetMapping
    @PreAuthorize("hasAuthority('trainee:read')")
    public String get() {
        return "GET:: trainee controller";
    }
    @PostMapping
    @PreAuthorize("hasAuthority('trainee:create')")
    @Hidden
    public String post() {
        return "POST:: trainee controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('trainee:update')")
    @Hidden
    public String put() {
        return "PUT:: trainee controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('trainee:delete')")
    @Hidden
    public String delete() {
        return "DELETE:: trainee controller";
    }
}
