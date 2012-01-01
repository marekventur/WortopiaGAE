package de.wortopia.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.wortopia.model.Word;

public class TestWord {
	
	@Test
    public void testClone() {
    	char[][] array1 = {{'1', '2'}, {'3', '4'}};
    	char[][] array2 = Word.clone(array1);
    	assertArrayEquals(array1, array2);
    	array1[1][1]='x';
    	assertEquals(array1[1][1], 'x');
    	assertEquals(array2[1][1], '4');
    }
	
	@Test
	public void testNormal() {
		Word word = new Word("haus");
		word.equalsString("Haus");
	}
	
	@Test
	public void testEquals() {
		Word word = new Word("haus");
		Word word2 = new Word("haus");
		assertEquals(word, word2);
	}
	
	@Test
	public void testLength() {
		Word word = new Word("haus");
		assertEquals(word.getLength(), 4);
	}
	
	@Test
	public void testLengthQU() {
		Word word = new Word("Qualle");
		assertEquals(word.getLength(), 6);
	}
	
	@Test
	public void testNormalization() {
		Word word = new Word("Qualle");
		assertEquals(word.getNormalized(), "QALLE");
	}
		
}
