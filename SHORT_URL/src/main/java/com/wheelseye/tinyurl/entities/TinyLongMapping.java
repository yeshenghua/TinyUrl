package com.wheelseye.tinyurl.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class TinyLongMapping {
	@Id
	@GeneratedValue
	private Long Id;
	
	private String tinyURL;
	private String longURL;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getTinyURL() {
		return tinyURL;
	}
	public void setTinyURL(String tinyURL) {
		this.tinyURL = tinyURL;
	}
	public String getLongURL() {
		return longURL;
	}
	public void setLongURL(String longURL) {
		this.longURL = longURL;
	}
}
