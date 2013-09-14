package app;

import java.util.Date;

public class Session {
	// Reference account
	public final User user;
	public final Date validUntil;

	public Session(User user, Date validUntil) {
		if(user == null || validUntil == null) {
			throw new IllegalArgumentException();
		}
		this.user = user;
		this.validUntil = validUntil;
	}

	public boolean isValid() {
		Date now = new Date();

		//test if validUntil is equal to now or now is < 0 (before validUntil)
		return (now.compareTo(validUntil) <= 0);
	}
}
