package vb.model.virtualbike;

import java.util.ArrayList;

public class City {
	private String id;
	private String name;
	private ArrayList<Station> slist;
	private static City city = null;

	private City() {
		
	}

	public static City getInstance() {
		if (city == null) {
			city = new City();
		}
		return city;
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

	public ArrayList<Station> getSlist() {
		return slist;
	}

	public void setSlist(ArrayList<Station> slist) {
		this.slist = slist;
	}

	public void Initlization(String cityid){
		
	}

}
