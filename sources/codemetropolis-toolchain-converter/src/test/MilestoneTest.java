import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.*;
import codemetropolis.toolchain.converter.gitlab.model.Issue;
import codemetropolis.toolchain.converter.gitlab.model.Milestone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MilestoneTest {

    private Milestone milestone;
    private static String MILESTONE_TITLE = "title";
    private static String MILESTONE_STATE = "state";

    @Before
    public void setUp() {
        milestone = new Milestone();
        milestone.setState(MILESTONE_STATE);
        milestone.setTitle(MILESTONE_TITLE);
        milestone.setID("15");
        GitLabElement.setGitLabApi(null);
    }

    @Test
    public void testArraySize() {
        List<CdfProperty> properties = milestone.createProperties();
        Assert.assertEquals(4, properties.size());
    }

    @Test
    public void testIfPropertiesConsistsElements() {
        List<CdfProperty> properties = milestone.createProperties();
        Assert.assertTrue(properties.stream().anyMatch(item->(MILESTONE_STATE.equals(item.getValue()))
                && "state".equals(item.getName())
                && CdfProperty.Type.STRING.equals(item.getType())));
    }

    @Test
    public void testIfMilestoneIDIsNull() {
        milestone.setID(null);
        Assert.assertEquals(true, milestone.createProperties().isEmpty());

    }

    private Milestone addPair(Pair pair, String state) {
        milestone.addChild(pair);
        Issue issue = new Issue();
        issue.setID(pair.getID());
        issue.setState(state);
        issue.setType(Type.ISSUE);

        try {
            issue.getElement();
        } catch (GitLabException e) {
            e.printStackTrace();
        }

        GitLabResource.addElement(issue);

        return milestone;
    }

    @Test
    public void testChildrenIssues() {

        addPair(new Pair("11", Type.ISSUE), "opened");
        addPair(new Pair("12", Type.ISSUE), "closed");

        List<CdfProperty> properties=milestone.createProperties();

        Assert.assertTrue(properties.stream().anyMatch(item->("0.5".equals(item.getValue()))
                && "openedRatio".equals(item.getName()) &&
                CdfProperty.Type.FLOAT.equals(item.getType())));

        Assert.assertTrue(properties.stream().anyMatch(item->("0.5".equals(item.getValue()))
                && "closedRatio".equals(item.getName()) &&
                CdfProperty.Type.FLOAT.equals(item.getType())));

        GitLabResource.clearMainStorage();
    }

    @Test
    public void testChildrenMethodIfIDIsNull() {
        milestone.setID(null);
        Assert.assertEquals(0, milestone.createChildren().size());
    }
}
