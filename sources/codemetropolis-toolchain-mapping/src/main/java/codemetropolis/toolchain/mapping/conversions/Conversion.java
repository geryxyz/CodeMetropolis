package codemetropolis.toolchain.mapping.conversions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import codemetropolis.toolchain.mapping.model.Limit;
import codemetropolis.toolchain.mapping.model.Parameter;

@XmlJavaTypeAdapter(ConversionAdapter.class)
public abstract class Conversion {
	
	protected List<Parameter> parameters = new ArrayList<Parameter>();
	
	public abstract Object apply(Object value, Limit limit);
	
	public void clearParameters() {
		parameters.clear();
	}
	
	public void addParameter(Parameter p) {
		parameters.add(p);
	}
	
	public void addParameters(Parameter... parameters) {
		for(Parameter p : parameters) {
			this.parameters.add(p);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Parameter> getParameters() {
		if(parameters == null) {
			parameters = Collections.EMPTY_LIST;
		}
		return Collections.unmodifiableList(parameters);
	}
	
	public static Conversion createFromName(String name) {	
		switch(name.toLowerCase()) {
			case ToIntConversion.NAME:
				return new ToIntConversion();
			case ToDoubleConversion.NAME:
				return new ToDoubleConversion();
			case MultiplyConversion.NAME:
				return new MultiplyConversion();
			case QuantizationConversion.NAME:
				return new QuantizationConversion();
			case NormalizeConversion.NAME:
				return new NormalizeConversion();
			case SwitchConversion.NAME:
				return new SwitchConversion();
		}
		return null;
	}
	
	//region conversion helpers
	public static double toDouble(Object value) {
		if(value instanceof String) {
			return Double.parseDouble((String)value);
		}
		return (double)value;
	}
	
	public static int toInt(Object value) {
		return (int)toDouble(value);
	}
	//endregion
		
}
