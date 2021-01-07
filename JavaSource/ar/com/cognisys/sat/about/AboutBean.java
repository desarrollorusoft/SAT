package ar.com.cognisys.sat.about;

import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@ManagedBean
@ViewScoped
public class AboutBean implements Serializable {

	private static final long serialVersionUID = -8161377038860028874L;
	private HashMap<String, SvnInfo> mapaDatos;
	private SvnInfo info;

	public AboutBean() {
		Gson gson = new GsonBuilder().create();
		InputStreamReader reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("repository.data"));
		mapaDatos = new HashMap<String, SvnInfo>();
		info = gson.fromJson(reader, SvnInfo.class);
	}

	public ArrayList<String> getKeys(Properties prop) {
		ArrayList<String> keys = new ArrayList<String>();

		for (Object o : prop.keySet())
			keys.add((String) o);

		return keys;
	}
	
	public List<String> getWS() {
		return new ArrayList<String>(mapaDatos.keySet());
	}

	public HashMap<String, SvnInfo> getMapaDatos() {
		return mapaDatos;
	}

	public void setMapaDatos(HashMap<String, SvnInfo> mapaDatos) {
		this.mapaDatos = mapaDatos;
	}

	public SvnInfo getInfo() {
		return info;
	}

	public void setInfo(SvnInfo info) {
		this.info = info;
	}
}