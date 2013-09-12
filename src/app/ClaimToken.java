package app;

public class ClaimToken extends Token {
	/**
	 * Constructs unique ClaimToken
	 */
	public ClaimToken() {
		super();
	}

	/**
	 * Constructs ClaimToken from signature
	 */
	public ClaimToken(String signature) {
		super(signature);
	}
}
