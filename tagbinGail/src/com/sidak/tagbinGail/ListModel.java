package com.sidak.tagbinGail;

public class ListModel {
	private String name;
	private String  image;
	private boolean selected;

	public ListModel(String name, String image) {
		this.name = name;
		this.image=image;
		selected = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String  image) {
		this.image = image;
	}
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
