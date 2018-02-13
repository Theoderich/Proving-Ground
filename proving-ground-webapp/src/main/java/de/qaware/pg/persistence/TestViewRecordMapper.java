/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.persistence;

import de.qaware.pg.dto.TestView;
import de.qaware.pg.persistence.entity.tables.records.TestRecord;
import org.jooq.RecordMapper;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestViewRecordMapper implements RecordMapper<TestRecord, TestView> {

    @Override
    public TestView map(TestRecord record) {
        return new TestView(record.getId(), record.getName(), record.getFkBuildLastRun(), record.getFkBuildLastSuccess());
    }
}
