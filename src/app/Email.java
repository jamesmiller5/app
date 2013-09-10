package app;

public class Email {
	public final String email;

	public Email( final String email ) {
		if( email == null )
			throw new IllegalArgumentException();

		if( email.length() == 0 )
			throw new IllegalArgumentException();

		this.email = email;
	}

	public boolean isValid() {
		//TODO: more complicated regex

		return true;
	}
}
