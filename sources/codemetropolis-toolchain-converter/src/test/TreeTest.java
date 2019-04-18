import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.model.Tree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeTest {

    private static Tree tree;
    private static String ID = "doc---master";

    @Before
    public void setUp() {
        GitLabElement.setGitLabApi(null);
        tree = new Tree();
        tree.setID(ID);
    }

    @Test
    public void branchAndNameHasBeenSplitted() {
        tree.createProperties();
        Assert.assertEquals("master", tree.getBranchName());
        Assert.assertEquals("doc", tree.getName());
    }

    @Test
    public void ifIDIsNullPropertyIsEmpty() {
        tree.setID(null);
        Assert.assertEquals(true, tree.createProperties().isEmpty());
    }

    @Test
    public void ifIDisNullPChildrenIsEmpty() {
        tree.setID(null);
        Assert.assertEquals(true, tree.createChildren().isEmpty());
    }

}
