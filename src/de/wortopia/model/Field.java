package de.wortopia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;


public class Field implements Serializable {
	private static final long serialVersionUID = 3786972484834684297L;
	private long gameId;
	private int size;
	private int maxScore;
	private char[][] field;
	private Collection<Word> words;
	
	/**
	 * Return a specific field
	 * @param gameId
	 * @param size
	 * @return a field
	 */
	public static Field fetchField(long gameId, int size) {
		if ((size != 4) && (size != 5)) 
			throw new IllegalArgumentException("Size not 4 or 5");
		
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		Field field = null;
		String key = "field:"+gameId+":"+size;
    
        if (cache.contains(key)) {
        	field = (Field)cache.get(key);
        }
        else
        {
        	field = new Field(gameId, size);
        	cache.put(key, field, Expiration.byDeltaSeconds(60 * 60)); // 1 hour
        }
          
        return field;
	}
	
	private Field(long gameId, int size) {
		this.gameId = gameId;
		this.size = size;
		field = new char[size][size];
		if ((size != 4) && (size != 5)) 
			throw new IllegalArgumentException("Size not 4 or 5");
		
		for (int y=0; y<size; y++) 
			for (int x=0; x<size; x++) 
				field[x][y] = Language.GERMAN.getRandomLetter();
		
		words = new ArrayList<Word>();
	}
	
	public Field (long gameId, String strField) {
		this.gameId = gameId;
		
		// Uppercase rules
		strField = strField.toUpperCase();
		
		// Check size
		if ((strField.length() != 16) && (strField.length() != 25)) 
			throw new IllegalArgumentException("Size not 4 or 5");
		size = strField.length()==16?4:5;
		
		// Loop over field
		field = new char[size][size];
		for (int y=0; y<size; y++) 
			for (int x=0; x<size; x++) 
				field[x][y] = strField.charAt(y*size + x);
		
		words = new ArrayList<Word>();
			
	}

	public long getGameId() {
		return gameId;
	}

	public int getSize() {
		return size;
	}
	
	public String toString() {
		String result = "";
		for (int y=0; y<size; y++) 
			for (int x=0; x<size; x++) 
				result += field[x][y];
		return result;
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof Field))
			return false;
		Field field = (Field)object;
		if (!field.toString().contentEquals(toString()))
			return false;
		if (getGameId() != field.getGameId())
			return false;
		return true;
	}
	
	public Word.Status checkWord(Word word) {
		if ((size==5) && (word.getLength()<4)) 
			return Word.Status.TOO_SHORT;
		if (word.getLength()<3) 
			return Word.Status.TOO_SHORT;
		if (!word.onField(this))
			return Word.Status.NOT_ON_FIELD;
		return Word.Status.VALID;
	}

	public char[][] getFieldAsArray() {
		return field;
	}
	
	public void processWords(Collection<Word> wordsToCheck) {
		for (Word word: wordsToCheck) 
			if (checkWord(word) == Word.Status.VALID) {
				words.add(word);
				maxScore += word.getScore();
			}
	}

	public Collection<Word> getWords() {
		return words;
	}
	
	public int getMaxScore() {
		return maxScore;
	}
	
}
