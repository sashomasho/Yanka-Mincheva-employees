package com.sirma.model;

import java.util.Date;

public class Record {

    private Long employeeId;
    private Long projectId;
    private Date dateFrom;
    private Date dateTo;

    public Record(long employeeId, long projectId, Date dateFrom, Date dateTo) {
        this.setEmployeeId(employeeId);
        this.setProjectId(projectId);
        this.setDateFrom(dateFrom);
        this.setDateTo(dateTo);
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    private void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    private void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getDateFrom() {
        return this.dateFrom;
    }

    private void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return this.dateTo;
    }

    private void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
