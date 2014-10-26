package vb.model.virtualbike;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import vb.helpers.virtualbike.HttpRequestHelper;
import vb.helpers.virtualbike.JsonParserHelper;

public class City {
	private String id;
	private String name;
	private ArrayList<Station> _slist;
	private static City city = null;
	//�����write2db��������Ҫ�Ƕ���ĳЩ������У������󷵻ص����ݵĲ�ͬ���ж��Ƿ���Ҫд�����ݿ�ı�Ҫ��
	public Boolean dnotwrite2db =null;
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
		return _slist;
	}

	public void setSlist(ArrayList<Station> slist) {
		this._slist = slist;
	}

	public void Initlization(String id, final String url) {
		// pre load data from url: check if exists in db by id

		// new thread to load data from url
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				HttpRequestHelper hr = new HttpRequestHelper(url);
				try {
					String dataString =null ;
					JsonParserHelper jsonph = new JsonParserHelper();
					dataString = hr.execute().split("{")[1];
					
					dnotwrite2db = dataString.contains("availBike");
					if (dnotwrite2db) {
						_slist = jsonph.parser2list_with_validbike(dataString);
					}
					else {
						_slist = jsonph.parser2list_without_validbike(dataString);
					}
					
				} catch (ClientProtocolException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		}).start();
		// if data contains "availBike" do not save to db
		// set data
	}

}
