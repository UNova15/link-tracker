package backend.academy.linktracker.bot.util;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class TagParser {

    public List<String> parseTags(String tags){
        return Arrays.stream(tags.split(","))
            .map(String::trim)
            .toList();
    }
}
