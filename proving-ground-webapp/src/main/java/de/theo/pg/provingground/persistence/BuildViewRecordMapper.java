/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.dto.BuildView;
import de.theo.pg.provingground.persistence.entity.tables.records.BuildRecord;
import org.jooq.RecordMapper;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class BuildViewRecordMapper implements RecordMapper<BuildRecord, BuildView> {
    @Override
    public BuildView map(BuildRecord record) {
        return new BuildView(record.getId(), record.getFkBranchId(), record.getName(), record.getStartTime(),
                record.getCommitid(), record.getStatus(), record.getNumTotal(), record.getNumSuccess(),
                record.getNumFailed(), record.getNumSkipped());
    }
}
