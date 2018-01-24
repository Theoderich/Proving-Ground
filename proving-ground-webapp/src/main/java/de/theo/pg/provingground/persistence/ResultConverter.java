package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.TestResult;
import org.jooq.impl.EnumConverter;

public class ResultConverter extends EnumConverter<Integer, TestResult> {
    public ResultConverter() {
        super(Integer.class, TestResult.class);
    }
}
