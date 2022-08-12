package com.example.problemsolver.model.response;

import java.time.OffsetDateTime;

public interface SearchableItem {
    String getId();
    OffsetDateTime getCreateDateTime();
    String getPrettyCreateDateTime();
    String getContent();
    User getAuthor();
}
