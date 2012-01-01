package de.wortopia.exceptions;

public class UsernameException extends Exception {
	public enum Type {
		TOO_SHORT(1),
		TOO_LONG(2),
		ALREADY_USED(3),
		INVALID_CHARACTER(4),
		UNCHANGEABLE(4);
		
		private int code;

		private Type(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}
	
	private Type type;

	public UsernameException(Type type) {
		super();
		this.type = type;
	}

	public Type getType() {
		return type;
	}	
}
