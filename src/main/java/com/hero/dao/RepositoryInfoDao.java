package com.hero.dao;

import com.hero.dao.entity.RepositoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface to manage {@link RepositoryInfo} entity persistence.
 * Extends the Spring Data JPA {@code JpaRepository} interface for standard.
 *
 * @Author Andrea
 * @Date 2025/11/30 11:46
 * @Version 1.0
 *
 */
public interface RepositoryInfoDao extends JpaRepository<RepositoryInfo, Long> {

    /**
     * Finds a RepositoryInfo entity by its owner and repository name.
     *
     * @param owner the username of the repository owner
     * @param repoName the name of the repository
     * @return an Optional containing the found RepositoryInfo, or empty if not found
     */
    Optional<RepositoryInfo> findByOwnerAndRepoName(String owner, String repoName);
}
