package codemetropolis.toolchain.converter.pmd;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import codemetropolis.toolchain.commons.cdf.CdfElement;
import codemetropolis.toolchain.commons.cdf.CdfTree;
import codemetropolis.toolchain.commons.cdf.converter.CdfConverter;
import codemetropolis.toolchain.commons.exceptions.CodeMetropolisException;
import codemetropolis.toolchain.commons.cdf.CdfProperty;

public class PmdConverter extends CdfConverter {

	int vCounter = 0;

	public PmdConverter(Map<String, String> params) {
		super(params);
	}

	@Override
	public CdfTree createElements(String source) throws CodeMetropolisException {

		vCounter = 0;
		Document doc = loadXml(source);
		CdfElement root = createElementsRecursively(doc);
		CdfTree tree = new CdfTree();
		tree.setRoot(root);
		return tree;
	}

	public CdfElement createElementsRecursively(Node root) {
		CdfElement element = null;
		element = cdfConvert(root);
		if (element != null) {
			addProperties(element, root);
			NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				CdfElement e = createElementsRecursively(children.item(i));
				if (e != null) {
					element.addChildElement(e);
				}
			}
		}
		return element;

	}

	public void addProperties(CdfElement elem, Node n) {
		if (n.hasAttributes()) {
			NamedNodeMap map = n.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {

				elem.addProperty(map.item(i).getNodeName(), map.item(i).getNodeValue(),
						convertType(map.item(i).getNodeValue()));
			}
		}
	}

	public CdfElement cdfConvert(Node n) {
		CdfElement elem = null;
		switch (n.getNodeName()) {
		case "violation":
			elem = new CdfElement(("violation" + vCounter), "violation");
			vCounter++;
			break;
		case "file":
			elem = new CdfElement(n.getAttributes().getNamedItem("name").getNodeValue(), "file");
			break;
		case "#document":
		case "pmd":
			elem = new CdfElement(n.getNodeName(), "other");
			break;
		default:
			
			break;
		}

		return elem;

	}

	public CdfProperty.Type convertType(String str) {

		if (isNumeric(str))
			return CdfProperty.Type.INT;
		else
			return CdfProperty.Type.STRING;

	}

	public boolean isNumeric(String str) {
		if (str.chars().allMatch(Character::isDigit))
			return true;
		else
			return false;

	}

	public Document parseXml(String source, DocumentBuilder dBuilder) {
		File xmlFile = new File(source);
		Document doc = null;
		try {
			doc = dBuilder.parse(xmlFile);
		} catch (SAXException e) {
			System.out.println("Parsing xml input has failed");
		} catch (IOException e) {

			System.out.println("Reading xml input has failed");
		}
		return doc;
	}

	public Document loadXml(String source) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		;
		DocumentBuilder dBuilder = null;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("Parse Configuration has failed");
		}
		Document doc = parseXml(source, dBuilder);
		return doc;
	}
}
