package app;

import java.util.Date;

public class Citation {
	public final Date dateCreated;
	public final String description;
	public final String resource;

	public Citation(String description, String resource) {
		if( description == null
				|| description.length() < 0
				|| resource == null
				|| resource.length() < 0 )
		{
			throw new IllegalArgumentException();
		}
		this.description = description;
		this.resource = resource;
		this.dateCreated = new Date();
	}
}
