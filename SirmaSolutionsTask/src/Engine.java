
import interfaces.Runnable;
import factories.RecordFactory;
import io.FileIO;
import io.Writer;
import model.Record;
import model.Team;
import services.EmployeeService;
import ui.EmployeeJTable;

import java.util.List;
import java.util.stream.Collectors;

import utils.Const;
import utils.Const.*;

public class Engine implements Runnable {

	private FileIO fileIO;
	private Writer writer;
	private EmployeeService emplService;
	private String mSelectedFileName;

	public Engine(String selectedFileName, FileIO fileIO, Writer writer, EmployeeService emplService) {
		this.fileIO = fileIO;
		this.writer = writer;
		this.emplService = emplService;
		this.mSelectedFileName = selectedFileName;
	}

	@Override
	public void run() {

		if (mSelectedFileName != null) {
			List<Record> records = this.fileIO.read(mSelectedFileName).
					stream()
					.map(RecordFactory::execute)
					.collect(Collectors.toList());

			this.emplService.addEmployeeRecords(records);

			List<Team> teams = this.emplService.findAllTeamsWithOverlap();
			
			new EmployeeJTable(teams).show();
		
			printResult(teams);
			
		} else {
			System.out.println(Const.NO_FILE_SELECTED);
		}

	}

	private void printResult(List<Team> teams) {
				
		if (teams.size() > 0) {
			teams.sort((team1, team2) -> (int) (team2.getTotalDuration() - team1.getTotalDuration()));
			Team bestTeam = teams.get(0);

			this.writer.write(String.format(Const.BEST_TEAM_PATTERN, bestTeam.getFirstEmployeeId(),
					bestTeam.getSecondEmployeeId(), bestTeam.getTotalDuration()));
		} else {
			this.writer.write(Const.NO_TEAMS_MSG);
		}
	}
}
