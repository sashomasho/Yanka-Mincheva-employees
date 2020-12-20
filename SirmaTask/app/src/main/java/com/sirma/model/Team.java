package com.sirma.model;

public class Team {

    private long firstEmployeeId;
    private long secondEmployeeId;
    private long projectId;
    private long totalDuration;
    

    public Team(long firstEmployeeId, long secondEmployeeId,long projectId,
    		long totalDuration) {
        this.setFirstEmployeeId(firstEmployeeId);
        this.setSecondEmployeeId(secondEmployeeId);
        this.setProjectId(projectId);
        this.setTotalDuration(totalDuration);
    }

    public long getFirstEmployeeId() {
        return this.firstEmployeeId;
    }

    private void setFirstEmployeeId(long firstEmployeeId) {
        this.firstEmployeeId = firstEmployeeId;
    }

    public long getSecondEmployeeId() {
        return this.secondEmployeeId;
    }

    private void setSecondEmployeeId(long secondEmployeeId) {
        this.secondEmployeeId = secondEmployeeId;
    }

    public long getTotalDuration() {
        return this.totalDuration;
    }

    private void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void addOverlapDuration(long overlap) {
        this.totalDuration += overlap;
    }

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
}
