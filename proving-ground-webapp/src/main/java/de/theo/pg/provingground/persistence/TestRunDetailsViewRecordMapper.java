/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.dto.TestRunDetailsView;
import de.theo.pg.provingground.persistence.entity.tables.Test;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static de.theo.pg.provingground.persistence.entity.tables.ErrorInfo.ERROR_INFO;
import static de.theo.pg.provingground.persistence.entity.tables.TestRun.TEST_RUN;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestRunDetailsViewRecordMapper implements RecordMapper<Record, TestRunDetailsView> {

    @Override
    public TestRunDetailsView map(Record record) {
        return new TestRunDetailsView(record.getValue(TEST_RUN.ID), record.getValue(TEST_RUN.FK_BUILD_ID),
                record.getValue(Test.TEST.NAME), record.getValue(TEST_RUN.RESULT), record.getValue(TEST_RUN.DURATION),
                record.getValue(TEST_RUN.STDOUT), record.getValue(TEST_RUN.STDERR), record.getValue(ERROR_INFO.ERRORTYPE), record.getValue(ERROR_INFO.ERRORMESSAGE),
                record.getValue(ERROR_INFO.STACKTRACE));
    }
}
