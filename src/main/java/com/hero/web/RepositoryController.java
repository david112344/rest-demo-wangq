package com.hero.web;

import com.hero.biz.RepositoryResponse;
import com.hero.dao.entity.RepositoryInfo;
import com.hero.service.RepoDetailsService;
import com.hero.util.RepoNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The RepositoryController class is a REST controller that handles HTTP requests for GitHub repository details.
 *
 * @Author Andrea
 * @Date 2025/11/30 13:17
 * @Version 1.0
 *
 */
@Slf4j
@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    private final RepoDetailsService repoDetailsService;

    public RepositoryController(RepoDetailsService repoDetailsService) {
        this.repoDetailsService = repoDetailsService;
    }

    /**
     * Endpoint to retrieve GitHub repository details by owner and repository name.
     *
     * @param owner the username of the repository owner
     * @param repoName the name of the repository
     * @return a ResponseEntity containing the repository details or a NOT FOUND status
     */
    @GetMapping("/{owner}/{repoName}")
    public ResponseEntity<RepositoryResponse> getRepoDetails(
            @PathVariable String owner,
            @PathVariable String repoName) {
        try {
            RepositoryInfo info = repoDetailsService.getRepositoryDetails(owner, repoName);
            // Map to response DTO (to control JSON format)
            RepositoryResponse response = new RepositoryResponse(info);
            return ResponseEntity.ok(response);
        } catch (RepoNotFoundException e) {
            log.error("Repository not found: {}/{}", owner, repoName, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}
