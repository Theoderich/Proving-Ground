package de.theo.pg.provingground.persistence;

import org.jooq.impl.AbstractConverter;

import java.time.Duration;

public class DurationConverter extends AbstractConverter<Long, Duration> {


    public DurationConverter() {
        super(Long.class, Duration.class);
    }

    @Override
    public Duration from(Long databaseObject) {
        return Duration.ofMillis(databaseObject);
    }

    @Override
    public Long to(Duration userObject) {
        return userObject.toMillis();
    }
}
