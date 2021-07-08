package com.unilog.model;

public class Criteria {
	  private String name;
	  private String value;
	  private String[] values;
	  private String clause;
	  private boolean applyAnd;

	  public String getName()
	  {
	    return this.name;
	  }
	  public void setName(String name) {
	    this.name = name;
	  }
	  public String getValue() {
	    return this.value;
	  }
	  public void setValue(String value) {
	    this.value = value;
	  }
	  public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	public String getClause() {
	    return this.clause;
	  }
	  public void setClause(String clause) {
	    this.clause = clause;
	  }
	  public boolean isApplyAnd() {
	    return this.applyAnd;
	  }
	  public void setApplyAnd(boolean applyAnd) {
	    this.applyAnd = applyAnd;
	  }
}
