

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.ConsoleOutputWriter;
import io.FileIO;
import io.FileIOImpl;
import io.Writer;
import repository.EmployeeRepository;
import repository.EmployeeRepositoryImpl;
import services.EmployeeService;
import services.EmployeeServiceImpl;
import ui.EmployeeJTable;

public class LongestPeriodTeamMain {
    public static void main(String[] args) {

    	JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		String mSelectedFilePath = null;

		if (returnVal == JFileChooser.APPROVE_OPTION) {			
			mSelectedFilePath = chooser.getSelectedFile().getAbsolutePath();
		}
		
        FileIO fileIO = new FileIOImpl();
        Writer writer = new ConsoleOutputWriter();
        EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
        EmployeeService emplService = new EmployeeServiceImpl(employeeRepository);

        Engine engine = new Engine(mSelectedFilePath, fileIO, writer, emplService);
        engine.run();
    }
}
