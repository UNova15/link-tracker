package backend.academy.linktracker.scrapper.util;

import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowContent;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RequesterUtil {

    public List<GitHubResponse> filterGitContentByCreationDate(List<GitHubResponse> changes, Instant since) {
        if (changes == null) {
            return List.of();
        }

        return changes.stream()
                .filter(change -> change.updatedAt().isAfter(since))
                .toList();
    }

    public Instant findStackOverflowMaxUpdatedTime(
            List<StackOverflowContent> updatedComments, List<StackOverflowContent> updatedAnswers) {
        if (updatedAnswers == null
                || updatedComments == null
                || (updatedAnswers.isEmpty() && updatedComments.isEmpty())) {
            throw new IllegalArgumentException("List of comments and answers is empty");
        }

        long maxCommentUpdateTime = updatedComments.stream()
                .mapToLong(StackOverflowContent::creationDate)
                .max()
                .orElse(0);
        long maxAnswerUpdateTime = updatedAnswers.stream()
                .mapToLong(StackOverflowContent::creationDate)
                .max()
                .orElse(0);
        long max = Math.max(maxAnswerUpdateTime, maxCommentUpdateTime);

        return Instant.ofEpochSecond(max);
    }

    public Instant findGitHubMaxUpdatedTime(List<GitHubResponse> changes) {
        return changes.stream()
                .map(GitHubResponse::updatedAt)
                .max(Instant::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("List of changes is empty"));
    }

    public List<StackOverflowContent> filterStackOverflowContentByCreationDate(
            List<StackOverflowContent> content, Instant lastCheck) {
        if (content == null) {
            return List.of();
        }

        return content.stream()
                .filter(comment -> Instant.ofEpochSecond(comment.creationDate()).isAfter(lastCheck))
                .toList();
    }
}
