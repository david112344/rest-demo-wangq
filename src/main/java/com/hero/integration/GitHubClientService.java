package com.hero.integration;

import com.hero.biz.dto.GitHubRepoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Service class for communicating with the GitHub API to fetch repository details.
 * Utilizes a predefined API URL to query repository information and returns structured data in the form of a {@code GitHubRepoDTO}.
 *
 * @Author Andrea
 * @Date 2025/11/30 11:32
 * @Version 1.0
 *
 */
@Service
public class GitHubClientService {

    @Value("${github.api.base-url:https://api.github.com}")
    private String baseUrl;

    @Value("${github.api.token:}")
    private String githubToken;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Fetches the details of a specified GitHub repository using the GitHub API.
     *
     * @param owner the username of the repository owner
     * @param repo the name of the repository
     * @return a {@code GitHubRepoDTO} object containing the repository details, or {@code null} if the repository is not found
     */
    public GitHubRepoDTO fetchRepoDetails(String owner, String repo) {
        String url = baseUrl + "/repos/" + owner + "/" + repo;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // If using a Personal Access Token for higher rate limits:
        if (githubToken != null && !githubToken.isEmpty()) {
            headers.set("Authorization", "Bearer " + githubToken);
        }

        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<GitHubRepoDTO> response = restTemplate.exchange(url, HttpMethod.GET, request, GitHubRepoDTO.class);
            // parsed GitHub repo details
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return null;  // handle 404 (not found)
        }
    }
}
