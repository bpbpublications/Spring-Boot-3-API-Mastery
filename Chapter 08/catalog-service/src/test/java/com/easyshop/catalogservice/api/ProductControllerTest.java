package com.easyshop.catalogservice.api;

import com.easyshop.catalogservice.exception.ProductAlreadyExistsException;
import com.easyshop.catalogservice.exception.ProductCodeMismatchException;
import com.easyshop.catalogservice.exception.ProductNotFoundException;
import com.easyshop.catalogservice.generated.model.ProductCategory;
import com.easyshop.catalogservice.generated.model.ProductRequest;
import com.easyshop.catalogservice.generated.model.ProductResponse;
import com.easyshop.catalogservice.model.PutProduct;
import com.easyshop.catalogservice.security.SecurityConfig;
import com.easyshop.catalogservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

    private static final String MANAGER_ROLE = "ROLE_manager";
    private static final String USER_ROLE = "ROLE_user";

    @MockBean
    private ProductService productService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void findProductByCodeOkTest() {
        var responseExpected = new ProductResponse()
                .code("code1")
                .category(ProductCategory.LAPTOP)
                .price(1000L);
        when(productService.findProductByCode("code1")).thenReturn(Mono.just(responseExpected));

        webTestClient
                .mutateWith(mockAccessToken(USER_ROLE))
                .get()
                .uri("/products/code1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .consumeWith(result -> assertThat(result.getResponseBody()).isEqualTo(responseExpected));

    }

    @Test
    void findProductByCodeKoProductNotFoundTest() {
        when(productService.findProductByCode("code1")).thenReturn(Mono.error(new ProductNotFoundException("code1")));

        webTestClient
                .mutateWith(mockAccessToken(USER_ROLE))
                .get()
                .uri("/products/code1")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void addProductOkTest() {
        var request = new ProductRequest()
                .code("code1")
                .category(ProductCategory.LAPTOP)
                .price(1000L);
        when(productService.addProduct(request)).thenReturn(Mono.just("code1"));
        webTestClient
                .mutateWith(mockAccessToken(MANAGER_ROLE))
                .post()
                .uri("/products")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Void.class)
                .consumeWith(result -> assertThat(result.getResponseHeaders().get("Location").get(0))
                        .isEqualTo("/products/code1"));

    }

    @Test
    void addProductKoValidationTest() {
        var request = new ProductRequest()
                .category(ProductCategory.LAPTOP)
                .price(1000L);
        webTestClient
                .mutateWith(mockAccessToken(MANAGER_ROLE))
                .post()
                .uri("/products")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void addProductKoProductAlreadyExistsTest() {
        var request = new ProductRequest()
                .code("code1")
                .category(ProductCategory.LAPTOP)
                .price(1000L);

        when(productService.addProduct(request)).thenReturn(Mono.error(new ProductAlreadyExistsException("code1")));
        webTestClient
                .mutateWith(mockAccessToken(MANAGER_ROLE))
                .post()
                .uri("/products")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void editProductOk200Test() {
        var request = new ProductRequest()
                .code("code1")
                .category(ProductCategory.LAPTOP)
                .price(1000L);

        var responseExpected = new ProductResponse()
                .code("code1")
                .category(ProductCategory.LAPTOP)
                .price(1000L);

        when(productService.editProduct("code1", request)).thenReturn(Mono.just(new PutProduct(responseExpected, false)));

        webTestClient
                .mutateWith(mockAccessToken(MANAGER_ROLE))
                .put()
                .uri("/products/code1")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .consumeWith(result -> assertThat(result.getResponseBody()).isEqualTo(responseExpected));

    }

    @Test
    void editProductOk201Test() {
        var request = new ProductRequest()
                .code("code1")
                .category(ProductCategory.LAPTOP)
                .price(1000L);

        when(productService.editProduct("code1", request)).thenReturn(Mono.just(new PutProduct(null, true)));

        webTestClient
                .mutateWith(mockAccessToken(MANAGER_ROLE))
                .put()
                .uri("/products/code1")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Void.class)
                .consumeWith(result -> assertThat(result.getResponseHeaders().get("Location").get(0))
                        .isEqualTo("/products/code1"));

    }

    @Test
    void editProductKoCodeMismatchTest() {
        var request = new ProductRequest()
                .code("code1")
                .category(ProductCategory.LAPTOP)
                .price(1000L);

        when(productService.editProduct("code1", request)).thenReturn(Mono.error(new ProductCodeMismatchException()));

        webTestClient
                .mutateWith(mockAccessToken(MANAGER_ROLE))
                .put()
                .uri("/products/code1")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void deleteProductOkTest() {
        when(productService.deleteProduct("code1")).thenReturn(Mono.empty());

        webTestClient
                .mutateWith(mockAccessToken(MANAGER_ROLE))
                .delete()
                .uri("/products/code1")
                .exchange()
                .expectStatus().isNoContent();

    }

    @Test
    void deleteProductKoProductNotFoundTest() {
        when(productService.deleteProduct("code1")).thenReturn(Mono.error(new ProductNotFoundException("code1")));

        webTestClient
                .mutateWith(mockAccessToken(MANAGER_ROLE))
                .delete()
                .uri("/products/code1")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void addProductKoForbiddenTest() {
        var request = new ProductRequest()
                .code("code1")
                .category(ProductCategory.LAPTOP)
                .price(1000L);
        when(productService.addProduct(request)).thenReturn(Mono.just("code1"));
        webTestClient
                .mutateWith(mockAccessToken(USER_ROLE))
                .post()
                .uri("/products")
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden();

    }

    @Test
    void addProductKoUnauthorizedTest() {
        var request = new ProductRequest()
                .code("code1")
                .category(ProductCategory.LAPTOP)
                .price(1000L);
        when(productService.addProduct(request)).thenReturn(Mono.just("code1"));
        webTestClient
                .post()
                .uri("/products")
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized();

    }


    private SecurityMockServerConfigurers.JwtMutator mockAccessToken(String role) {
        return SecurityMockServerConfigurers.mockJwt()
                .authorities(new SimpleGrantedAuthority(role));
    }


}
