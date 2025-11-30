package com.hero.biz;

import com.hero.dao.entity.RepositoryInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * * * @Description Repository response DTO class
 *
 * @Author Andrea
 * @Date 2025/11/30 13:20
 * @Version 1.0
 *
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryResponse {
    /**
     * Full name of the repository (e.g., "owner/repo")
     */
    private String fullName;

    /**
     * Description of the repository
     */
    private String description;

    /**
     * URL to clone the repository
     */
    private String cloneUrl;

    /**
     * Number of stars the repository has
     */
    private int stars;

    /**
     * Creation date of the repository in ISO 8601 format
     */
    private String createdAt;

    public RepositoryResponse(RepositoryInfo info) {
        this.fullName = info.getFullName();
        this.description = info.getDescription();
        this.cloneUrl = info.getCloneUrl();
        this.stars = info.getStars();
        this.createdAt = Optional.ofNullable(info.getCreatedAt())
                .map(Object::toString)
                .orElse(null);
    }
}
