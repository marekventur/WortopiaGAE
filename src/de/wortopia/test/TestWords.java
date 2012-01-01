package de.wortopia.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import de.wortopia.exceptions.NoWordListInMemcache;
import de.wortopia.model.Word;
import de.wortopia.model.Words;

public class TestWords {
	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
	
	@Test
	public void testMock() {
		assertTrue(Words.getMockWords("REE", "FEE", "SEE").size() == 3);
		assertTrue(Words.getMockWords("REE", "FEE", "SEE").contains(new Word("ree")));
		assertTrue(Words.getMockWords("REE", "FEE", "SEE").contains(new Word("fee")));
		assertTrue(Words.getMockWords("REE", "FEE", "SEE").contains(new Word("see")));
		assertFalse(Words.getMockWords("REE", "FEE", "SEE").contains(new Word("tee")));
	}
	
	@Test(expected=NoWordListInMemcache.class)
	public void testNotInMemcacheException() {
		Words.getAllWords();
	}
	
	@Test
	public void testWithMemcache() {
		Words.recache();
		Words.getAllWords();
	}
	
	
	
}
