package exceptions;

public class TransferNotAllowedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public TransferNotAllowedException(String message) {
		super(message);
	}

}
