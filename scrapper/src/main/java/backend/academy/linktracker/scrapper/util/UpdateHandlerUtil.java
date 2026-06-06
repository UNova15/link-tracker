package backend.academy.linktracker.scrapper.util;

import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowContent;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class UpdateHandlerUtil {

    public List<GitHubResponse> filterGitHubContentByCreationDate(List<GitHubResponse> changes, Instant since) {
        if (changes == null) {
            return List.of();
        }
        return changes.stream()
                .filter(change -> change.updatedAt().isAfter(since))
                .toList();
    }

    public Instant findStackOverflowMaxUpdateTime(List<StackOverflowContent> contents) {

        long maxCommentUpdateTime = contents.stream()
                .mapToLong(StackOverflowContent::creationDate)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("List of comments and answers is empty"));

        return Instant.ofEpochSecond(maxCommentUpdateTime);
    }

    public Instant findGitHubMaxUpdateTime(List<GitHubResponse> changes) {
        return changes.stream()
                .map(GitHubResponse::updatedAt)
                .max(Instant::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("List of changes is empty"));
    }

    public List<StackOverflowContent> filterStackOverflowContentByCreationDate(
            List<StackOverflowContent> answers, List<StackOverflowContent> comments, Instant lastCheck) {

        List<StackOverflowContent> safeAnswers = answers == null ? List.of() : answers;
        List<StackOverflowContent> safeComments = comments == null ? List.of() : comments;

        return Stream.concat(safeComments.stream(), safeAnswers.stream())
                .filter(content -> Instant.ofEpochSecond(content.creationDate()).isAfter(lastCheck))
                .toList();
    }
}
