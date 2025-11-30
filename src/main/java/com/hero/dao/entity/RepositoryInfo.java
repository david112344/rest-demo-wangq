package com.hero.dao.entity;

import com.hero.biz.dto.GitHubRepoDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * * @Description warehouseInformationEntityClass
 *
 * @Author Andrea
 * @Date 2025/11/30 11:45
 * @Version 1.0
 */
@Entity
@Table(name = "repository_info")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryInfo {

    /**
     * Primary key for the repository info
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the owner of the repository
     */
    private String owner;

    /**
     * Represents the name of the repository
     */
    private String repoName;

    /**
     * Represents the full name of the repository
     */
    private String fullName;
    private String description;
    private String cloneUrl;
    private int stars;
    private Instant createdAt;  // or use java.time.Instant/LocalDateTime for dates

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createTime;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updateTime;


    public static RepositoryInfo of(String owner, String repoName, GitHubRepoDTO apiData) {
        return RepositoryInfo.builder()
                .owner(owner)
                .repoName(repoName)
                .fullName(apiData.getFullName())
                .description(apiData.getDescription())
                .cloneUrl(apiData.getCloneUrl())
                .stars(apiData.getStargazersCount())
                .createdAt(parseInstant(apiData.getCreatedAt())) // parse created_at string to Instant
                .build();
    }

    /**
     * Parses a date-time string into an {@code Instant} object.
     * If the input string is {@code null} or empty, it returns {@code null}.
     *
     * @param dateStr the date-time string to be parsed, expected to be in ISO-8601 format
     * @return the parsed {@code Instant} object, or {@code null} if the input string is {@code null} or empty
     */
    private static Instant parseInstant(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return Instant.parse(dateStr);
    }
}
