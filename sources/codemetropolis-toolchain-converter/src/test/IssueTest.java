import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.Type;
import codemetropolis.toolchain.converter.gitlab.model.Issue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class IssueTest {

    private Issue issue;

    private static String ISSUE_STATE="opened";
    private static int ISSUE_USER_NOTES_COUNT=10;
    private static int ISSUE_WEIGHT=2;
    private static int ISSUE_TIME_ESTIMATE=4;
    private static int ISSUE_TOTAL_TIME=11;

    @Before
    public void setUp() {
        issue=new Issue();
        issue.setType(Type.ISSUE);
        issue.setState(ISSUE_STATE);
        issue.setTimeEstimate(ISSUE_TIME_ESTIMATE);
        issue.setTotalTime(ISSUE_TOTAL_TIME);
        issue.setUserNotesCount(ISSUE_USER_NOTES_COUNT);
        issue.setWeight(ISSUE_WEIGHT);
        issue.setID("12");
        GitLabElement.setGitLabApi(null);
    }

    @Test
    public void testArraySize() {
        List<CdfProperty> properties=issue.createProperties();
        Assert.assertEquals(6, properties.size());
    }

    @Test
    public void testIfPropertiesConsistsElements() {
        List<CdfProperty> properties=issue.createProperties();
        Assert.assertTrue(properties.stream().anyMatch(item->(
                ISSUE_STATE.equals(item.getValue()) &&
                        "state".equals(item.getName())&&
                        CdfProperty.Type.STRING.equals(item.getType())
                )));
    }

    @Test
    public void testIfIssueIDIsNull() {
        issue.setID(null);
        Assert.assertEquals(true, issue.createProperties().isEmpty());
    }

    @Test
    public void testTimeRatio() {
        float t=ISSUE_TIME_ESTIMATE/(float)ISSUE_TOTAL_TIME;
        String ISSUE_RATIO=Float.toString(t);
        List<CdfProperty> properties=issue.createProperties();
        Assert.assertTrue(properties.stream().anyMatch(item->
                    ISSUE_RATIO.equals(item.getValue()) &&
                            "timeRatio".equals(item.getName()) &&
                            CdfProperty.Type.FLOAT.equals(item.getType())
                ));
    }

    @Test
    public void testChildren() {
        Assert.assertEquals(0, issue.createChildren().size());
    }

}
