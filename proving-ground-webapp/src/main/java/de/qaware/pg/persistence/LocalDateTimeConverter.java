package de.qaware.pg.persistence;

import org.jooq.impl.AbstractConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LocalDateTimeConverter extends AbstractConverter<Timestamp, LocalDateTime> {

    public LocalDateTimeConverter() {
        super(Timestamp.class, LocalDateTime.class);
    }

    @Override
    public LocalDateTime from(Timestamp databaseObject) {
        return databaseObject.toLocalDateTime();
    }

    @Override
    public Timestamp to(LocalDateTime userObject) {
        return Timestamp.valueOf(userObject);
    }
}
