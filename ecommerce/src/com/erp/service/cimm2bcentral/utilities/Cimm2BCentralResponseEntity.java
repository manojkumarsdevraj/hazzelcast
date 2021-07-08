package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralResponseEntity {

	
	public Cimm2BCentralResponseEntity() {
		super();
	}
	public Cimm2BCentralResponseEntity(Object data, Cimm2BCentralStatus status) {
		super();
		this.data = data;
		this.status = status;
	}
	public Object data;
	private Cimm2BCentralStatus status;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Cimm2BCentralStatus getStatus() {
		return status;
	}
	public void setStatus(Cimm2BCentralStatus status) {
		this.status = status;
	}
}
