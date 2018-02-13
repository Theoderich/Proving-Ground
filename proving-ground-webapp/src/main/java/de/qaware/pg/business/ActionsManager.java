/**
 * ﻿Copyright 2012, Deutsche Telekom AG, DTAG GHS GIS. All rights reserved.
 */

package de.qaware.pg.business;

import de.qaware.pg.Branch;
import de.qaware.pg.Build;
import de.qaware.pg.ElementNotFoundException;
import de.qaware.pg.Test;
import de.qaware.pg.dto.TestChangeInformation;
import de.qaware.pg.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * TODO describe type.
 *
 * @author Andreas Janning andreas.janning@qaware.de
 */
@Component
public class ActionsManager {


    private final Persistence persistence;

    @Autowired
    public ActionsManager(Persistence persistence) {
        this.persistence = persistence;
    }


    public void deleteBuild(long branchId, long buildId) throws ElementNotFoundException {
        Branch branch = Branch.loadFromPersistence(persistence, branchId);

        Collection<TestChangeInformation> testChanges = branch.removeBuild(buildId);


        for (TestChangeInformation testChange : testChanges) {
            Test test = testChange.getTest();
            if (testChange.getChange() == TestChangeInformation.TestChange.MODIFIED) {
                Build lastSuccess = test.getLastSuccess();
                persistence.updateTest(test.getDatabaseId(), test.getLastRun().getDatabaseId(), lastSuccess == null ? null : lastSuccess.getDatabaseId());
            } else if (testChange.getChange() == TestChangeInformation.TestChange.DELETED) {
                persistence.deleteTest(test.getDatabaseId());
            }
        }

        persistence.deleteBuild(buildId);
    }

}
