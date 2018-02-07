/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.persistence;

import de.qaware.pg.dto.BranchView;
import de.qaware.pg.persistence.entity.tables.records.BranchRecord;
import org.jooq.RecordMapper;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class BranchViewRecordMapper implements RecordMapper<BranchRecord, BranchView> {

    @Override
    public BranchView map(BranchRecord record) {
        return new BranchView(record.getId(), record.getFkProjectId(), record.getName(), record.getStatus());
    }
}
