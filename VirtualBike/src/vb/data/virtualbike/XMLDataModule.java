package vb.data.virtualbike;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class XMLDataModule implements IDataModule {

	// Book book = null;
	public XMLDataModule() {
		// TODO 自动生成的构造函数存根

	}

	@Override
	public void WriteData(String config) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void ReadData(String config) {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean CheckExist(String arg0) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public Map<String, List<String>> ReadData(InputStream is) {
		// TODO 自动生成的方法存根
		Map<String, List<String>> res = new HashMap<String, List<String>>();
		String province= null;
		List<String> cities= null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		try {
			while (parser.nextTag() != XmlPullParser.END_DOCUMENT) {
				parser.require(XmlPullParser.START_TAG, null, "province");
				parser.nextTag();
				province = parser.nextText();
				cities= new ArrayList<String>();
				Log.v("tag", parser.getName() + "=" + province);
					while (parser.nextTag() != XmlPullParser.END_TAG&&!"province".equals(parser.getName())) {
						parser.require(XmlPullParser.START_TAG, null, "city");
						String cityinfoString="";
						for (int i = 0; i < 3; i++) {
							parser.nextTag();
							cityinfoString = cityinfoString + parser.getName() + "=" + parser.nextText();
							Log.v("tag",cityinfoString);
						}
						cities.add(cityinfoString);
						parser.nextTag();
					}
					res.put(province, cities);
			}
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		Log.v("res", res.toString());
		return res;
	}
}
