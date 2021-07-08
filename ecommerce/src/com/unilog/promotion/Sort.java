package com.unilog.promotion;

public class Sort {
	private String name = "name";
	  private boolean ascending = true;
	  private boolean ignoreCase = true;

	  public String getName() { return this.name; }

	  public void setName(String name) {
	    this.name = name;
	  }
	  public boolean isAscending() {
	    return this.ascending;
	  }
	  public void setAscending(boolean ascending) {
	    this.ascending = ascending;
	  }
	  public boolean isIgnoreCase() {
	    return this.ignoreCase;
	  }
	  public void setIgnoreCase(boolean ignoreCase) {
	    this.ignoreCase = ignoreCase;
	  }
}
