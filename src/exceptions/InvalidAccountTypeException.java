package exceptions;

public class InvalidAccountTypeException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidAccountTypeException(String message) {
		super(message);
	}
}
