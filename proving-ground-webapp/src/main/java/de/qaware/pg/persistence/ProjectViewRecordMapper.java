/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.persistence;

import de.qaware.pg.dto.ProjectView;
import de.qaware.pg.persistence.entity.tables.records.ProjectRecord;
import org.jooq.RecordMapper;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class ProjectViewRecordMapper implements RecordMapper<ProjectRecord, ProjectView> {


    @Override
    public ProjectView map(ProjectRecord record) {
        return new ProjectView(record.getId(), record.getName());
    }
}
