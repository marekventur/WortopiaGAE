package de.wortopia.test;


import org.junit.After;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import de.wortopia.model.Signer;

public class SignerTester {
	
	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());
	private Signer signer;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		signer = new Signer();
	}

	@After
    public void tearDown() {
        helper.tearDown();
    }
	
	@Test
	public void testNotEmpty() {
		assertTrue(signer.sign("").length() > 0);
	}
	
	@Test
	public void testTheSame() {
		assertEquals(signer.sign("asf"), signer.sign("asf"));
		assertEquals(signer.sign("asdf", "123"), signer.sign("asdf", "123"));
		assertEquals(signer.sign(""), signer.sign(""));
		assertEquals(signer.sign("123"), signer.sign("123"));
	}

	@Test
	public void testCheck() {
		assertTrue(signer.check(signer.sign("asf"), "asf"));
		assertTrue(signer.check(signer.sign("4256"), "4256"));
		assertFalse(signer.check(signer.sign("456"), "123"));	
	}
	
}
