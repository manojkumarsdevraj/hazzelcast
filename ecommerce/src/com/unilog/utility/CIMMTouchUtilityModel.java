package com.unilog.utility;

public class CIMMTouchUtilityModel {
		private String blogId;
		private String blogEntryId;
		private boolean blogEntryAvailable;
		
		public void setBlogId(String blogId) {
			this.blogId = blogId;
		}
		public String getBlogId() {
			return blogId;
		}
		public void setBlogEntryId(String blogEntryId) {
			this.blogEntryId = blogEntryId;
		}
		public String getBlogEntryId() {
			return blogEntryId;
		}
		public void setBlogEntryAvailable(boolean blogEntryAvailable) {
			this.blogEntryAvailable = blogEntryAvailable;
		}
		public boolean isBlogEntryAvailable() {
			return blogEntryAvailable;
		}
}
