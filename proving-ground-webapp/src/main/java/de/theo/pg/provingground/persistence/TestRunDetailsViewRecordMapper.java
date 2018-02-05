/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.dto.TestRunDetailsView;
import de.theo.pg.provingground.persistence.entity.tables.records.TestrunRecord;
import org.jooq.RecordMapper;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class TestRunDetailsViewRecordMapper implements RecordMapper<TestrunRecord, TestRunDetailsView> {

    @Override
    public TestRunDetailsView map(TestrunRecord record) {
        return new TestRunDetailsView(record.getId(), record.getFkTestsuiteId(), record.getTestName(), record.getResult(),
                record.getDuration(), record.getOutput(), record.getErrortype(), record.getErrormessage(), record.getStacktrace());
    }
}
