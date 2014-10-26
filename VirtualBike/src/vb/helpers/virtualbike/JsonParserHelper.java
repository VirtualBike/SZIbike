package vb.helpers.virtualbike;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;


import vb.model.virtualbike.Station;

public class JsonParserHelper {

public ArrayList<Station> parser2list_with_validbike(String json) throws JSONException {
	JSONObject jsonObject = new JSONObject(json);
	JSONObject jsonstation=null;
	ArrayList<Station> stations= new ArrayList<Station>();
	JSONArray jsonArray = jsonObject.getJSONArray("station");
	for (int i = 0; i < jsonArray.length(); i++) {
		 jsonstation = jsonArray.getJSONObject(i);
		 Station st = new Station();
		 st.setId(jsonstation.getString("id"));
		 st.setName(jsonstation.getString("name"));
		 st.setLat(jsonstation.getString("lat"));
		 st.setLon(jsonstation.getString("lng"));
		 st.setAddress(jsonstation.getString("address"));
		 String availbike = jsonstation.getString("availBike");
		 st.setBorrow_info(availbike);
		 int returnbike = Integer.parseInt(jsonstation.getString("capacity"))-Integer.parseInt(availbike);
		 st.setReturn_info(String.valueOf(returnbike));
		 stations.add(st);
	}
	return stations;
	
}
public ArrayList<Station> parser2list_without_validbike(String json) throws JSONException {
	JSONObject jsonObject = new JSONObject(json);
	JSONObject jsonstation=null;
	ArrayList<Station> stations= new ArrayList<Station>();
	JSONArray jsonArray = jsonObject.getJSONArray("station");
	for (int i = 0; i < jsonArray.length(); i++) {
		 jsonstation = jsonArray.getJSONObject(i);
		 Station st = new Station();
		 st.setId(jsonstation.getString("id"));
		 st.setName(jsonstation.getString("name"));
		 st.setLat(jsonstation.getString("lat"));
		 st.setLon(jsonstation.getString("lng"));
		 st.setAddress(jsonstation.getString("address"));
		 stations.add(st);
	}
	return stations;
	
}
}
