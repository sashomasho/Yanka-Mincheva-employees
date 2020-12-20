package com.sirma.services;

import com.sirma.model.Record;
import com.sirma.model.Team;

import java.util.List;

public interface EmployeeService {

    void addEmployeeRecords(List<Record> records);

    List<Team> findAllTeamsWithOverlap();
}
