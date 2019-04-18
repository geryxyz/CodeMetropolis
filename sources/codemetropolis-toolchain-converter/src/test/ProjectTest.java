import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import codemetropolis.toolchain.converter.gitlab.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import codemetropolis.toolchain.commons.cdf.CdfProperty;
import codemetropolis.toolchain.converter.gitlab.model.Project;

public class ProjectTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testCreateProperties() {
		GitLabElement project = new Project();
		List<CdfProperty> l = project.createProperties();
		assertEquals(true, l.size() == 0);
	}

	@Test
	public void testAddProperties() {
		Project p = new Project();
		List<CdfProperty> list = new ArrayList<>();

		p.setCommitCount(33);
		p.setJobArtifactsSize(3);
		p.setJobArtifactsSize(10);
		p.setLfsObjectSize(4);
		p.setStorageSize(10);
		p.setApprovalsNumber(3);
		p.setForksCount(4);
		p.setName("namae");

		p.addProperties(list);

		assertEquals(true, list.size() == 7);
	}

	@Test
	public void testCreateChildren() {
		GitLabElement project = new Project();
		List<Pair> pairList = project.createChildren();
		assertEquals(false, pairList.size() != 0);		
	}
}
