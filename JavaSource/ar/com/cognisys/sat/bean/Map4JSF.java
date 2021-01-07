package ar.com.cognisys.sat.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Map4JSF implements Serializable {
	
	private static final long serialVersionUID = 816233341515450654L;
	private Map<String, String> mapa;
	private List<ObjMap4JSF> lista;
	
	public Map4JSF(Map<String, String> mapa) {
		this.setMapa(mapa);
		this.generarLista();
	}
	
	private void generarLista() {
		this.setLista(new ArrayList<ObjMap4JSF>());
		for (Entry<String, String> obj : this.getMapa().entrySet())
			this.getLista().add(new ObjMap4JSF(obj.getKey(), obj.getValue() == null ? "-":obj.getValue()));
	}

	public List<ObjMap4JSF> getLista() {
		return lista;
	}
	
	public void setLista(List<ObjMap4JSF> lista) {
		this.lista = lista;
	}

	private Map<String, String> getMapa() {
		return mapa;
	}

	private void setMapa(Map<String, String> mapa) {
		this.mapa = mapa;
	}
}