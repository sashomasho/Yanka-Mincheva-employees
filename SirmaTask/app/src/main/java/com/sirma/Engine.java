package com.sirma;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.sirma.factories.RecordFactory;
import com.sirma.io.FileIO;
import com.sirma.model.Record;
import com.sirma.model.Team;
import com.sirma.services.EmployeeService;
import com.sirma.utils.Const;

import java.util.List;
import java.util.stream.Collectors;

public class Engine /*implements Runnable */ {

    private FileIO fileIO;
    private EmployeeService emplService;
    private String mSelectedFileName;

    public Engine(String selectedFileName, FileIO fileIO, EmployeeService emplService) {
        this.fileIO = fileIO;
        this.emplService = emplService;
        this.mSelectedFileName = selectedFileName;
    }

    /*	@Override*/
    public List<Team> run() {
        List<Team> teams = null;
        if (mSelectedFileName != null) {
            List<Record> records = this.fileIO.read(mSelectedFileName).stream()
                    .map(RecordFactory::execute)
                    .collect(Collectors.toList());
            this.emplService.addEmployeeRecords(records);

            teams = this.emplService.findAllTeamsWithOverlap();
            teams.sort((team1, team2) -> (int) (team2.getTotalDuration() - team1.getTotalDuration()));

           // printResult(teams);
        } else {
            System.out.println(Const.NO_FILE_SELECTED);
        }
        return teams;
    }
}
