package com.hero.service;

import com.hero.biz.dto.GitHubRepoDTO;
import com.hero.dao.RepositoryInfoDao;
import com.hero.dao.entity.RepositoryInfo;
import com.hero.integration.GitHubClientService;
import com.hero.util.RepoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * * * Unit tests for the {@code RepoDetailsService} class.
 *
 * @Author Andrea
 * @Date 2025/11/30 14:16
 * @Version 1.0
 *
 */
@ExtendWith(MockitoExtension.class)
class RepoDetailsServiceMockTest {

    @Mock
    private GitHubClientService gitHubClient;

    @Mock
    private RepositoryInfoDao repoInfoRepo;

    @InjectMocks
    private RepoDetailsService repoDetailsService;

    private GitHubRepoDTO mockGitHubResponse;

    @BeforeEach
    void setUp() {
        // prepare a mock GitHub response
        mockGitHubResponse = new GitHubRepoDTO();
        mockGitHubResponse.setFullName("octocat/Hello-World");
        mockGitHubResponse.setDescription("This is your first repo!");
        mockGitHubResponse.setCloneUrl("https://github.com/octocat/Hello-World.git");
        mockGitHubResponse.setStargazersCount(80);
        mockGitHubResponse.setCreatedAt("2025-01-26T19:01:12Z");
    }

    @Test
    @DisplayName("When GitHub returns data, correctly map and save it to the database")
    void givenGitHubReturnsData_whenGetRepoDetails_thenMapAndSave() {
        // Given: GitHub API returns valid data
        when(gitHubClient.fetchRepoDetails("octocat", "Hello-World"))
                .thenReturn(mockGitHubResponse);
        when(repoInfoRepo.save(any(RepositoryInfo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When: invoking the service method
        RepositoryInfo result = repoDetailsService.getRepositoryDetails("octocat", "Hello-World");

        // Then: verify the returned data is correctly mapped
        assertNotNull(result);
        assertEquals("octocat", result.getOwner());
        assertEquals("Hello-World", result.getRepoName());
        assertEquals("octocat/Hello-World", result.getFullName());
        assertEquals("This is your first repo!", result.getDescription());
        assertEquals(80, result.getStars());

        // verify interactions
        verify(gitHubClient, times(1)).fetchRepoDetails("octocat", "Hello-World");
        verify(repoInfoRepo, times(1)).save(any(RepositoryInfo.class));
    }

    @Test
    @DisplayName("RepoNotFoundException thrown when GitHub API returns null")
    void givenGitHubReturnsNull_whenGetRepoDetails_thenThrowException() {
        // Given: GitHub API returns null
        when(gitHubClient.fetchRepoDetails("someuser", "unknownrepo"))
                .thenReturn(null);

        // When & Then: The calling method should throw an exception
        RepoNotFoundException exception = assertThrows(
                RepoNotFoundException.class,
                () -> repoDetailsService.getRepositoryDetails("someuser", "unknownrepo")
        );

        // Verification exception information contains owner and repoName
        assertTrue(exception.getMessage().contains("someuser"));
        assertTrue(exception.getMessage().contains("unknownrepo"));

        // Verify that the save method is not called
        verify(repoInfoRepo, never()).save(any(RepositoryInfo.class));
    }

    @Test
    @DisplayName("Verify that the saved entity data is correct")
    void givenGitHubResponse_whenGetRepoDetails_thenSaveCorrectEntity() {
        // Given
        when(gitHubClient.fetchRepoDetails("apache", "dubbo"))
                .thenReturn(mockGitHubResponse);
        when(repoInfoRepo.save(any(RepositoryInfo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        repoDetailsService.getRepositoryDetails("apache", "dubbo");

        // Then: Verify that the saved entity fields are correct
        verify(repoInfoRepo).save(argThat(info ->
                "apache".equals(info.getOwner()) &&
                        "dubbo".equals(info.getRepoName()) &&
                        "octocat/Hello-World".equals(info.getFullName()) &&
                        info.getStars() == 80 &&
                        info.getCreatedAt() != null
        ));
    }
}
