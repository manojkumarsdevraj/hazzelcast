package com.unilog.model;

public class CriteriaIntValue {

	  private String name;
	  private int value;
	  private String clause;
	  private boolean applyAnd;

	  public String getName()
	  {
	    return this.name;
	  }
	  public void setName(String name) {
	    this.name = name;
	  }
	 
	  public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
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
