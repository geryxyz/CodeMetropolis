package codemetropolis.toolchain.converter.gitlab;

import codemetropolis.toolchain.commons.cdf.CdfElement;
import codemetropolis.toolchain.commons.cdf.CdfProperty;
import org.gitlab4j.api.GitLabApi;

import java.util.ArrayList;
import java.util.List;

public abstract class GitLabElement {

    private List<CdfProperty> properties;
    protected List<Pair> children;
    private List<String> parentIds;

    protected static Integer projectId;
    protected static GitLabApi gitLabApi;

    protected Type type;
    protected String ID;

    public String getID() {
        return ID;
    }

    protected CdfElement element;

    public List<String> getParentIds() {
        return parentIds;
    }

    public void addChild(Pair child) {
        children.add(child);
    }

    public static void setProjectID(int project) {
        projectId = project;
    }

    public static void setGitLabApi(GitLabApi gitLabA) {
        gitLabApi = gitLabA;
    }

    public void addParentID(String parentId) {
        parentIds.add(parentId);
    }

    public GitLabElement() {
        this.parentIds=new ArrayList<>();
        this.children=new ArrayList<>();
    }

    public void makeProperties() {
        properties = createProperties();
    }

    public void makeChildren() {
        children = createChildren();
    }

    private  String currParentId;

    public CdfElement getElement() throws GitLabException {

        if (GitLabResource.consistsOf(type, ID)) {
            GitLabResource.addParentID(type, ID, currParentId);
            return GitLabResource.getElement(type, ID).element;
        }

        CdfElement element = new CdfElement(ID, type.toString().toLowerCase());
        makeChildren();

        for(Pair ch: children) {
            GitLabElement el=GitLabResource.receiveElement(ch.getType());
            el.addParentID(ID);
            el.currParentId=ID;
            el.setID(ch.getID());
            el.setType(ch.getType());
            element.addChildElement(el.getElement());
        }

        makeProperties();

        for (CdfProperty property : properties) {
            element.addProperty(property.getName(), property.getValue(), property.getType());
        }


        this.element=element;
        GitLabResource.addElement(this);

        return  element;
    }

    public  void setID(String id) {
        this.ID = id;
    }

    public void setType(Type type) {this.type = type;}

    public abstract List<CdfProperty> createProperties();

    public abstract List<Pair> createChildren();

}
