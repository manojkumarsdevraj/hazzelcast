package com.unilog.datasmart.model;

public class HotSpot {
	private String Tag;
	private Coordinates TopLeft;
	private Coordinates BottomRight;
	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public Coordinates getTopLeft() {
		return TopLeft;
	}
	public void setTopLeft(Coordinates topLeft) {
		TopLeft = topLeft;
	}
	public Coordinates getBottomRight() {
		return BottomRight;
	}
	public void setBottomRight(Coordinates bottomRight) {
		BottomRight = bottomRight;
	}

}
