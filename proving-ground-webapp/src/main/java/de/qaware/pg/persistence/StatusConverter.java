package de.qaware.pg.persistence;

import de.qaware.pg.dto.Status;
import org.jooq.impl.EnumConverter;

public class StatusConverter extends EnumConverter<Integer, Status> {
    public StatusConverter() {
        super(Integer.class, Status.class);
    }
}
