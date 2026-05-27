package backend.academy.linktracker.bot.unit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import backend.academy.linktracker.bot.dto.LinkResponse;
import backend.academy.linktracker.bot.dto.ListLinkResponse;
import backend.academy.linktracker.bot.util.ListCommandHelper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class ListCommandHelperTest {

    private final ListCommandHelper helper = new ListCommandHelper();

    @Test
    void formateResponse_withLinks_returnFormatedLinksMessage() {
        List<String> links = List.of("https://1234", "https://asasddkl", "https://ajshdoiwj");
        String expectedMessage = """
            Отслеживаемые ссылки:
            https://1234
            https://asasddkl
            https://ajshdoiwj
            """;

        String actualMessage = helper.formateResponse(links);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void formateResponse_withEmptyLinks_returnFormatedLinksMessage() {
        List<String> links = List.of();
        String expectedMessage = """
            Отслеживаемые ссылки:
            """;

        String actualMessage = helper.formateResponse(links);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void filterLinksByTag_withValidTag_returnLinksFilteredByTag() {
        Optional<String> tag = Optional.of("Work");
        LinkResponse linkResponse1 = new LinkResponse(1, "https://1234", List.of("Work", "Home"));
        LinkResponse linkResponse2 = new LinkResponse(2, "https://askdm", List.of("Home"));
        LinkResponse linkResponse3 = new LinkResponse(3, "https://askdkaldj", List.of("Work"));
        LinkResponse linkResponse4 = new LinkResponse(2, "https://askdm", List.of("Something"));

        ListLinkResponse response =
                new ListLinkResponse(List.of(linkResponse1, linkResponse2, linkResponse3, linkResponse4), 4);

        List<String> expectedList = List.of("https://1234", "https://askdkaldj");

        List<String> actualLinks = helper.filterLinksByTag(response, tag);

        assertEquals(expectedList, actualLinks);
    }

    @Test
    void filterLinksByTag_withEmptyTag_returnLinksFilteredByTag() {
        Optional<String> tag = Optional.empty();
        LinkResponse linkResponse1 = new LinkResponse(1, "https://1234", List.of("Work", "Home"));
        LinkResponse linkResponse2 = new LinkResponse(2, "https://askdm", List.of("Home"));
        LinkResponse linkResponse3 = new LinkResponse(3, "https://askdkaldj", List.of("Work"));
        LinkResponse linkResponse4 = new LinkResponse(2, "https://askdm", List.of("Something"));

        ListLinkResponse response =
                new ListLinkResponse(List.of(linkResponse1, linkResponse2, linkResponse3, linkResponse4), 4);

        List<String> expectedList = List.of("https://1234", "https://askdm", "https://askdkaldj", "https://askdm");

        List<String> actualLinks = helper.filterLinksByTag(response, tag);

        assertEquals(expectedList, actualLinks);
    }
}
