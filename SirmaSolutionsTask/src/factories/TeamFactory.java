package factories;

import model.Team;

public final class TeamFactory {

    public TeamFactory() {
    }

    public static Team execute(long firstEmployeeId, long secondEmployeeId,long projectId, long overlapDuration) {
        return new Team(
                firstEmployeeId,
                secondEmployeeId,
                projectId,
                overlapDuration);
    }
}
