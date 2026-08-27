package exceptions;

public class InvalidContactInfoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidContactInfoException(String message) {
		super(message);
	}
}
