package com.hero.biz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * * * @Description Data Transfer Object for GitHub repository details.
 *
 * @Author Andrea
 * @Date 2025/11/30 12:35
 * @Version 1.0
 *
 */
@Getter
@Setter
public class GitHubRepoDTO {

    @JsonProperty("full_name")
    private String fullName;

    private String description;

    @JsonProperty("clone_url")
    private String cloneUrl;

    @JsonProperty("stargazers_count")
    private int stargazersCount;

    @JsonProperty("created_at")
    private String createdAt;
}
