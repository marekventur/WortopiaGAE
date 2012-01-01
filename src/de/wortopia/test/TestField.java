package de.wortopia.test;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import de.wortopia.model.Field;
import de.wortopia.model.Word;
import de.wortopia.model.Words;

public class TestField {
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
	public void testMemcaching() {
		Field f1 = Field.fetchField(1, 4);
		Field f2 = Field.fetchField(1, 4);
		assertEquals(f1, f2);
	}
	
	@Test
	public void testShouldBeDifferent() {
		Field f1 = Field.fetchField(1, 4);
		Field f2 = Field.fetchField(2, 4);
		assertFalse(f1.equals(f2));
	}
	
	@Test
	public void testGenerateField4() {
		Field f = new Field(1, "ABCDEFGHIJKLMNOP");
		assertEquals(4, f.getSize());
		assertEquals("ABCDEFGHIJKLMNOP", f.toString());
		char[] row1 = {'A','E','I','M'}; 
		char[] row2 = {'B','F','J','N'}; 
		char[] row3 = {'C','G','K','O'}; 
		char[] row4 = {'D','H','L','P'}; 
		assertArrayEquals(row1, f.getFieldAsArray()[0]);
		assertArrayEquals(row2, f.getFieldAsArray()[1]);
		assertArrayEquals(row3, f.getFieldAsArray()[2]);
		assertArrayEquals(row4, f.getFieldAsArray()[3]);
	}
	
	@Test
	public void testGenerateField5() {
		Field f = new Field(1, "ABCDEABCDEABCDEABCDEABCDE");
		assertEquals(5, f.getSize());	
		assertEquals("ABCDEABCDEABCDEABCDEABCDE", f.toString());
	}
	
	@Test
	public void testWordOnFieldValid() {
		Field f = new Field(1, "ELEOEENKHHUNAIEL");
		assertEquals(Word.Status.VALID, f.checkWord(new Word("lee")));	
		assertEquals(Word.Status.VALID, f.checkWord(new Word("nee")));	
		assertEquals(Word.Status.VALID, f.checkWord(new Word("eeul")));	
		assertEquals(Word.Status.VALID, f.checkWord(new Word("neia")));	
	}

	@Test
	public void testWordOnFieldTooShort4() {
		Field f = new Field(1, "ELEOEENKHHUNAIEL");
		assertEquals(Word.Status.TOO_SHORT, f.checkWord(new Word("le")));	
	}

	@Test
	public void testWordOnFieldTooShort5() {
		Field f = new Field(1, "ABCDEABCDEABCDEABCDEABCDE");
		assertEquals(Word.Status.TOO_SHORT, f.checkWord(new Word("lee")));	
	}

	@Test
	public void testWordOnFieldNotOnField() {
		Field f = new Field(1, "ELEOEENKHHUNAIEL");
		assertEquals(Word.Status.NOT_ON_FIELD, f.checkWord(new Word("lees")));	
		assertEquals(Word.Status.NOT_ON_FIELD, f.checkWord(new Word("net")));	
		assertEquals(Word.Status.NOT_ON_FIELD, f.checkWord(new Word("eeuley")));	
		assertEquals(Word.Status.NOT_ON_FIELD, f.checkWord(new Word("neiaw")));	
	}
	
	@Test
	public void testWordOnFieldQU() {
		Field f = new Field(1, "QALLEENKHHUNAIEL");
		assertEquals(Word.Status.VALID, f.checkWord(new Word("QUALL")));	
		assertEquals(Word.Status.VALID, f.checkWord(new Word("QALL")));		
	}
	
	@Test
	public void testFindWords() {
		Field f = new Field(1, "ELEOEENKHHUNAIEL");
		f.processWords(Words.getMockWords("lee", "nee", "eeul", "neia", "qualt"));
		assertEquals(4, f.getMaxScore());
		assertTrue(f.getWords().contains(new Word("lee")));
		assertTrue(f.getWords().contains(new Word("nee")));
		assertTrue(f.getWords().contains(new Word("eeul")));
		assertTrue(f.getWords().contains(new Word("neia")));
		assertFalse(f.getWords().contains(new Word("qualt")));
	}

	
}
