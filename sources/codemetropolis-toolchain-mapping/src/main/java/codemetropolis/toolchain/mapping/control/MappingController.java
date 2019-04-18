package codemetropolis.toolchain.mapping.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import codemetropolis.toolchain.commons.cdf.exceptions.CdfReaderException;
import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.commons.cmxml.BuildableTree;
import codemetropolis.toolchain.commons.cmxml.BuildableTree.Iterator;
import codemetropolis.toolchain.mapping.conversions.Conversion;
import codemetropolis.toolchain.mapping.exceptions.NotValidBuildableStructure;
import codemetropolis.toolchain.mapping.model.Binding;
import codemetropolis.toolchain.mapping.model.Limit;
import codemetropolis.toolchain.mapping.model.Linking;
import codemetropolis.toolchain.mapping.model.Mapping;

public class MappingController {
	
	private static final int MIN_SIZE = 9;
	
	private final LimitController limitController = new LimitController();
	private Map<Buildable, Map<String, String>> attributesByBuildables = new HashMap<>();
	private double scale;
	private boolean skipInvalidStructures;
	private Stack<Buildable> buildableStack = new Stack<>();
	private Mapping mapping;
	protected int metricValue;
	public MappingController(Mapping mapping) {
		this(mapping, 1.0, false);
	}
	
	public MappingController(Mapping mapping, double scale, boolean skipInvalidStructures) {
		this.mapping = mapping;
		this.scale = scale;
		this.skipInvalidStructures = skipInvalidStructures;
	}
	
