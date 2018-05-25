/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.persistence;

import de.qaware.pg.dto.BranchWithNewestBuildView;
import de.qaware.pg.dto.BuildView;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static de.qaware.pg.persistence.entity.tables.Branch.BRANCH;
import static de.qaware.pg.persistence.entity.tables.Build.BUILD;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class BranchWithNewestBuildViewRecordMapper implements RecordMapper<Record, BranchWithNewestBuildView> {

    @Override
    public BranchWithNewestBuildView map(Record record) {
        BuildView buildView = new BuildView(
                record.get(BUILD.ID),
                record.get(BUILD.FK_BRANCH_ID),
                record.get(BUILD.NAME),
                record.get(BUILD.START_TIME),
                record.get(BUILD.COMMITID),
                record.get(BUILD.STATUS),
                record.get(BUILD.NUM_TOTAL),
                record.get(BUILD.NUM_SUCCESS),
                record.get(BUILD.NUM_FAILED),
                record.get(BUILD.NUM_SKIPPED));
        return new BranchWithNewestBuildView(record.get(BRANCH.ID), record.get(BRANCH.FK_PROJECT_ID), record.get(BRANCH.NAME), record.get(BRANCH.STATUS), buildView);

    }
}
