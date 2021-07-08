package com.erp.service.cimm2bcentral.utilities;

import java.util.List;

public class Cimm2BCentralProjectManagementInformation  {

	// Report Header details
	private List<Cimm2BCentralManagementHeader> reportHeader;

	//ReportBody details

	private List<Cimm2BCentralManagementBody> reportBody;

	public List<Cimm2BCentralManagementHeader> getReportHeader() {
		return reportHeader;
	}

	public void setReportHeader(List<Cimm2BCentralManagementHeader> reportHeader) {
		this.reportHeader = reportHeader;
	}

	public List<Cimm2BCentralManagementBody> getReportBody() {
		return reportBody;
	}

	public void setReportBody(List<Cimm2BCentralManagementBody> reportBody) {
		this.reportBody = reportBody;
	}


}