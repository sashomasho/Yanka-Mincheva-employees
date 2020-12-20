package com.sirma.repository;

import com.sirma.model.Record;
import java.util.Collection;
import java.util.List;

public interface EmployeeRepository {

   void saveAll(Collection<Record> records);

    List<Record> getAllRecords();
}
