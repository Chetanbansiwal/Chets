package com.example.tagbintrials;

public class CustomRes {
	private String imgurl;
	private String rbname;
	private float rating;
	private String deal;
	
	public CustomRes(String imgurl, String rbname, float rating, String deal) {
		this.imgurl = imgurl;
		this.rbname = rbname;
		this.rating = rating;
		this.deal = deal;
	}
	
	public String getName() {
		return rbname; 
	}
	
	public float getRating() {
		return rating;
	}
	
	public String getImage() {
		return imgurl;
	}
	
	public String getDeal() {
		return deal;
	}

}
