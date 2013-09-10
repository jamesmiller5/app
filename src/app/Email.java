package app;

public class Email implements Identity {
	public final String email;
	public final User user;
	public final boolean isClaimed;

	public Email( final String email) {
		this(email, null);
	}
	public Email( final String email, final User user) {
		if( email == null )
			throw new IllegalArgumentException();

		if( email.length() == 0 )
			throw new IllegalArgumentException();

		this.email = email;
		this.user = user;

		if( this.user == null ) {
			this.isClaimed = false;
		} else {
			this.isClaimed = true;
		}
	}

	public Email getEmail() {
		if( isClaimed ) {
			return user.getEmail();
		}
		return this;
	}

	public boolean isValid() {
		//TODO: more complicated regex

		return true;
	}
}
