package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.model.Link;

public interface LinkChecker {
    boolean checkLink(Link link);
}
