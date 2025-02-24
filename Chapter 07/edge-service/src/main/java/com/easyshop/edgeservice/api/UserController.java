package com.easyshop.edgeservice.api;

import lombok.Builder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping
    public User whoIam(@AuthenticationPrincipal OidcUser oidcUser) {
        return User.builder()
                .username(oidcUser.getPreferredUsername())
                .email(oidcUser.getEmail())
                .firstName(oidcUser.getGivenName())
                .lastName(oidcUser.getFamilyName())
                .roles(oidcUser.getClaimAsStringList("roles"))
                .build();
    }

    @Builder
    public record User(String username,
                       String email,
                       String firstName,
                       String lastName,
                       List<String> roles){}
}
