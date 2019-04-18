package codemetropolis.toolchain.mapping.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.commons.util.Resources;
import codemetropolis.toolchain.mapping.exceptions.MissingResourceException;
import codemetropolis.toolchain.mapping.exceptions.NotSupportedLinkingException;

@XmlAccessorType(XmlAccessType.FIELD)
public class Linking {

    private static final Map<Type, String[]> SUPPORTED_PROPERTIES = new HashMap<>();

    static {
        SUPPORTED_PROPERTIES.put(Type.FLOOR, new String[]{"width","BuiltMetric1", "height","BuiltMetric2", "length","BuiltMetric3", "character", "external_character", "torches"});
        SUPPORTED_PROPERTIES.put(Type.CELLAR, new String[]{"width", "height", "length", "character", "external_character", "torches"});
        SUPPORTED_PROPERTIES.put(Type.GARDEN, new String[]{"tree-ratio", "mushroom-ratio", "flower-ratio"});
        SUPPORTED_PROPERTIES.put(Type.GROUND, new String[]{});
    }
	
	@XmlAttribute
	private String source;
	
	@XmlAttribute
	private String target;
	
	@XmlElement(name="binding")
	private List<Binding> bindings = new ArrayList<>();

	public Linking() {}

	public Linking(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}

	public List<Binding> getBindings() {
		return Collections.unmodifiableList(bindings);
	}
	
	public void addBinding(Binding binding) {
		bindings.add(binding);
	}
	
	public void removeBinding(Binding binding) {
		bindings.remove(binding);
	}
	
	public static String[] getSupportedProperties(Type buildableType) {
		return SUPPORTED_PROPERTIES.get(buildableType);
	}

	public void validate(List<Constant> resources) throws NotSupportedLinkingException, MissingResourceException {
		Type type;
		try {
			type = Type.valueOf(target.toUpperCase());
		} catch(IllegalArgumentException e) {
			throw new NotSupportedLinkingException(String.format(Resources.get("invalid_linking_target_error"), target));
		}
		String[] validTargetProps = SUPPORTED_PROPERTIES.get(type);
		for(Binding b : bindings) {
			validateBindingResource(b, resources);
			boolean isValid = false;
			for(String prop : validTargetProps) {
				if(prop.equalsIgnoreCase(b.getTo())) {
					isValid = true;
					break;
				}
			}
			if(!isValid) {
				throw new NotSupportedLinkingException(String.format(Resources.get("invalid_linking_error"), source, b.getFrom(), target, b.getTo()));
			}
		}
	}
	
	private void validateBindingResource(Binding b, List<Constant> resources) throws MissingResourceException {
		String variableId = b.getVariableId();
		if(variableId != null) {
			boolean isValid = false;
			for(Constant res : resources) {
				if(res.getId().equals(variableId)) {
					isValid = true;
					break;
				}
			}
			if(!isValid) {
				throw new MissingResourceException(String.format(Resources.get("missing_resource_error"), variableId));
			}
		}
	}
	
}
