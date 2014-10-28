package vb.model.virtualbike;

import java.util.ArrayList;

public class Station {
	private String id;
	private String name;
	private String address;
	private String lat;
	private String lon;
	private String borrow_info;
	private String return_info;
	private Double distence_info;
	public Object locobjObject;
	
	public Station () {
		
	}


	public Double getDistence_info() {
		return distence_info;
	}


	public void setDistence_info(Double distence_info) {
		this.distence_info = distence_info;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getLat() {
		return lat;
	}


	public void setLat(String lat) {
		this.lat = lat;
	}


	public String getLon() {
		return lon;
	}


	public void setLon(String lon) {
		this.lon = lon;
	}


	public String getBorrow_info() {
		return borrow_info;
	}


	public void setBorrow_info(String borrow_info) {
		this.borrow_info = borrow_info;
	}


	public String getReturn_info() {
		return return_info;
	}


	public void setReturn_info(String return_info) {
		this.return_info = return_info;
	}
}
