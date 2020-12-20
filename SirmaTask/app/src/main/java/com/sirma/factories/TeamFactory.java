package com.sirma.factories;

import com.sirma.model.Team;

public final class TeamFactory {

    public TeamFactory() {
    }

    public static Team execute(long firstEmployeeId, long secondEmployeeId, long projectId, long overlapDuration) {
        return new Team(
                firstEmployeeId,
                secondEmployeeId,
                projectId,
                overlapDuration);
    }
}
