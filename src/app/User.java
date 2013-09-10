package app;

public class User implements Identity {
	public final Email[] emails;

	public User(final Email[] emails) {
		if( emails == null || emails.length>0 ) {
			throw new IllegalArgumentException();
		}
		this.emails = emails;
	}

	public Email getEmail() {
		return emails[0];
	}
}
