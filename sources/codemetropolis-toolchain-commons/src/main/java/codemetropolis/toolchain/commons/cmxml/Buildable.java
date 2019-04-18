package codemetropolis.toolchain.commons.cmxml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Buildable implements Comparable<Buildable> {
	
	public enum Type {
		GROUND,
		GARDEN,
		FLOOR,
		CELLAR,
		CONTAINER,


	}
	
	private String id;
	private String name;
	private Type type;
	private Point position;
	private Point size;
	private List<Attribute> attributes;
	private List<Buildable> children;
	private Buildable parent;
	private String cdfNames;
	private int BuiltMetric1, BuiltMetric2, BuiltMetric3;
	
	public Buildable(String id, String name, Type type) {
		this(id, name, type, new Point(), new Point());
	}
	
	public Buildable(String id, String name, Type type, Point position, Point size) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.position = position;
		this.size = size;
		this.attributes = new ArrayList<Attribute>();
		this.children = new ArrayList<Buildable>();
	}
	
	public boolean isOverlapping(Buildable b) {
		return 	b.getPositionX() < this.getPositionX() + this.getSizeX() &&
				b.getPositionX() + b.getSizeX() > this.getPositionX() &&
				b.getPositionZ() < this.getPositionZ() + this.getSizeZ() &&
				b.getPositionZ() + b.getSizeZ() > this.getPositionZ();
	}
	
	public boolean isOverlapping(int x1, int z1, int x2, int z2) {
		return	x1 < this.getPositionX() + this.getSizeX() &&
				x2 > this.getPositionX() &&
				z1 < this.getPositionZ() + this.getSizeZ() &&
				z2 > this.getPositionZ();
	}
	
	public boolean isOverlapping(int x, int z) {
		return	x <= this.getPositionX() + this.getSizeX() &&
				x >= this.getPositionX() &&
				z <= this.getPositionZ() + this.getSizeZ() &&
				z >= this.getPositionZ(); 
	}
	
	public Point getCenter() {
		return new Point(
				position.getX() + size.getX() / 2,
				position.getY() + size.getY() / 2,
				position.getZ() + size.getZ() / 2
				);
	}
	
	public String getFullName() {
		return getFullName(".");
	}
	
	public String getFullName(String separator) {
		StringBuilder sb = new StringBuilder();
		Buildable[] ancestors = getAncestors();
		for(int i = 1; i < ancestors.length; i++)
			sb.append(ancestors[i].getName()).append(separator);
		sb.append(getName());
		return sb.toString();
	}
	
	public boolean hasAttribute(String name) {
		for(Attribute a : attributes) {
			if(a.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public String getAttributeValue(String name) {
		for(Attribute a : attributes) {
			if(a.getName().equals(name))
				return a.getValue();
		}
		return null;
	}
	
	public Buildable[] getAncestors() {
		List<Buildable> result = new ArrayList<Buildable>();
		Buildable temp = this;
		while(!temp.isRoot()) {
			result.add(0, temp.getParent());
			temp = temp.getParent();
		}
		return result.toArray(new Buildable[result.size()]);
	}
	
	public List<Buildable> getSiblings() {
		List<Buildable> result = new ArrayList<Buildable>();
		if(!isRoot()) {
			for(Buildable b : parent.getChildren()) {
				if(b != this) {
					result.add(b);
				}
			}
		}
		return result;
	}
	
	public Buildable getLastLeftDescendant() {
		Buildable b = this;
		while(b.getFirstChild() != null) {
			b = b.getFirstChild();
		}	
		return b;
	}
	
	public int getNumberOfDescendants() {
		return getDescendants().size();
	}
	
	public List<Buildable> getDescendants() {
		List<Buildable> result = new ArrayList<Buildable>();
		Stack<Buildable> temp = new Stack<Buildable>();
		temp.push(this);
		while(!temp.isEmpty()) {
			Buildable current = temp.pop();
			if(current.getNumberOfChildren() > 0) {
				for(Buildable child : current.getChildren()) {
					result.add(child);
					temp.push(child);
				}
			}
		}
		return result;
	}
	
	public int getDescendantLevel() {
		if(this.isRoot()) return 1;
		return parent.getDescendantLevel() + 1;
	}
	
	public void addAttribute(Attribute a) {
		attributes.add(a);
	}
	
	public void addAttribute(String name, String value) {
		Attribute a = new Attribute(
				name,
				value
				);
				attributes.add(a);
	}
	
	public void addAttributes(Attribute... attributes) {
		for(Attribute a : attributes) {
			this.attributes.add(a);
		}
	}
	
	public void clearAttributes() {
		attributes.clear();
	}
	
	public void addChild(Buildable b) {
		if(!b.isRoot()) b.parent.children.remove(b);
		children.add(b);
		b.parent = this;
	}
	
	public void addChildren(Buildable... children) {
		for(Buildable b : children) {
			addChild(b);
		}
	}
	
	public void addChildren(Collection<Buildable> children) {
		for(Buildable b : children) {
			addChild(b);
		}
	}
	
	public void clearChildren() {
		for(Buildable c : children) {
			c.parent = null;
		}
		children.clear();
	}
	
	public Buildable getFirstChild() {
		if(!children.isEmpty()) {
			return children.get(0);
		}
		return null;
	}
	
	public Buildable getLastChild() {
		if(!children.isEmpty()) {
			return children.get(children.size()-1);
		}
		return null;
	}
	
	public int getNumberOfChildren() {
		return children.size();
	}
	
	public int getNumberOfAttributes() {
		return attributes.size();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public int getPositionX() {
		return position.getX();
	}
	
	public int getPositionY() {
		return position.getY();
	}
	
	public int getPositionZ() {
		return position.getZ();
	}
	
	public void setPositionX(int x) {
		position.setX(x);
	}
	
	public void setPositionXR(int x) { //Recursive version: also sets the position of children
		for(Buildable b : this.getDescendants()) {
			b.setPositionX(b.getPositionX() - position.getX() + x);
		}
		position.setX(x);
	}
	
	public void setPositionY(int y) {
		position.setY(y);
	}
	
	public void setPositionYR(int y) { //Recursive version: also sets the position of children
		for(Buildable b : this.getDescendants()) {
			b.setPositionY(b.getPositionY() - position.getY() + y);
		}
		position.setY(y);
	}
	
	public void setPositionZ(int z) {
		position.setZ(z);
	}
	
	public void setPositionZR(int z) { //Recursive version: also sets the position of children
		for(Buildable b : this.getDescendants()) {
			b.setPositionZ(b.getPositionZ() - position.getZ() + z);
		}
		position.setZ(z);
	}
	
	public int getSizeX() {
		return size.getX();
	}
	
	public int getSizeY() {
		return size.getY();
	}
	
	public int getSizeZ() {
		return size.getZ();
	}
	
	public void setSizeX(int x) {
		size.setX(x);
	}
	
	public void setSizeY(int y) {
		size.setY(y);
	}
	
	public void setSizeZ(int z) {
		size.setZ(z);
	}

	public int getBuiltMetric1() {
		return BuiltMetric1;
	}

	public void setBuiltMetric1(int builtMetric1) {
		BuiltMetric1 = builtMetric1;
	}

	public int getBuiltMetric2() {
		return BuiltMetric2;
	}

	public void setBuiltMetric2(int builtMetric2) {
		BuiltMetric2 = builtMetric2;
	}

	public int getBuiltMetric3() {
		return BuiltMetric3;
	}

	public void setBuiltMetric3(int builtMetric3) {
		BuiltMetric3 = builtMetric3;
	}

	public Attribute[] getAttributes() {
		return attributes.toArray(new Attribute[attributes.size()]);
	}

	public Buildable[] getChildren() {
		return children.toArray(new Buildable[children.size()]);
	}

	public Buildable getParent() {
		return parent;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public String getId() {
		return id;
	}

	public String getCdfNames() {
		return cdfNames;
	}

	public void setCdfNames(String cdfNames) {
		this.cdfNames = cdfNames;
	}

	@Override
	public int compareTo(Buildable b) {
		return size.getX() * size.getY() * size.getZ() - b.getSizeX() * b.getSizeY() * b.getSizeZ();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Buildable other = (Buildable) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(0, false);
	}
	
	public String toEscapedString() {
		return toString(0, true);
	}
	
	String toString(int indent, boolean escape) {
		String id = this.id;
		String name = this.name;
		if(escape) {
			id = StringEscapeUtils.escapeXml10(id);
			name = StringEscapeUtils.escapeXml10(name);
		}
		
		StringBuilder indentSb = new StringBuilder();
		for(int i = 0; i < indent; i++)
			indentSb.append("\t");
		
		StringBuilder sb = new StringBuilder();
		sb.append(
				indentSb + "<buildable id=\"" + id + "\" name=\"" + name + "\" type=\"" + type.toString().toLowerCase() + "\">\n"
				+ indentSb + "\t<position x=\"" + position.getX() + "\" y=\"" + position.getY() + "\" z=\"" + position.getZ() + "\"/>\n"
				+ indentSb + "\t<size x=\"" + size.getX() + "\" y=\"" + size.getY() + "\" z=\"" + size.getZ() + "\"/>\n"
				+ indentSb + "\t<attributes>\n"
				);
		for(Attribute a : attributes) {
			sb.append(indentSb + "\t\t<attribute name=\"" + a.getName() + "\" value=\"" + (escape ? StringEscapeUtils.escapeXml10(a.getValue()) : a.getValue()) + "\"/>\n");
		}
		sb.append(
				indentSb + "\t</attributes>\n"
				+ indentSb + "\t<children>\n");
		for(Buildable c : children) {
			sb.append(c.toString(indent + 2, escape));
		}
		sb.append(
				indentSb + "\t</children>\n"
				+ indentSb + "</buildable>\n"
				);
		return sb.toString();
	}
	
	public Element toXmlElement(Document doc, boolean recursive) {
		Element buildable = doc.createElement("buildable");
		buildable.setAttribute("id", id);
		buildable.setAttribute("name", name);
		buildable.setAttribute("type", type.toString().toLowerCase());
		buildable.setIdAttribute("id", true);
		
		Element position = doc.createElement("position");
		position.setAttribute("x", "" + this.position.getX());
		position.setAttribute("y", "" + this.position.getY());
		position.setAttribute("z", "" + this.position.getZ());
		buildable.appendChild(position);
		
		Element size = doc.createElement("size");
		size.setAttribute("x", "" + this.size.getX());
		size.setAttribute("y", "" + this.size.getY());
		size.setAttribute("z", "" + this.size.getZ());
		buildable.appendChild(size);
		
		Element attributes = doc.createElement("attributes");
		buildable.appendChild(attributes);
		
		for(Attribute a : this.attributes) {
			Element attr = doc.createElement("attribute");
			attr.setAttribute("name", "" + a.getName());
			attr.setAttribute("value", "" + a.getValue());
			attributes.appendChild(attr);
		}

		Element children = doc.createElement("children");
		buildable.appendChild(children);
		if(recursive)
			for(Buildable c : this.children) {
				children.appendChild(c.toXmlElement(doc, true));
			}
		
		return buildable;
	}
	
}