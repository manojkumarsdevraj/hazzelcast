package com.unilog.ecomm.model;


public class Coupon {
	
	private String copounCode;
	private int totalUses = 1;
	private int timesUsed;
	private boolean valid;
	private String description;
	private String lable;
	private CouponCategory category = CouponCategory.AUTO_APPLY;
	private Status status = Status.ACTIVE;
	private boolean stackable;
	private String stackMates;
	private String errorMessage;

	public String getCopounCode() {
		return copounCode;
	}

	public void setCopounCode(String copounCode) {
		this.copounCode = copounCode;
	}
	
	public int getTotalUses() {
		return totalUses;
	}
	public void setTotalUses(int totalUses) {
		this.totalUses = totalUses;
	}
	public int getTimesUsed() {
		return timesUsed;
	}
	public void setTimesUsed(int timesUsed) {
		this.timesUsed = timesUsed;
	}
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public CouponCategory getCategory() {
		return category;
	}

	public void setCategory(CouponCategory category) {
		this.category = category;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public boolean isStackable() {
		return stackable;
	}

	public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}

	public String getStackMates() {
		return stackMates;
	}

	public void setStackMates(String stackMates) {
		this.stackMates = stackMates;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "Coupon [copounCode=" + copounCode + ", totalUses=" + totalUses
				+ ", timesUsed=" + timesUsed + ", valid=" + valid
				+ ", description=" + description + ", lable=" + lable
				+ ", category=" + category + ", status=" + status
				+ ", stackable=" + stackable + ", stackMates=" + stackMates
				+ ", errorMessage=" + errorMessage + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((copounCode == null) ? 0 : copounCode.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((lable == null) ? 0 : lable.hashCode());
		result = prime * result
				+ ((stackMates == null) ? 0 : stackMates.hashCode());
		result = prime * result + (stackable ? 1231 : 1237);
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + totalUses;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coupon other = (Coupon) obj;
		if (category != other.category)
			return false;
		if (copounCode == null) {
			if (other.copounCode != null)
				return false;
		} else if (!copounCode.equals(other.copounCode))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (lable == null) {
			if (other.lable != null)
				return false;
		} else if (!lable.equals(other.lable))
			return false;
		if (stackMates == null) {
			if (other.stackMates != null)
				return false;
		} else if (!stackMates.equals(other.stackMates))
			return false;
		if (stackable != other.stackable)
			return false;
		if (status != other.status)
			return false;
		if (totalUses != other.totalUses)
			return false;
		return true;
	}

	

}
