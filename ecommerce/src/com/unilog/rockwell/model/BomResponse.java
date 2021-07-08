package com.unilog.rockwell.model;

import java.util.List;

public class BomResponse {
	private List<BomModel> BOM;

	public void setBOM(List<BomModel> bOM) {
		BOM = bOM;
	}

	public List<BomModel> getBOM() {
		return BOM;
	}
}
