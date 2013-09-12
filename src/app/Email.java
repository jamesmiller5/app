package app;

import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.UniqueFactory;

public class Email extends UniqueEntity {
	public static final String EMAIL_KEY = "address";
	public static final String CLAIM_TOKEN = "claimtoken";

	private static UniqueFactory<Node> factory = null;
	private static UniqueFactory<Node> getFactory() {
		if( factory == null ) {
			factory = createFactory(EMAIL_KEY, "email");
		}
		return factory;
	}

	public Email( final String address ) {
		super(EMAIL_KEY, address, getFactory());
	}

	public Email( final Node internalNode ) {
		super(internalNode);
	}

	public String getAddress() {
		try( Transaction tx = graphDb().beginTx() ) {
			String address = (String) internalNode.getProperty( EMAIL_KEY );
			tx.success();
			return address;
		}
	}

	public ClaimToken getClaimToken() {
		String signature = (String) internalNode.getProperty( CLAIM_TOKEN );
		if( signature == null ) return null;
		return new ClaimToken( signature );
	}

	public boolean isValid() {
		//TODO: more complicated regex

		return true;
	}
}
