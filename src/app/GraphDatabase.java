package app;

import java.io.File;
import java.io.IOException;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;

public abstract class GraphDatabase {
	//make a scratch dir for database & other data stores at the app root
	public static final File scratch = new File(Hello.class.getClassLoader().getResource("").getPath() + File.separator + ".." + File.separator + "scratch");
    public static final String DB_NAME = scratch.getPath() + File.separator + "neo4j-hello-db";

    private static final GraphDatabaseService graphDb;

	//always invoked upon class inclusion, removes startup commands
	static {
		//make sure this directory exists
		scratch.mkdir();

		//open embedded database
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_NAME );
        registerShutdownHook( graphDb );
	}

	public static Transaction startTransaction() {
		return graphDb.beginTx();
	}

    private static void clearDb() {
        try {
            FileUtils.deleteRecursively( new File( DB_NAME ) );
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb ) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        } );
    }
}
