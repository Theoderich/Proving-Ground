package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.dto.Status;
import org.jooq.impl.EnumConverter;

public class StatusConverter extends EnumConverter<Integer, Status> {
    public StatusConverter() {
        super(Integer.class, Status.class);
    }
}
