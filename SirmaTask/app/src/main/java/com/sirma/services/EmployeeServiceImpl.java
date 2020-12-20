package com.sirma.services;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.sirma.factories.TeamFactory;
import com.sirma.model.Record;
import com.sirma.model.Team;
import com.sirma.repository.EmployeeRepository;
import com.sirma.utils.Const;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository emplRepo;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.emplRepo = employeeRepository;
    }

    /**
     * Method which save all records to the database using EmployeeRepository
     */
    @Override
    public void addEmployeeRecords(List<Record> records) {
        this.emplRepo.saveAll(records);
    }

    /**
     * Method which finding all all teams, couples which have overlap and save them
     * into List<Team>
     */
    @Override
    public List<Team> findAllTeamsWithOverlap() {
        List<Record> allRecords = this.emplRepo.getAllRecords();

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < allRecords.size() - 1; i++) {
            for (int j = i + 1; j < allRecords.size(); j++) {
                Record firstEmpl = allRecords.get(i);
                Record secondEmpl = allRecords.get(j);

                if (firstEmpl.getProjectId() == secondEmpl.getProjectId() && hasOverlap(firstEmpl, secondEmpl)) {
                    long overlapDays = calculateOverlap(firstEmpl, secondEmpl);

                    if (overlapDays > Const.DEFAULT_OVERLAP_ZERO_DAYS) {
                        updateTeamCollection(teams, firstEmpl, secondEmpl, overlapDays);
                    }
                }
            }
        }
        return teams;
    }

    /**
     * Method which calculating the total overlap and returning it
     */
    private long calculateOverlap(Record firstEmpl, Record secondEmpl) {
        Date periodStartDate = firstEmpl.getDateFrom().before(secondEmpl.getDateFrom())
                ? secondEmpl.getDateFrom()
                : firstEmpl.getDateFrom();

        Date periodEndDate = firstEmpl.getDateTo().before(secondEmpl.getDateTo()) ? firstEmpl.getDateTo()
                : secondEmpl.getDateTo();

        long diff = periodEndDate.getTime() - periodStartDate.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * hasOverlap method returning if two employees have overlap
     */
    private boolean hasOverlap(Record firstEmpl, Record secondEmpl) {
        // have overlap if (startA <= endB) and (endA >= startB)
        return (firstEmpl.getDateFrom().before(secondEmpl.getDateTo())
                || firstEmpl.getDateFrom().equals(secondEmpl.getDateTo()))
                && (firstEmpl.getDateTo().after(secondEmpl.getDateFrom())
                || firstEmpl.getDateTo().equals(secondEmpl.getDateFrom()));
    }

    /**
     * method check and returning if the current team is already present in team
     * collection (worked together under others projects)
     */
    private boolean isTeamPresent(Team team, long firstEmplId, long secondEmplId) {
        return (team.getFirstEmployeeId() == firstEmplId && team.getSecondEmployeeId() == secondEmplId)
                || (team.getFirstEmployeeId() == secondEmplId && team.getSecondEmployeeId() == firstEmplId);
    }

    private void updateTeamCollection(List<Team> teams, Record firstEmpl, Record secondEmpl,
									  long overlapDays) {
        AtomicBoolean isPresent = new AtomicBoolean(false);
        // If the team is present -> update team's total overlap

		for (Team team: teams) {
			if (isTeamPresent(team, firstEmpl.getEmployeeId(), secondEmpl.getEmployeeId())) {
				team.addOverlapDuration(overlapDays);
				isPresent.set(true);
			}
		}

			// If the team isn't present -> create and add new team with the current data
        if (!isPresent.get()) {
            Team newTeam = TeamFactory.execute(firstEmpl.getEmployeeId(), secondEmpl.getEmployeeId(),
                    secondEmpl.getProjectId(), overlapDays);
            teams.add(newTeam);
        }
    }
}
