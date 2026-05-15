package backend.academy.linktracker.scrapper.util;

import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowContent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ResponseFormatter {
    private static final String ANSWER_TYPE_NAME = "Ответ";
    private static final String COMMENT_TYPE_NAME = "Комментарий";
    private static final String DESCRIPTION_NOT_FOUND_MESSAGE = "Описание отсутствует";
    private static final int MAX_DESCRIPTION_SIZE = 200;

    private static final String GITHUB_PATTERN = """
            *Обновление в GitHub*%n\
            Название Issue/PR: %s%n\
            Имя пользователя: %s%n\
            Время создания: %s%n\
            Описание: %s\
            """;
    private static final String STACK_OVERFLOW_PATTERN = """
             *Обновление в StackOverFlow*%n\
            Вопрос: %s%n\
            Имя пользователя: %s%n\
            Время создания: %s%n\
            %s: %s\
            """;

    public String formatStackOverflowResponse(
            String title, List<StackOverflowContent> answers, List<StackOverflowContent> comments) {
        List<String> updateMessages = new ArrayList<>();

        for (var answer : answers) {
            String message = formatStackOverflowContent(ANSWER_TYPE_NAME, answer, title);
            updateMessages.add(message);
        }

        for (var comment : comments) {
            String message = formatStackOverflowContent(COMMENT_TYPE_NAME, comment, title);
            updateMessages.add(message);
        }
        return String.join("\n\n", updateMessages);
    }

    private String formatStackOverflowContent(String type, StackOverflowContent content, String question) {
        String previewContent = formatPreview(content.body());

        return STACK_OVERFLOW_PATTERN.formatted(
                question, content.user().name(), content.creationDate(), type, previewContent);
    }

    public String formatGitHubResponse(List<GitHubResponse> responses) {
        return responses.stream().map(this::formatGitHubIssue).collect(Collectors.joining("\n\n"));
    }

    private String formatGitHubIssue(GitHubResponse responses) {
        String previewDescription = formatPreview(responses.description());

        return GITHUB_PATTERN.formatted(
                responses.title(), responses.user().login(), responses.updatedAt(), previewDescription);
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
