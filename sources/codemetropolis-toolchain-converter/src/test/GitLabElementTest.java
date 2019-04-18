import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.*;
import codemetropolis.toolchain.converter.gitlab.model.Issue;
import codemetropolis.toolchain.converter.gitlab.model.Milestone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GitLabElementTest {

    private static GitLabElement element;

    private String ID="12";
    private String MILESTONE_ID="11";

    private Issue issue;
    private Milestone milestone;

    @BeforeClass
    public static void setUp() {
        element = new GitLabElement() {
            @Override
            public List<CdfProperty> createProperties() {
                return new ArrayList<>();
            }

            @Override
            public List<Pair> createChildren() {
                return new ArrayList<>();
            }
        };
    }

    @Before
    public void afterSetUp() {
        issue=new Issue();
        issue.setID(ID);
        issue.setType(Type.ISSUE);
        milestone=new Milestone();
        milestone.setID(MILESTONE_ID);
    }

    @Test
    public void testIfConsistsGitLabResource() {
        GitLabResource.addElement(issue);
        element.setType(Type.ISSUE);
        element.setID(ID);

        try {
            Assert.assertEquals(element.getElement(),  issue.getElement());
        } catch (GitLabException e) {
            e.printStackTrace();
        }

        GitLabResource.clearMainStorage();
    }

}
