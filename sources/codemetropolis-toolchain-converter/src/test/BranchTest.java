import static org.junit.Assert.*;

import codemetropolis.toolchain.commons.cdf.CdfProperty;
import org.junit.BeforeClass;
import org.junit.Test;

import codemetropolis.toolchain.converter.gitlab.model.Branch;

import java.util.ArrayList;
import java.util.List;

public class BranchTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testCreateProperties() {
		Branch b = new Branch();
		List<CdfProperty> list = new ArrayList();

		b.setIsMerged(0);
		b.setDevelopersCanPush(1);
		b.addProperties(list);
		assertEquals(2, list.size());
	}

	@Test
	public void testConvertBooleanToInteger() {		
		assertEquals(0, Branch.convertBooleanToInteger(false));
		assertEquals(1, Branch.convertBooleanToInteger(true));
	}
}
