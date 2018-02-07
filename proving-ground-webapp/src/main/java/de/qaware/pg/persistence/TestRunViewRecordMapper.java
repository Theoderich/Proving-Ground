/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.persistence;

import de.qaware.pg.dto.TestRunView;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static de.qaware.pg.persistence.entity.tables.Build.BUILD;
import static de.qaware.pg.persistence.entity.tables.Test.TEST;
import static de.qaware.pg.persistence.entity.tables.TestRun.TEST_RUN;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestRunViewRecordMapper implements RecordMapper<Record, TestRunView> {


    @Override
    public TestRunView map(Record record) {
        return new TestRunView(
                record.getValue(TEST_RUN.ID),
                record.getValue(TEST_RUN.FK_BUILD_ID),
                record.getValue(TEST.FK_BUILD_LAST_SUCCESS),
                record.getValue(BUILD.NAME),
                record.getValue(TEST.NAME),
                record.getValue(TEST_RUN.RESULT),
                record.getValue(TEST_RUN.DURATION));
    }
}
