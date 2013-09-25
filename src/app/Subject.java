package app;

import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import java.util.ArrayList;

public class Subject {
	private static final String SUBJECT_KEY = "Subject_key";
	private static final String SUBJECT_INDEX = "Subject";
	private static final String SUBJECT_IDENT = "Singleton";
	private static final String NAME = "NAME";

	private static final UniqueNodeFactory factory = new UniqueNodeFactory( SUBJECT_KEY, SUBJECT_INDEX );

	private final String subject;

	public Subject( final String name ) {
		if( name == null
				|| name.length() > 256 )
		{
			throw new IllegalArgumentException();
		}

		this.subject = name;
	}

	public String getName() {
		return this.subject;
	}
	/*
	public static boolean isSubject( final String subj ) {
		GraphDatabaseService graphDb = GraphDatabase.get();
		try( Transaction tx = graphDb.beginTx() ) {
			boolean isSub = getSubjectMaster().hasLabel( DynamicLabel.label(subj) );
			tx.success();
			return isSub;
		}
	}
	public static Subject[] getSubjects() {
		GraphDatabaseService graphDb = GraphDatabase.get();
		try( Transaction tx = graphDb.beginTx() ) {
			ArrayList<Subject> subjects = new ArrayList<Subject>();
			Node master = getSubjectMaster();
			for(Label subj : master.getLabels()) {
				subjects.add(new Subject(subj.name()));
			}
			tx.success();
			return subjects.toArray(new Subject[0]);
		}
	}
	public static Node getSubjectMaster() {
		// code-level factory, Db call internal - internal transaction.
		return factory.getOrCreate( SUBJECT_IDENT );
	}
	*/
}
