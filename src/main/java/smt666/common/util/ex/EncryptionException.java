package smt666.common.util.ex;

/**
 * SHA加密失败的异常。
 * @author 27140
 *
 */
public class EncryptionException extends RuntimeException{

	private static final long serialVersionUID = 3699064716188763676L;
	
	public EncryptionException() {
		super();
	}

	public EncryptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EncryptionException(String message, Throwable cause) {
		super(message, cause);
	}

	public EncryptionException(String message) {
		super(message);
	}

	public EncryptionException(Throwable cause) {
		super(cause);
	}

	
}
