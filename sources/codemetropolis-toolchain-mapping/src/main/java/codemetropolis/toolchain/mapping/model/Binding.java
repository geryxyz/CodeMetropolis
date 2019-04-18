package codemetropolis.toolchain.mapping.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import codemetropolis.toolchain.mapping.conversions.Conversion;

@XmlAccessorType(XmlAccessType.FIELD)
public class Binding {
	
	@XmlAttribute
	private String from;
	
	@XmlAttribute
	private String to;
	
	@XmlAttribute(name="default")
	private String defaultValue;
	
	@XmlElementWrapper(name="conversions")
	@XmlElement(name="conversion")
	private List<Conversion> conversions = new ArrayList<>();
	
	public Binding() {}

	public Binding(String from, String to) {
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getVariableId() {
		String pattern = "^\\$\\{.*\\}$";
		if(from.matches(pattern)) {
			return from.substring(2, from.length() - 1);
		}
		return null;
	}

	public List<Conversion> getConversions() {
		return Collections.unmodifiableList(conversions);
	}
	
	public void addConversion(Conversion conversion) {
		conversions.add(conversion);
	}
	
	public void clearConversions() {
		conversions.clear();
	}
	
}
