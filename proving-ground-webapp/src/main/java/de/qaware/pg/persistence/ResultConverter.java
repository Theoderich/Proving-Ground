package de.qaware.pg.persistence;

import de.qaware.pg.TestResult;
import org.jooq.impl.EnumConverter;

public class ResultConverter extends EnumConverter<Integer, TestResult> {
    public ResultConverter() {
        super(Integer.class, TestResult.class);
    }
}
