package model;

import java.util.List;
import java.util.UUID;

/**
 * Suggestion Data Transfer Object, used to be parsed a Json and transfered
 */
public class SuggestionDTO {

    private UUID id;
    private List<Product> suggestions;

    public SuggestionDTO(UUID id, List<Product> suggestions) {
        this.id = id;
        this.suggestions = suggestions;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Product> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Product> suggestions) {
        this.suggestions = suggestions;
    }
}
