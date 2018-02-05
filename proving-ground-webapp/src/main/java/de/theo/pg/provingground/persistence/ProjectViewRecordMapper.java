/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.theo.pg.provingground.persistence;

import de.theo.pg.provingground.dto.ProjectView;
import de.theo.pg.provingground.persistence.entity.tables.records.ProjectRecord;
import org.jooq.RecordMapper;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
public class ProjectViewRecordMapper implements RecordMapper<ProjectRecord, ProjectView> {


    @Override
    public ProjectView map(ProjectRecord record) {
        return new ProjectView(record.getId(), record.getName(), record.getStatus());
    }
}
