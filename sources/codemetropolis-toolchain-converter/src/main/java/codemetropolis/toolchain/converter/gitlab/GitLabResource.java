package codemetropolis.toolchain.converter.gitlab;

import codemetropolis.toolchain.commons.cdf.CdfElement;
import codemetropolis.toolchain.converter.gitlab.model.*;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class GitLabResource {

    private static Map<Type, Map<String, GitLabElement>> mainStorage = new EnumMap<Type, Map<String, GitLabElement>>(Type.class);

    public static GitLabElement receiveElement(Type t) throws GitLabException {
        switch(t) {
            case PROJECT:
                return new Project();
            case COMMIT:
                return new Commit();
            case TREE:
                return new Tree();
            case ISSUE:
                return new Issue();
            case BRANCH:
                return new Branch();
            case MILESTONE:
                return new Milestone();
            case USER:
                return new User();
            default:
                throw new GitLabException("Illegal type");
        }

    }

    public static void clearMainStorage() {mainStorage.clear();}

    public static Map<Type, Map<String, GitLabElement>> getMainStorage() {
        return mainStorage;
    }

    public static boolean consistsOf(Type type, String ID) {
        return mainStorage.containsKey(type) && mainStorage.get(type).containsKey(ID);
    }

    public static boolean addElement(GitLabElement element) {
        if (mainStorage.containsKey(element.type)) {
            if (mainStorage.get(element.type).containsKey(element.ID))
                return false;

            mainStorage.get(element.type).put(element.ID, element);

            return true;
        }

        mainStorage.put(element.type, new HashMap<>());
        mainStorage.get(element.type).put(element.ID, element);

        return true;
    }

    public static void addParentID(Type type, String ID, String parentID) {
        if (consistsOf(type, ID)) {
            GitLabElement element=mainStorage.get(type).get(ID);
            element.addParentID(parentID);
            mainStorage.get(type).put(ID, element);
        }
    }

    public static Map<String, GitLabElement> getAllByType(Type type) {
        return mainStorage.get(type);
    }

    public static GitLabElement getElement(Type type, String ID) {
        return mainStorage.get(type).get(ID);
    }

    public static GitLabElement getElement(Pair pair) {
        return getElement(pair.getType(), pair.getID());
    }

    public static CdfElement getElement(Type type, String ID, String parentId) throws GitLabException {
        if (ID==null) return new CdfElement();

        try {
            if (mainStorage.containsKey(type)) {
                if (mainStorage.get(type).containsKey(ID))
                    return mainStorage.get(type).get(ID).element;

                GitLabElement gle = createGitLabElement(type, ID, parentId);
                mainStorage.get(type).put(ID, gle);

                return gle.element;
            }

            Map<String, GitLabElement> m=new HashMap<>();
            GitLabElement gle = createGitLabElement(type, ID, parentId);

            CdfElement out=gle.getElement();
            m.put(ID, gle);

            mainStorage.put(type, m);

            return out;
        } catch (Exception e) {
            throw new GitLabException("Illegal argument added");
        }

    }

    private static GitLabElement createGitLabElement(Type type, String ID, String parentId) throws GitLabException {
        GitLabElement gle = receiveElement(type);
        gle.addParentID(parentId);
        gle.setType(type);
        gle.setID(ID);
        gle.makeProperties();

        return gle;
    }

}
