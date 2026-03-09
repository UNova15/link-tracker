package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.model.Link;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LinkRepository {
    private final List<Link> repository = new ArrayList<>();

    public List<Link> getActiveLinks(){
        return repository;
    }

}
