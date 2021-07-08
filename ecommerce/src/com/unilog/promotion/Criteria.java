package com.unilog.promotion;

public class Criteria {
	  private String name;
	  private String value;
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
