package codemetropolis.toolchain.placing.layout;

import codemetropolis.toolchain.placing.exceptions.NonExistentLayoutException;
import codemetropolis.toolchain.placing.layout.pack.PackLayout;
import junit.framework.TestCase;

public class LayoutTest extends TestCase {
	
	public void testParse() {
		
		try {
			assert(Layout.parse("PACK") instanceof PackLayout);
		} catch (NonExistentLayoutException e) {
			fail();
		}
		
		try {
			Layout.parse("WRONGTYPE");
			fail();
		} catch (NonExistentLayoutException e) {}
		
		
	}
	
}
