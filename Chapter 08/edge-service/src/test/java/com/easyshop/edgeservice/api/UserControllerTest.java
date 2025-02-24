package com.easyshop.edgeservice.api;

import com.easyshop.edgeservice.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ReactiveClientRegistrationRepository repository;

    @Test
    void authenticatedUserTest() {
        var expectedUser = UserController.User.builder()
                .username("john")
                .firstName("John")
                .lastName("Smith")
                .email("john@mail.com")
                .roles(List.of("manager"))
                .build();

        webTestClient
                .mutateWith(mockIdToken(expectedUser))
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserController.User.class)
                .value(user -> assertThat(user).isEqualTo(expectedUser));
    }

    @Test
    void unauthenticatedUserTest() {

        webTestClient
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().isFound();
    }

    private SecurityMockServerConfigurers.OidcLoginMutator mockIdToken(UserController.User expectedUser) {
        return SecurityMockServerConfigurers.mockOidcLogin()
                .idToken(builder ->
                        builder
                                .claim(StandardClaimNames.PREFERRED_USERNAME,
                                        expectedUser.username())
                                .claim(StandardClaimNames.GIVEN_NAME,
                                        expectedUser.firstName())
                                .claim(StandardClaimNames.FAMILY_NAME,
                                        expectedUser.lastName())
                                .claim(StandardClaimNames.EMAIL,
                                        expectedUser.email())
                                .claim("roles",
                                        expectedUser.roles())
                );
    }
}
