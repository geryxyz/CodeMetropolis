import static org.junit.Assert.*;

import codemetropolis.toolchain.converter.gitlab.GitLabConnectException;
import codemetropolis.toolchain.converter.gitlab.GitLabConverter;
import org.junit.BeforeClass;
import org.junit.Test;

public class GitLabConverterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetFullUrl() {
		GitLabConverter glc = new GitLabConverter();
		
		final String url = "myHostUrl/myGroup/myProject";
		glc.setGroupName("myGroup");
		glc.setHostUrl("myHostUrl");
		glc.setProjectName("myProject");
		
		assertEquals(true, glc.createFullUrl().equals(url));		
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void testAuthentication() throws GitLabConnectException {
		GitLabConverter glc = new GitLabConverter();
		glc.setProjectName("simple_project");
		glc.setGroupName("simple_group");		
		glc.authentication();
	}

}
