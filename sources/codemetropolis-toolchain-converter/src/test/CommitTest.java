import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.Pair;
import codemetropolis.toolchain.converter.gitlab.model.Commit;
import org.gitlab4j.api.GitLabApi;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class CommitTest {

    private Commit commit;

    private int COMMIT_ADDITION=10;
    private int COMMIT_DELETIONS=15;
    private int COMMIT_TOTAL=20;
    private int projectID=10;

    private String COMMIT_MESSAGE="Commit branch";
    private String COMMIT_STATUS="success";
    private String ID="aAAAa";

    @Before
    public void setup() {
        commit=new Commit();
        GitLabElement.setGitLabApi(new GitLabApi("", ""));
        GitLabElement.setProjectID(10);
        commit.setID(ID);
        commit.setAddition(COMMIT_ADDITION);
        commit.setDeletions(COMMIT_DELETIONS);
        commit.setStatus(COMMIT_STATUS);
        commit.setTotal(COMMIT_TOTAL);
        commit.setMessage(COMMIT_MESSAGE);
        commit.makeProperties();
    }

    @Test
    public void testIfPropertiesConsistsElements() {

        List<CdfProperty> properties=commit.createProperties();
        Assert.assertTrue(properties.stream().anyMatch(item->(COMMIT_MESSAGE.equals(item.getValue()))
                && "message".equals(item.getName())
                && CdfProperty.Type.STRING.equals(item.getType())));

    }

    @Test
    public void testCommitMessageLength() {

        Commit commit=new Commit();
        commit.setID(ID);
        commit.setMessage(COMMIT_MESSAGE);
        List<CdfProperty> properties= commit.createProperties();
        Assert.assertTrue(properties.stream().anyMatch(item->("13".equals(item.getValue()))
                && "messageLength".equals(item.getName()) &&
                CdfProperty.Type.INT.equals(item.getType())));
    }



    @Test
    public void testArraySize() {
        List<CdfProperty> properties=commit.createProperties();
        Assert.assertEquals(7, properties.size());
    }

    @Test
    public void testChildren() {
        List<Pair> children= commit.createChildren();
        Assert.assertEquals(0, children.size());
    }

    @Test
    public void testIfCommitIDIsNull() {
        commit.setID(null);
        Assert.assertEquals(true, commit.createProperties().isEmpty());

    }

}
