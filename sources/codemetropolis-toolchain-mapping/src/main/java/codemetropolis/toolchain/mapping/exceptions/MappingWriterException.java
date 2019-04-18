package codemetropolis.toolchain.mapping.exceptions;

public class MappingWriterException extends MappingException {

	private static final long serialVersionUID = -3708470145539789698L;

	public MappingWriterException() {
		super();
	}

	public MappingWriterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MappingWriterException(String message, Throwable cause) {
		super(message, cause);
	}

	public MappingWriterException(String message) {
		super(message);
	}

	public MappingWriterException(Throwable cause) {
		super(cause);
	}

}
