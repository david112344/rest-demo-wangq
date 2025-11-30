package com.hero.web;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.hero.biz.RepositoryResponse;
import com.hero.dao.RepositoryInfoDao;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Repository API.
 *
 * @Author Andrea
 * @Date 2025/12/1 10:00
 * @Version 1.0
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepositoryApiIntegrationTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void startWireMock() {
        // create and start a WireMock server
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("github.api.base-url", () -> wireMockServer.baseUrl());
    }

    @Autowired
    private RepositoryInfoDao repoInfoRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        // clear the database before each test
        repoInfoRepo.deleteAll();
        // reset WireMock before each test
        wireMockServer.resetAll();
    }


    @Test
    @DisplayName("Get data from GitHub and save it")
    void givenRepoNotCached_whenGetRepo_thenFetchFromGitHubAndCache() {
        // 1. setup WireMock stub - simulate GitHub API response
        wireMockServer.stubFor(get(urlEqualTo("/repos/octocat/Hello-World"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{"
                    + "\"full_name\":\"octocat/Hello-World\","
                    + "\"description\":\"This is your first repo!\","
                    + "\"clone_url\":\"https://github.com/octocat/Hello-World.git\","
                    + "\"stargazers_count\":80,"
                    + "\"created_at\":\"2025-01-26T19:01:12Z\""
                    + "}")));

        // 2. invoke our API
        ResponseEntity<RepositoryResponse> resp = restTemplate.getForEntity(
                "/repositories/octocat/Hello-World", RepositoryResponse.class);

        // 3. verify response
        assertEquals(HttpStatus.OK, resp.getStatusCode());
         RepositoryResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals("octocat/Hello-World", body.getFullName());
        assertEquals(80, body.getStars());

        // 4. verify WireMock was called once
        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/repos/octocat/Hello-World")));
    }

    @Test
    @DisplayName("When GitHub returns 404, our API also returns 404")
    void givenRepoNotFound_whenGetRepo_thenReturn404() {
        // 1. setup WireMock stub - simulate GitHub 404 response
        wireMockServer.stubFor(get(urlEqualTo("/repos/someuser/unknownrepo"))
            .willReturn(aResponse()
                .withStatus(404)));

        // 2. invoke our API
        ResponseEntity<String> resp = restTemplate.getForEntity(
                "/repositories/someuser/unknownrepo", String.class);

        // 3. verify response 404
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }
}