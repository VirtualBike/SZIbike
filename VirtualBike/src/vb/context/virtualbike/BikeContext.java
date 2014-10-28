package vb.context.virtualbike;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import vb.data.virtualbike.IDataModule;
import vb.model.virtualbike.City;
import vb.model.virtualbike.Station;

import android.R.integer;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class BikeContext {
	public ArrayList<Station> slist;
	public Map<String, List<String>> res;
	private City _city = null;
	private IDataModule _datamodule = null ;
	private SortedSet<Station> _sortedstationSet =null;

	
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
	public ArrayList<LatLng> SearchStation(LatLng location,int searchcounts) {
		slist = _city.getSlist();
		ArrayList<LatLng> listlocs = new ArrayList<LatLng>();
		_sortedstationSet = new TreeSet<Station>(new Comparator<Station>() {
			@Override
			public int compare(Station arg0, Station arg1) {
				// TODO 自动生成的方法存根
				int result = arg0.getDistence_info()>arg1.getDistence_info()? 1 : 0;
				return result;
			}
		});  
		for(Station station : slist)
		{
			double lat = Double.valueOf(station.getLat());
			double lng = Double.valueOf(station.getLon());
			LatLng stationloc = new LatLng(lat, lng);
			station.setDistence_info(DistanceUtil.getDistance(location, stationloc));
			station.locobjObject= stationloc;
			_sortedstationSet.add(station);
		}
		for (int i = 0; i < searchcounts; i++) {
			listlocs.add((LatLng) ((Station)_sortedstationSet.toArray()[i]).locobjObject);
		}
		return listlocs;
	}
	
	
}
