package com.hero.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a GitHub repository is not found.
 * Mark it as 404 Not Found, so that the REST API will automatically return a 404 status code when an exception is thrown.
 *
 * @Author Andrea
 * @Date 2025/11/30 12:15
 * @Version 1.0
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RepoNotFoundException extends RuntimeException {

    public RepoNotFoundException(String owner, String repoName) {
        super(String.format("Repository not found for owner: '%s' and name: '%s'", owner, repoName));
    }
}