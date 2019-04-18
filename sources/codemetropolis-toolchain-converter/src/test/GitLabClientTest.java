import codemetropolis.toolchain.converter.gitlab.GitLabClient;
import codemetropolis.toolchain.converter.gitlab.GitLabConnectException;
import org.junit.BeforeClass;
import org.junit.Test;

public class GitLabClientTest {	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test(expected = GitLabConnectException.class)
	public void testAuthentication() throws GitLabConnectException
	{
		GitLabClient glc = new GitLabClient("mock", "mock", "mock");
		glc.authentication();
	}	
	
}
