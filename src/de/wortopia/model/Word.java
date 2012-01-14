package de.wortopia.model;

import java.io.Serializable;

public class Word implements Serializable {
	public enum Status {
		VALID(1),
		TOO_SHORT(2),
		UNKNOWN(3),
		NOT_ON_FIELD(4),
		TOO_LATE(5);
		
		private int code;
		
		private Status(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
		
		public boolean isValid() {
			return this == Status.VALID;
		}
	}
	
	private String word;
	private boolean accept = true;
	private int userAskId = 0;
	private int userCheckId = 0;
	//private Language language = Language.GERMAN;
	
	public Word(String word, boolean accept, int userAskId, int userCheckId) {
		super();
		setWord(word);
		this.accept = accept;
		this.userAskId = userAskId;
		this.userCheckId = userCheckId;
	}
	
	public Word(String word) {
		setWord(word);
	}
	
	public String getNormalized() {
		return word.replace("QU", "Q");
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word.toUpperCase()
			.replaceAll("Ä", "AE")
			.replaceAll("Ö", "OE")
			.replaceAll("Ü", "UE")
			.replaceAll("ß", "SS");
	}

	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	public int getUserAskId() {
		return userAskId;
	}

	public void setUserAskId(int userAskId) {
		this.userAskId = userAskId;
	}

	public int getUserCheckId() {
		return userCheckId;
	}

	public void setUserCheckId(int userCheckId) {
		this.userCheckId = userCheckId;
	}
	
	public int getLength() {
		return word.length();
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof Word))
			return false;
		return equalsString(((Word)object).getWord());
	}
	
	public boolean equalsString(String checkWord) {
		return word.equalsIgnoreCase(checkWord);
	}
	
	public String toString() {
		return getWord();
	}
	
	public boolean onField(Field field) {
		return onArray(field.getFieldAsArray());			
	}
	
	public boolean onArray(char[][] aField) {
		int size = aField.length;
		String checkString = getNormalized();
		
		for (int y=0; y<size; y++)
	    	for (int x=0; x<size; x++) 
	    		if (aField[x][y] == checkString.charAt(0)) 
	    			if (iterateArray(clone(aField), checkString.substring(1), x, y, size))
	    				return true;
	    return false;			
	}
	
	/**Creates an independent copy(clone) of the boolean array.
	 * http://stackoverflow.com/a/812814/496118
	 * @param array The array to be cloned.
	 * @return An independent 'deep' structure clone of the array.
	 */
	public static char[][] clone(char[][] array) {
		int rows=array.length ;
		char[][] newArray = (char[][])array.clone();
	    //clone the 'deep' structure of array
	    for(int row=0;row<rows;row++){
	        newArray[row]=(char[]) array[row].clone();
	    }
	    return newArray;
	}
	
	private static boolean iterateArray(char[][] aField, String checkString, int x, int y, int size) {
		
		// No word left! Yeah!
		if (checkString.length() == 0) return true;
		
		// Block current field
		aField[x][y] = '#';
		
		// Get first character, remove from word
		char first = checkString.charAt(0);
		checkString = checkString.substring(1);
		
		// Test cells around [x, y]
		if (y > 0)
			if (aField[x][y-1] == first)
				return iterateArray(clone(aField), checkString, x, y-1, size);
		
		if (y < size-1) 
			if (aField[x][y+1] == first)
				return iterateArray(clone(aField), checkString, x, y+1, size);
		
		if (x > 0) 
			if (aField[x-1][y] == first)
				return iterateArray(clone(aField), checkString, x-1, y, size);
		
		if (x < size-1) 
			if (aField[x+1][y] == first)
				return iterateArray(clone(aField), checkString, x+1, y, size);
			
		if ((x > 0) && (y > 0)) 
			if (aField[x-1][y-1] == first)
				return iterateArray(clone(aField), checkString, x-1, y-1, size);
		
		if ((x > 0) && (y < size-1)) 
			if (aField[x-1][y+1] == first)
				return iterateArray(clone(aField), checkString, x-1, y+1, size);
		
		if ((x < size-1) && (y > 0))  
			if (aField[x+1][y-1] == first)
				return iterateArray(clone(aField), checkString, x+1, y-1, size);
		
		if ((x < size-1) && (y < size-1))  
			if (aField[x+1][y+1] == first)
				return iterateArray(clone(aField), checkString, x+1, y+1, size);
		
		return false;
	}
	
	public int hashCode() {
		return word.hashCode();
	}

	public int getScore() {
		int length = getLength();
		if (length == 3) return 1;
		if (length == 4) return 1;
		if (length == 5) return 2;
		if (length == 6) return 3;
		if (length == 7) return 5;
	    return 11;			
	}
		
}
