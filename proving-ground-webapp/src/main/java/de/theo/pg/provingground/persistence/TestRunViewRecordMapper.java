/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.TestResult;
import de.theo.pg.provingground.dto.TestRunView;
import org.jooq.Record5;
import org.jooq.RecordMapper;

import java.time.Duration;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestRunViewRecordMapper implements RecordMapper<Record5<Long, Long, String, TestResult, Duration>, TestRunView> {


    @Override
    public TestRunView map(Record5<Long, Long, String, TestResult, Duration> record) {
        return new TestRunView(record.value1(), record.value2(), record.value3(), record.value4(), record.value5());
    }
}