	public void createBuildablesFromCdf(String filename) throws CdfReaderException {
		attributesByBuildables.clear();
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();;
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File xmlFile = new File(filename);
			if(!xmlFile.exists()) {
				throw new FileNotFoundException();
			}
			Document doc = dBuilder.parse(xmlFile);
			
			doc.getDocumentElement().normalize();
			Element rootElement = (Element) doc.getChildNodes().item(0);
			Buildable container = new Buildable(UUID.randomUUID().toString(), "", Type.CONTAINER);
				
			Buildable actualBuildable = createBuildable(rootElement);
			if(actualBuildable == null){
				attributesByBuildables.put(container, new HashMap<>());
				setChildren(container, rootElement);
			} else {
				setAttributes(actualBuildable, rootElement);
				setChildren(actualBuildable, rootElement);	
				Buildable root = findRoot(attributesByBuildables.keySet());
				container.addChild(root);
				attributesByBuildables.put(container, new HashMap<>());
			}
		} catch (Exception e){
			throw new CdfReaderException(e);
		}
	}
	
	public BuildableTree linkBuildablesToMetrics() {
		
		List<Linking> linkings = mapping.getLinkings();
		Map<String, String> resources = mapping.getResourceMap();
		
		for(Map.Entry<Buildable, Map<String, String>> entry : attributesByBuildables.entrySet()) {
			
			Buildable b = entry.getKey();



			Map<String, String> attributes = entry.getValue();
			
			Linking linking = null;
			for(Linking l : linkings) {
				if(l.getTarget().equalsIgnoreCase(b.getType().toString())) {
					linking = l;
					break;
				}
			}
			
			if(linking == null) {
				continue;
			}
				
			for(Binding binding : linking.getBindings()) {
				String variableId = binding.getVariableId();
				if(variableId != null) {
					String resource = resources.get(variableId);
					setProperty(b, binding.getTo(), resource, false);
					continue;
				}
				
				Object value = attributes.get(binding.getFrom());
				if(value == null) {
					String defaultValue = binding.getDefaultValue();
					if(defaultValue == null || defaultValue.isEmpty()) continue;
					setProperty(b, binding.getTo(), defaultValue, true);
					continue;
				}
				
				Limit limit = limitController.getLimit(linking.getSource(), binding.getFrom());
				
				for(Conversion c : binding.getConversions()) {
					value = c.apply(value, limit);
				}
				
				setProperty(b, binding.getTo(), value, true);
			}
			
		}
		
		Buildable root = findRoot(attributesByBuildables.keySet());
		BuildableTree buildableTree = new BuildableTree(root);
		prepareBuildables(buildableTree);
		return buildableTree;
	}
	
	private void setProperty(Buildable b, String propertyName, Object value, boolean adjustSize) {
		
		 
		
		switch(propertyName) {
		case "height":
		case "width":
		case "length":
		case "BuiltMetric1":
		case "BuiltMetric2":
		case "BuiltMetric3":


			value = Conversion.toInt(value);
			metricValue=(int)value;

			if(adjustSize){ value = Conversion.toInt(MIN_SIZE + (int)value * scale);}
			break;
	}

		
		switch(propertyName) {
			case "BuiltMetric1":
				b.setBuiltMetric1(metricValue);
				b.addAttribute(propertyName, String.valueOf(metricValue));
				break;
			
			case "BuiltMetric2":
				b.setBuiltMetric2(metricValue);
				b.addAttribute(propertyName, String.valueOf(metricValue));
				break;
			case "BuiltMetric3":
				b.setBuiltMetric3(metricValue);
				b.addAttribute(propertyName, String.valueOf(metricValue));
				break;	
			
			case "height":
				b.setSizeY((int)value);
				break;
			case "width":
				b.setSizeX((int)value);

				break;
			case "length":
				b.setSizeZ((int)value);

				break;
			default:
				b.addAttribute(propertyName, String.valueOf(value));
		}	
	}
	
	private void setChildren(Buildable buildable, Element element){
		if(buildableStack.isEmpty()){
			buildable.setCdfNames(buildable.getName());
		} else {
			Buildable top = buildableStack.peek();
			if(buildable.getCdfNames() == null){
				StringBuffer sb = new StringBuffer(top.getCdfNames());
				sb.append(".").append(buildable.getName());
				buildable.setCdfNames(sb.toString());
			}
		}
		buildableStack.add(buildable);
		Node children = element.getChildNodes().item(1);
		NodeList childrenNodes = children.getChildNodes();
		for(int i = 0; i < childrenNodes.getLength(); i++){
			if(childrenNodes.item(i).getNodeType() == Node.ELEMENT_NODE){
				Element childElement = (Element) childrenNodes.item(i);
				Buildable childBuildable = createBuildable(childElement);
				if(childBuildable != null){
					buildableStack.peek().addChild(childBuildable);
					setAttributes(childBuildable, childElement);
					setChildren(childBuildable, childElement);
				} else {
					setChildren(buildable, childElement);
				}
			}
		}
		buildableStack.pop();
		
	}
	
	private void setAttributes(Buildable buildable, Element element){
		Map<String,String> attributes = new HashMap<>();
		Node properties = element.getChildNodes().item(3);
		if(properties.getNodeType() == Node.ELEMENT_NODE){
			Element propertiesElements = (Element) properties;
			attributes.putAll(createAttributeMap(propertiesElements));
			attributes.put("SourceType", element.getAttribute("type"));				
		}
		attributesByBuildables.put(buildable, attributes);
	}
	
	private Buildable createBuildable(Element element) {
		String id = UUID.randomUUID().toString();
		String name = element.getAttribute("name");
		String typeStr = mapping.getTargetTypeOf(element.getAttribute("type"));
		
		if (typeStr == null){			
			return null;
		}
		
		Type type = Type.valueOf(typeStr);
		return new Buildable(id, name, type);
	}
	
	private Map<String, String> createAttributeMap(Element element) {
		
		Map<String, String> attributes = new HashMap<String, String>();
		NodeList propNodes = element.getChildNodes();
		
		for(int i = 0; i< propNodes.getLength(); i++){
			Node propNode = propNodes.item(i);
			if(propNode.getNodeType() == Node.ELEMENT_NODE){
				Element propElement = (Element) propNode;
				Object value;
				Double doubleValue = null;
				switch(propElement.getAttribute("type")) {
					case "string":
						value = propElement.getAttribute("value");
						break;
					case "int":
						doubleValue = Double.valueOf(propElement.getAttribute("value"));
						value = doubleValue;
						break;
					case "float":
						doubleValue = Double.valueOf(propElement.getAttribute("value"));
						value = doubleValue;
						break;
					default:
						value = "";
				}
				
				if(doubleValue != null){
					Element parentElement = (Element)element.getParentNode();
					limitController.add(parentElement.getAttribute("type").toLowerCase(), propElement.getAttribute("name"), doubleValue);				
				}
				String name = propElement.getAttribute("name");
				attributes.put(
						name,
						String.valueOf(value)
						);
			}							
		}
		return attributes;
	}
	
	private Buildable findRoot(Collection<Buildable> buildables) {
		for(Buildable b : buildables)
			if(b.isRoot()) return b;
		return null;
	}
	
	private void prepareBuildables(BuildableTree buildables) {
		Iterator it = buildables.iterator();
		while(it.hasNext()) {
			Buildable b = it.next();



			if(b.getSizeX() % 2 == 0) b.setSizeX(b.getSizeX() + 1);
			if(b.getSizeY() % 2 == 0) b.setSizeY(b.getSizeY() + 1);
			if(b.getSizeZ() % 2 == 0) b.setSizeZ(b.getSizeZ() + 1);
			
			if(b.getSizeX() < MIN_SIZE) b.setSizeX(MIN_SIZE);
			if(b.getSizeY() < MIN_SIZE) b.setSizeY(MIN_SIZE);
			if(b.getSizeZ() < MIN_SIZE) b.setSizeZ(MIN_SIZE);
			
			if(skipInvalidStructures && (b.getType() == Type.FLOOR || b.getType() == Type.CELLAR)) {
				b.clearChildren();
			}
		}
	}
	
	public boolean validateBuildableStructure(BuildableTree buildableTree) throws NotValidBuildableStructure{

		BuildableTree.Iterator iterator = buildableTree.iterator();
		while(iterator.hasNext()){
			Buildable build = iterator.next();
			if(build.getParent() != null){
				Type buildableParentType = build.getParent().getType();
				if(!Type.GARDEN.equals(buildableParentType) && !Type.GROUND.equals(buildableParentType) && !Type.CONTAINER.equals(buildableParentType)){
					throw new NotValidBuildableStructure(build.getCdfNames());
				}
			} else if(!Type.CONTAINER.equals(build.getType())) {
				throw new NotValidBuildableStructure(build.getCdfNames());
			}
			
		}
		return false;
	}
}
