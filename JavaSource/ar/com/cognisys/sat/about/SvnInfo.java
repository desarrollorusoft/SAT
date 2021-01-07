package ar.com.cognisys.sat.about;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

public class SvnInfo implements Serializable {

	private static final long serialVersionUID = -1293853160243779999L;
	
	private ArrayList<Properties> props;

	public ArrayList<Properties> getProperties() {
		return props;
	}

	public void setProperties(ArrayList<Properties> props) {
		this.props = props;
	}
}