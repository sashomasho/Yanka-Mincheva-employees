package services;

import model.Record;
import model.Team;

import java.util.List;

public interface EmployeeService {

    void addEmployeeRecords(List<Record> records);

    List<Team> findAllTeamsWithOverlap();
}
