package vb.data.virtualbike;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IDataModule {
	public void WriteData(String config);
	public void ReadData(String config);
	public Map<String, List<String>> ReadData(InputStream is);
	public boolean CheckExist(String arg0); 
}
