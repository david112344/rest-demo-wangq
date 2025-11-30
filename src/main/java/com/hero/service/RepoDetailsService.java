package com.hero.service;

import com.hero.biz.dto.GitHubRepoDTO;
import com.hero.dao.RepositoryInfoDao;
import com.hero.dao.entity.RepositoryInfo;
import com.hero.integration.GitHubClientService;
import com.hero.util.JsonUtils;
import com.hero.util.RepoNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * * Service class for retrieving repository details.
 *
 * @Author Andrea
 * @Date 2025/11/30 11:47
 * @Version 1.0
 *
 */
@Slf4j
@Service
public class RepoDetailsService {

    private final GitHubClientService gitHubClient;

    private final RepositoryInfoDao repoInfoRepo;

    public RepoDetailsService(GitHubClientService gitHubClient, RepositoryInfoDao repoInfoRepo) {
        this.gitHubClient = gitHubClient;
        this.repoInfoRepo = repoInfoRepo;
    }

    /**
     * Retrieves the details of a repository by its owner and repository name.
     *
     * @param owner the username of the repository owner
     * @param repoName the name of the repository
     * @return a {@code RepositoryInfo} object containing the repository details
     * @throws RepoNotFoundException if the repository is not found on GitHub
     */
    public RepositoryInfo getRepositoryDetails(String owner, String repoName) {
        // 1. fetch from GitHub
        GitHubRepoDTO apiData = gitHubClient.fetchRepoDetails(owner, repoName);

        log.info("Fetched from GitHub API: {}", JsonUtils.toJson(apiData));

        if (apiData == null) {
            // Not found on GitHub
            throw new RepoNotFoundException(owner, repoName);
        }
        // 2. Map API DTO to Entity and save in DB
        RepositoryInfo info = RepositoryInfo.of(owner, repoName, apiData);
        repoInfoRepo.save(info);
        return info;
    }
}
