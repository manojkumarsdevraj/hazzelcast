package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralProjectManagementJobIdInfo extends Cimm2BCentralResponseEntity {

	// Report Header details
	 private String jobId;
	 private String jobName;
	 private String jobStatus;
	 
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	
}