import static org.junit.Assert.*;

import codemetropolis.toolchain.converter.gitlab.GitLabElement;
import org.junit.BeforeClass;
import org.junit.Test;

import codemetropolis.toolchain.converter.gitlab.model.User;

public class UserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testAddingRatio() {
		User user = new User();
		user.setAddingRatio(3, 0);
		final float epsilon = 0.01f;

		assertEquals(true, user.getAddingRatio() == 0);
	}

	@Test
	public void testSetDeletionRatio() {
		User user = new User();
		user.setDeletionRatio(3, 10);
		final float epsilon = 0.01f;
		
		assertEquals(true, 0.3 - epsilon <= user.getDeletionRatio() && 0.3 + epsilon >= user.getDeletionRatio());
	}

	@Test
	public void testCreateChildren() {
		GitLabElement user = new User();
		assertEquals(0, user.createChildren().size());
	}

}
