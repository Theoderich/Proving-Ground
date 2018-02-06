/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.dto.TestRunView;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static de.theo.pg.provingground.persistence.entity.tables.Build.BUILD;
import static de.theo.pg.provingground.persistence.entity.tables.Test.TEST;
import static de.theo.pg.provingground.persistence.entity.tables.TestRun.TEST_RUN;

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
