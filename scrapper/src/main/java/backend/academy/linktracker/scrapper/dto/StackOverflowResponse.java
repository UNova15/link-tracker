package backend.academy.linktracker.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StackOverflowResponse<T>(
        @JsonProperty("items") List<T> items,
        @JsonProperty("has_more") boolean hasMore,
        @JsonProperty("quota_max") int quotaMax,
        @JsonProperty("quota_remaining") int quotaRemaining) {}
