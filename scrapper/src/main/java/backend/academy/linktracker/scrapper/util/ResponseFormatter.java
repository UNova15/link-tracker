package backend.academy.linktracker.scrapper.util;

import backend.academy.linktracker.scrapper.dto.GitHubResponse;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class ResponseFormatter {
    private static final String DESCRIPTION_NOT_FOUND_MESSAGE = "Описание отсутствует";
    private static final int MAX_DESCRIPTION_SIZE = 200;

    private static final String GITHUB_PATTERN =
        """
            *Обновление в GitHub*
            Название Issue/PR: %s
            Имя пользователя: %s
            Время создания: %s
            Описание: %s
            """;

    public String formatGitHubResponse(GitHubResponse[] responses) {
        return Arrays.stream(responses)
            .map(this::formatGitHubIssue)
            .collect(Collectors.joining("\n\n"));
    }

    private String formatGitHubIssue(GitHubResponse responses) {
        String previewDescription = formatPreview(responses.description());

        return GITHUB_PATTERN.formatted(
            responses.title(),
            responses.user().login(),
            responses.updatedAt(),
            previewDescription
        );
    }

    private String formatPreview(String description) {
        if (description == null || description.isBlank()) {
            return DESCRIPTION_NOT_FOUND_MESSAGE;
        }
        return description.length() > MAX_DESCRIPTION_SIZE
            ? description.substring(0, MAX_DESCRIPTION_SIZE - 1) + "..."
            : description;
    }
}
