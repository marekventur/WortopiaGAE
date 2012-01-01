package de.wortopia.model;

import java.io.Serializable;
import java.util.Date;

public class WordWiki implements Serializable {
	private static final long serialVersionUID = 6108950043253710913L;
	private String text;
	private Date date;
	
	public WordWiki(String text, Date date) {
		super();
		this.text = text;
		this.date = date;
	}
	
	public String getText() {
		return text;
	}
	
	public Date getDate() {
		return date;
	} 
	 
}
