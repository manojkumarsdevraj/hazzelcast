package com.unilog.socialmedia.twitter.model;

import com.google.gson.annotations.Expose;

public class SymbolEntityModel {
	@Expose private long start;

	@Expose private long end;

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
	
	
}
