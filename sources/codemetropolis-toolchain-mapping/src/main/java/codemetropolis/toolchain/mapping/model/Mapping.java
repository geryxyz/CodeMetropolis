package codemetropolis.toolchain.mapping.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import codemetropolis.toolchain.mapping.exceptions.MappingReaderException;
import codemetropolis.toolchain.mapping.exceptions.MappingWriterException;
import codemetropolis.toolchain.mapping.exceptions.MissingResourceException;
import codemetropolis.toolchain.mapping.exceptions.NotSupportedLinkingException;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Mapping {

	@XmlAttribute
	private String version;
	
	@XmlAttribute
	private String id;
	
	@XmlElementWrapper(name="resources")
	@XmlElement(name="constant")
	private List<Constant> resources = new ArrayList<>();
	
	@XmlElement(name="linking")
	private List<Linking> linkings = new ArrayList<>();

	public Mapping() {}

	public Mapping(String version, String id) {
		this.version = version;
		this.id = id;
	}

	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Constant> getResources() {
		return Collections.unmodifiableList(resources);
	}
	
	public void addResource(Constant resource) {
		resources.add(resource);
	}
	
	public Map<String, String> getResourceMap() {
		Map<String, String> result = new HashMap<>();
		for(Constant resource : resources) {
			result.put(resource.getId(), resource.getValue());
		}
		return result;
	}

	public List<Linking> getLinkings() {
		return Collections.unmodifiableList(linkings);
	}
	
	public void addLinking(Linking linking) {
		linkings.add(linking);
	}
	
	public void removeLinking(Linking linking) {
		linkings.remove(linking);
	}
	
	public String getTargetTypeOf(String sourceType) {
		for(Linking linking : linkings) {
			if(linking.getSource().equalsIgnoreCase(sourceType)) {
				return linking.getTarget().toUpperCase();
			}
		}
		return null;
	}

	public static Mapping readFromXML(String mappingFile) throws MappingReaderException, FileNotFoundException {

		File file = new File(mappingFile);
		if(!file.exists()) {
			throw new FileNotFoundException();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(Mapping.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Mapping mapping = (Mapping) unmarshaller.unmarshal(file);
			return mapping;
		} catch (JAXBException e) {
			throw new MappingReaderException(e);
		}
		
	}
	
	public void writeToXML(String mappingFile) throws MappingWriterException {

		try {
			File file = new File(mappingFile);
			JAXBContext context = JAXBContext.newInstance(Mapping.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(this, file);
		} catch (JAXBException e) {
			throw new MappingWriterException(e);
		}
		
	}
	
	public void validate() throws NotSupportedLinkingException, MissingResourceException {
		for(Linking linking : linkings) {
			linking.validate(resources);
		}
	}
	
}
