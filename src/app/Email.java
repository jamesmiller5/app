package app;

import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

public class Email implements Identity {
	public static final String EMAIL_KEY = "address";
	private static Index<Node> emailByAddress;

	public final String address;
	public final User user;
	public final boolean isClaimed;

	public Email( final String address) {
		this(address, null);
	}

	public Email( final String address, final User user) {
		if( address == null )
			throw new IllegalArgumentException("Address can't be null");

		if( address.length() == 0 )
			throw new IllegalArgumentException("Address can't be zero lenght");

		this.address = address;
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

	public static Email FromEmail( final String address ) {
		//TODO: query database and find the Email that corresponds to this string

		if( emailByAddress == null )
			throw new IllegalStateException("Index email_by_address was null");

		if( address == null || address.length() > 255 )
			throw new IllegalArgumentException("Address was null or over 255 charactesr");

		Node nodeEmail = emailByAddress.get( "address", address ).getSingle();

		if( nodeEmail == null )
			return null;

		return FromNode( nodeEmail );
	}

	public static Email FromNode( final Node source ) {
		String address;

		if( source == null )
			throw new IllegalArgumentException("Source node can't be null");
		if( !source.hasProperty(EMAIL_KEY) )
			throw new IllegalArgumentException("Couldn't find EMAIL_KEY in source node");

		//TODO: query for User or ClaimToken
		address = (String)source.getProperty(EMAIL_KEY);

		return new Email(address);
	}

	public static void BuildIndices() {
		//Tell the db how to build our index
		try(Transaction tx = GraphDatabase.get().beginTx()) {
			emailByAddress = GraphDatabase.get().index().forNodes( "email_by_address" );
			tx.success();
		}
	}
}
