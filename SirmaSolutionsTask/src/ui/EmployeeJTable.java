package ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.Team;
import utils.Const;

public class EmployeeJTable {
	List<Team> mTeams;

	public EmployeeJTable(List<Team> teams) {

		this.mTeams = teams;

	}

	public void show() {
		JFrame f = new JFrame();

		List<String> columns = new ArrayList<String>();
		List<String[]> values = new ArrayList<String[]>();

		columns.add(Const.EMPLOYEE_ID_1);
		columns.add(Const.EMPLOYEE_ID_2);
		columns.add(Const.PROJECT_ID);
		columns.add(Const.DAYS_WORKED);

		for (int i = 0; i < mTeams.size(); i++) {
			values.add(new String[] { "" + mTeams.get(i).getFirstEmployeeId(), "" + mTeams.get(i).getSecondEmployeeId(),
					"" + mTeams.get(i).getProjectId(), "" + mTeams.get(i).getTotalDuration() });
		}

		TableModel tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
		JTable table = new JTable(tableModel);

		table.setBounds(30, 40, 200, 300);
		JScrollPane sp = new JScrollPane(table);
		f.add(sp);
		f.setSize(300, 400);
		f.setVisible(true);
	}
}
