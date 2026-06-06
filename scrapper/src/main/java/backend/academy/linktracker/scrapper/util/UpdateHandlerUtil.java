package backend.academy.linktracker.scrapper.util;

import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowContent;
import java.time.Instant;
import java.util.List;
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
        List<StackOverflowContent> filteredAnswers = filterStackOverflowContentByCreationDate(answers, lastCheck);
        List<StackOverflowContent> filteredComments = filterStackOverflowContentByCreationDate(comments, lastCheck);

        filteredComments.addAll(filteredAnswers);
        return filteredComments;
    }

    private List<StackOverflowContent> filterStackOverflowContentByCreationDate(
            List<StackOverflowContent> content, Instant lastCheck) {
        if (content == null) {
            return List.of();
        }

        return content.stream()
                .filter(comment -> Instant.ofEpochSecond(comment.creationDate()).isAfter(lastCheck))
                .toList();
    }
}
