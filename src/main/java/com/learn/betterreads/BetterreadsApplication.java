package com.learn.betterreads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BetterreadsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BetterreadsApplication.class, args);
    }

    /**
     * Verifies successful github oAuth authentication if user's Name is printed.
     *
     * @param principal
     * @return
     */
    @RequestMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println(principal);
        return principal.getAttribute("name");
    }


}
