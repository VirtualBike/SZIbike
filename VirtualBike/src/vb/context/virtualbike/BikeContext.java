package vb.context.virtualbike;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import vb.data.virtualbike.IDataModule;
import vb.model.virtualbike.City;
import vb.model.virtualbike.Station;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.mapapi.utils.DistanceUtil;

public class BikeContext implements Serializable {
	public List<Station> slist;
	public Map<String, List<String>> res;
	private City _city = null;
	private IDataModule _datamodule = null ;
	private SortedSet<Station> _sortedstationSet =null;
	public List<Station> gPSLocateStations = null;
	
	
	public BikeContext(){
		_city = City.getInstance(); 
		slist = new ArrayList<Station>();
		gPSLocateStations = new ArrayList<Station>();
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
				double arg0distense = arg0.getDistence_info();
				double arg1distense = arg1.getDistence_info();
				int result = (arg0distense>arg1distense? 1 :(arg0distense==arg1distense?0:-1));
				return result;
			}
		});  
		for(Station station : slist)
		{
			double lat = Double.valueOf(station.getLat());
			double lng = Double.valueOf(station.getLon());
			LatLng stationloc = new LatLng(lat, lng);
			CoordinateConverter converter  = new CoordinateConverter();  
			converter.from(CoordType.COMMON);  
			// sourceLatLng待转换坐标  
			converter.coord(stationloc);  
			LatLng desLatLng = converter.convert();  
			station.setDistence_info(DistanceUtil.getDistance(location, desLatLng));
			station.locobjObject= desLatLng;
			_sortedstationSet.add(station);
		}
		for (Station station : _sortedstationSet) {
			Log.v("distense",station.getName()+"-"+station.getId()+"-"+station.getDistence_info().toString());
		}
		gPSLocateStations.clear();
		for (int i = 0; i < searchcounts; i++) {
			Station ss= (Station) _sortedstationSet.toArray()[i];
			listlocs.add((LatLng)(ss.locobjObject));
			gPSLocateStations.add(ss);
		}
		return listlocs;
	}
	public void SearchStation(String location) {
		slist = _city.getSlist();
		gPSLocateStations.clear();
		for(Station station : slist)
		{
			if (station.getName().contains(location)||station.getAddress().contains(location)) {
				gPSLocateStations.add(station);
			}
		}
	}

	public void SetDataModuleStratege(IDataModule datamodulestratege) {
		// TODO 自动生成的方法存根
		_datamodule = datamodulestratege;
	}
}
