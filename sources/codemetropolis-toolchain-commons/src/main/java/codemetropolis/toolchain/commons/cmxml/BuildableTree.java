package codemetropolis.toolchain.commons.cmxml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.commons.cmxml.exceptions.CmxmlReaderException;
import codemetropolis.toolchain.commons.cmxml.exceptions.CmxmlWriterException;
import codemetropolis.toolchain.commons.util.FileUtils;
import codemetropolis.toolchain.commons.util.Resources;

public class BuildableTree {
	
	public class Iterator {

		private List<Buildable> temp = new ArrayList<Buildable>();
		private int index = 0;
		
		Iterator() {
			temp.add(root);
		}
		
		public int getIndex() {
			return index;
		}
		
		public boolean hasNext() {
			return !temp.isEmpty();
		}

		public Buildable next() {
			Buildable next = temp.remove(0);
			temp.addAll(0, Arrays.asList(next.getChildren()));
			++index;
			return next;
		}
		
	}
	
	private Buildable root;
	
	public BuildableTree() {
		this(null);
	}
	
	public BuildableTree(Buildable root) {
		this.root = root;
	}
	
	public Buildable getRoot() {
		return root;
	}
	
	public Iterator iterator() {
		return new Iterator();
	}
	
	public int size() {
		return getBuildables().size();
	}
	
	public List<Buildable> getBuildables() {
		List<Buildable> buildables = new ArrayList<>();
		buildables.add(root);
		buildables.addAll(root.getDescendants());
		return buildables;
	}
	
	public Buildable getBuildable(String id) {
		return getBuildableFromDescendants(root, id);
	}
	
	private Buildable getBuildableFromDescendants(Buildable b, String id) {
		if(b.getId().equals(id)) return b;
		for(Buildable c : b.getChildren())
			if(getBuildableFromDescendants(c, id) != null) return getBuildableFromDescendants(c, id);
		return null;
	}
	
	public Buildable[] findBuildables(int x, int z) {
		List<Buildable> result = new ArrayList<Buildable>();
		Iterator it = iterator();
		while(it.hasNext()) {
			Buildable b = it.next();
			if(b.isOverlapping(x, z)) {
				result.add(b);
			}
		}
		return result.toArray(new Buildable[result.size()]);
	}
	
	public void loadFromFile(String path) throws CmxmlReaderException {
		try {
			root = null;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();;
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File xmlFile = new File(path);
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("buildable");
	 
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
					Element eElement = (Element) nNode;
					
					Type type = null;
					switch(eElement.getAttribute("type")) {
						case "ground": type = Type.GROUND;
						break;
						case "garden": type = Type.GARDEN;
						break;
						case "floor": type = Type.FLOOR;
						break;
						case "cellar": type = Type.CELLAR;
						break;
						case "container": type = Type.CONTAINER;
						break;
					}
					
					Point position;
					if(eElement.getElementsByTagName("position").item(0) != null && eElement.getElementsByTagName("position").item(0).getParentNode() == nNode) {
						position = new Point(
								Integer.parseInt(((Element)eElement.getElementsByTagName("position").item(0)).getAttribute("x")),
								Integer.parseInt(((Element)eElement.getElementsByTagName("position").item(0)).getAttribute("y")),
								Integer.parseInt(((Element)eElement.getElementsByTagName("position").item(0)).getAttribute("z"))
								);
					} else {
						position = new Point();
					}
			
					Point size;



					if(eElement.getElementsByTagName("size").item(0) != null && eElement.getElementsByTagName("size").item(0).getParentNode() == nNode) {
						size = new Point(
								Integer.parseInt(((Element) eElement.getElementsByTagName("size").item(0)).getAttribute("x")),
								Integer.parseInt(((Element) eElement.getElementsByTagName("size").item(0)).getAttribute("y")),
								Integer.parseInt(((Element) eElement.getElementsByTagName("size").item(0)).getAttribute("z"))
						);



					}
					 else {
						size = new Point();

					}
					
					Buildable b = new Buildable(
							eElement.getAttribute("id"),
							eElement.getAttribute("name"),
							type,
							position,
							size
							);


					NodeList attributeNodes = eElement.getElementsByTagName("attributes").item(0).getChildNodes();
					
					for(int i = 1; attributeNodes.item(i) != null; i += 2) {
						b.addAttribute(
								((Element)attributeNodes.item(i)).getAttribute("name"),
								((Element)attributeNodes.item(i)).getAttribute("value")
						);

					}
					
					if(!nNode.getParentNode().getNodeName().equals("buildables")) {
						// Adds current buildable to parent's "children" list 
						Element parentElement = (Element)nNode.getParentNode().getParentNode();
						getBuildable(parentElement.getAttribute("id")).addChild(b);
					} else {
						root = b;
					}
				}
			}
		} catch(Exception e) {
			throw new CmxmlReaderException(e);
		}
	}
	
	public void writeToFile(String filePath, String from, String to, String version) throws CmxmlWriterException {
		writeToFile(filePath, from, to, version, true);
	}
	
	public void writeToFile(String filePath, String from, String to, String version, boolean recursive) throws CmxmlWriterException  {
		try {
			FileUtils.createContainingDirs(filePath);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("buildables");
			rootElement.setAttribute("from", from);
			rootElement.setAttribute("to", to);
			rootElement.setAttribute("version", version);
			doc.appendChild(rootElement);
			
			if(recursive) {
				rootElement.appendChild(root.toXmlElement(doc, true));
			} else {
				Iterator it = iterator();
				while(it.hasNext()) {
					Buildable b = it.next();
					rootElement.appendChild(b.toXmlElement(doc, false));
				}
				
				Iterator it2 = iterator();
				while(it2.hasNext()) {
					Buildable b = it2.next();
					Buildable parent = b.getParent();
					if(parent != null) {
						Element parentElement = doc.getElementById(parent.getId());
						Element buildableElement = doc.getElementById(b.getId());
						parentElement.getElementsByTagName("children").item(0).appendChild(buildableElement);
					}
				}
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(source, result);
		} catch ( Exception e) {
			throw new CmxmlWriterException(Resources.get("cmxml_writer_error"), e);
		}
	}
	
	@Override
	public String toString() {
		return "<buildables>\n"
				+ root.toString(1, false)
				+ "</buildables>";
	}

	public String toEscapedString(String from, String to, String version) {
		String str = "<buildables from=\"" + from + "\" to=\"" + to + "\" version=\"" + "1.0" + "\">\n"
				+ root.toString(1, true)
				+ "</buildables>";
		return str;
	}
	
}
