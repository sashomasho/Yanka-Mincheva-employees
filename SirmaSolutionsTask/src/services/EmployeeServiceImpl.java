package services;

import factories.TeamFactory;
import model.Record;
import model.Team;
import repository.EmployeeRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import utils.Const;

public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository emplRepo;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.emplRepo = employeeRepository;
	}

	/** Method which save all records to the database using EmployeeRepository */
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

	/** Method which calculating the total overlap and returning it */
	private long calculateOverlap(Record firstEmpl, Record secondEmpl) {
		LocalDate periodStartDate = firstEmpl.getDateFrom().isBefore(secondEmpl.getDateFrom())
				? secondEmpl.getDateFrom()
				: firstEmpl.getDateFrom();

		LocalDate periodEndDate = firstEmpl.getDateTo().isBefore(secondEmpl.getDateTo()) ? firstEmpl.getDateTo()
				: secondEmpl.getDateTo();

		return Math.abs(ChronoUnit.DAYS.between(periodStartDate, periodEndDate));
	}

	/** hasOverlap method returning if two employees have overlap */
	private boolean hasOverlap(Record firstEmpl, Record secondEmpl) {
		// have overlap if (startA <= endB) and (endA >= startB)
		return (firstEmpl.getDateFrom().isBefore(secondEmpl.getDateTo())
				|| firstEmpl.getDateFrom().isEqual(secondEmpl.getDateTo()))
				&& (firstEmpl.getDateTo().isAfter(secondEmpl.getDateFrom())
						|| firstEmpl.getDateTo().isEqual(secondEmpl.getDateFrom()));
	}

	/**
	 * method check and returning if the current team is already present in team
	 * collection (worked together under others projects)
	 */
	private boolean isTeamPresent(Team team, long firstEmplId, long secondEmplId) {
		return (team.getFirstEmployeeId() == firstEmplId && team.getSecondEmployeeId() == secondEmplId)
				|| (team.getFirstEmployeeId() == secondEmplId && team.getSecondEmployeeId() == firstEmplId);
	}

	/**
	 * If the team is already present, it's total overlap duration will be updated
	 * with the new value, otherwise will be create and add new team with the
	 * current data
	 */
	private void updateTeamCollection(List<Team> teams, Record firstEmpl, Record secondEmpl,
			long overlapDays) {
		AtomicBoolean isPresent = new AtomicBoolean(false);
		// If the team is present -> update team's total overlap
		teams.forEach(team -> {
			if (isTeamPresent(team, firstEmpl.getEmployeeId(), secondEmpl.getEmployeeId())) {
				team.addOverlapDuration(overlapDays);
				isPresent.set(true);
			}
		});
		// If the team isn't present -> create and add new team with the current data
		if (!isPresent.get()) {
			Team newTeam = TeamFactory.execute(firstEmpl.getEmployeeId(), secondEmpl.getEmployeeId(),
					secondEmpl.getProjectId(), overlapDays);
			teams.add(newTeam);
		}
	}
}
