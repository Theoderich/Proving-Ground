/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.dto.BranchView;
import de.theo.pg.provingground.persistence.entity.tables.records.BranchRecord;
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
