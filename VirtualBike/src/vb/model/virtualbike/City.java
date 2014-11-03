package vb.model.virtualbike;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import vb.context.virtualbike.BikeContext;
import vb.helpers.virtualbike.HttpRequestHelper;
import vb.helpers.virtualbike.JsonParserHelper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class City {
	private String id;
	private String name;
	private ArrayList<Station> _slist;
	private static City city = null;
	//�����write2db��������Ҫ�Ƕ���ĳЩ������У������󷵻ص����ݵĲ�ͬ���ж��Ƿ���Ҫд�����ݿ�ı�Ҫ��
	public Boolean dnotwrite2db =null;
	private BikeContext _bikecontext = null;
	String cityurl = null;
	public String getCityurl() {
		return cityurl;
	}

	public void setCityurl(String cityurl) {
		this.cityurl = cityurl;
	}

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

	public void Initlization(final Handler handler,String id, final String url) {
		// pre load data from url: check if exists in db by id

		// new thread to load data from url
		cityurl = url;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				Message msg = new Message();
				HttpRequestHelper hr = new HttpRequestHelper(url);
				try {
					String dataString =null ;
					JsonParserHelper jsonph = new JsonParserHelper();
					dataString = hr.execute().split("=")[1];
					Log.v("jsonstring",dataString);
					dnotwrite2db = dataString.contains("availBike");
					if (dnotwrite2db) {
						_slist = jsonph.parser2list_with_validbike(dataString);
					}
					else {
						_slist = jsonph.parser2list_without_validbike(dataString);
					}
					msg.what = 1;//���ݴ��ݽ���
					handler.sendMessage(msg);
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

	public void SetContext(BikeContext context) {
		// TODO �Զ����ɵķ������
		_bikecontext = context;
	}
	public BikeContext GetContext() {
		// TODO �Զ����ɵķ������
		return _bikecontext;
	}

}
