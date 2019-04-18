import codemetropolis.toolchain.commons.cdf.CdfElement;
import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.GitLabException;
import codemetropolis.toolchain.converter.gitlab.GitLabResource;
import codemetropolis.toolchain.converter.gitlab.Type;
import codemetropolis.toolchain.converter.gitlab.model.Commit;
import org.gitlab4j.api.GitLabApi;
import org.junit.*;
import org.junit.rules.ExpectedException;
import codemetropolis.toolchain.converter.gitlab.model.Branch;
import codemetropolis.toolchain.converter.gitlab.model.Commit;
import codemetropolis.toolchain.converter.gitlab.model.Project;

import static org.junit.Assert.assertEquals;

public class GitLabResourceTest {

    private static Commit commit;

    private String COMMIT_MESSAGE="Commit branch";
    private String COMMIT_STATUS="success";
    private String ID = "aAAAa";

    private int COMMIT_ADDITION=10;
    private int COMMIT_DELETIONS=15;
    private int COMMIT_TOTAL=20;
    private int projectID=10;

    @BeforeClass
    public static void setUp() {
        commit=new Commit();
        GitLabElement.setGitLabApi(new GitLabApi("", ""));
        GitLabElement.setProjectID(10);
    }

    @Before
    public void afterSetUp() {
        GitLabResource.clearMainStorage();
        commit.setID(ID);
        commit.setAddition(COMMIT_ADDITION);
        commit.setDeletions(COMMIT_DELETIONS);
        commit.setStatus(COMMIT_STATUS);
        commit.setTotal(COMMIT_TOTAL);
        commit.setMessage(COMMIT_MESSAGE);
    }

    @Test
    public void testConsistsOf() {
        GitLabElement branch = new Branch();
        branch.setID("myBelovedBranch");
        branch.setType(Type.BRANCH);

        assertEquals(false, GitLabResource.consistsOf(Type.BRANCH, "myBelovedBranch"));
        GitLabResource.addElement(branch);

        assertEquals(false, GitLabResource.consistsOf(Type.COMMIT, "non-existing-commit"));
        assertEquals(true, GitLabResource.consistsOf(Type.BRANCH, "myBelovedBranch"));
    }

    @Test
    public void testAddElement() {
        GitLabElement project = new Project();
        project.setID("root");
        project.setType(Type.PROJECT);

        GitLabElement project2 = new Project();
        project2.setID("root");
        project2.setType(Type.PROJECT);


        assertEquals(true, GitLabResource.addElement(project));
        assertEquals(false, GitLabResource.addElement(project2));
    }

    @Test
    public void testAddParentID() {
        GitLabElement commit = new Commit();
        commit.setID("c1");
        commit.addParentID("parent_c1");
        assertEquals(true, commit.getParentIds().contains("parent_c1"));
    }

    @Test
    public void testIfReturnsTheSameValue() {
        CdfElement element=new CdfElement();
        CdfElement element2=new CdfElement();

        try {
            element = GitLabResource.getElement(Type.COMMIT, ID, "aaa");
            element2 = GitLabResource.getElement(Type.COMMIT, ID, "aaa");
        } catch (GitLabException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(element2, element);

        GitLabResource.clearMainStorage();
    }

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    @Test
    public void testIfTypeNull() throws GitLabException {
        thrown.expect(GitLabException.class);
        thrown.expectMessage("Illegal argument added");
        CdfElement element = GitLabResource.getElement(null, ID, "aaa");
    }

    @Test
    public void testIfNullIDIsInserted() throws GitLabException {
        GitLabResource.clearMainStorage();
        CdfElement element= GitLabResource.getElement(Type.COMMIT, null, "aaa");
        Assert.assertEquals(true, GitLabResource.getMainStorage().isEmpty());
        GitLabResource.clearMainStorage();
    }

}
