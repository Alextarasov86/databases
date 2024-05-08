package ru.alex.databases;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Note {
    private long id;
    private String title;
    private String text;
    private OffsetDateTime createAt;
    private Author author; // на уровне таблиц автора не будет, будет только author_id хранить в таблице только id
}
