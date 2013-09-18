package app;

public class RecoveryToken extends Token {
	/**
	 * Constructs unique RecoveryToken
	 */
	public RecoveryToken() {
		super();
	}

	/**
	 * Constructs RecoveryToken from signature
	 */
	public RecoveryToken(String signature) {
		super(signature);
	}

	public static RecoveryToken randomToken() {
		return new RecoveryToken();
	}
}
