package com.unilog.socialmedia.service;

import com.google.gson.annotations.Expose;



public abstract class ResponseStatusSocialMedia {
	@Expose protected StatusSocialMedia status;

	public StatusSocialMedia getStatus() {
		return status;
	}

	public void setStatus(StatusSocialMedia status) {
		this.status = status;
	}
}
