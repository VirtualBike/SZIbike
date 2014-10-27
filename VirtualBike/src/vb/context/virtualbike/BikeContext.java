package vb.context.virtualbike;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;

import vb.data.virtualbike.IDataModule;
import vb.model.virtualbike.City;
import vb.model.virtualbike.Station;

public class BikeContext {
	public ArrayList<Station> slist;
	public Map<String, List<String>> res;
	public City _city = null;
	private IDataModule _datamodule = null ;

	
	public BikeContext(IDataModule datamodule){
		_datamodule = datamodule;
		_city = City.getInstance(); 
	}
	public BikeContext(){
		_city = City.getInstance(); 
	}
	/*
	 * function for read city xml*/
	public Map<String, List<String>> ReadXMLData(InputStream is) {
		return _datamodule.ReadData(is) ;
	}
	
	public ArrayList<Station> ReadDBData() {
		_datamodule.ReadData("");
		return null ;
		
	}
	public boolean CheckExist(String arg0) {
		return false;
		
	}
	
	
}
