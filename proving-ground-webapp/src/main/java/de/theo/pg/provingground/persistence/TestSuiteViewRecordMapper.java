/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.dto.TestSuiteView;
import de.theo.pg.provingground.persistence.entity.tables.records.TestsuiteRecord;
import org.jooq.RecordMapper;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestSuiteViewRecordMapper implements RecordMapper<TestsuiteRecord, TestSuiteView> {
    @Override
    public TestSuiteView map(TestsuiteRecord record) {
        return new TestSuiteView(record.getId(), record.getFkProjectId(), record.getName(), record.getStartTime(),
                record.getCommitid(), record.getBranch(), record.getStatus(), record.getNumTotal(), record.getNumSuccess(),
                record.getNumFailed(), record.getNumSkipped());
    }
}
