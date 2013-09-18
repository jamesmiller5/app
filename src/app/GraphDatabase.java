package app;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;

public abstract class GraphDatabase {
	//make a scratch dir for database & other data stores at the app root
	public static final File scratch = new File(GraphDatabase.class.getClassLoader().getResource("").getPath() + File.separator + ".." + File.separator + "scratch");
    public static final String DB_NAME = scratch.getPath() + File.separator + "neo4j-db";

    private static GraphDatabaseService graphDb = null;
	private static ExecutionEngine engine = null;

	private static Thread shutdownHook = null;

	public static GraphDatabaseService get() {
		if( graphDb == null ) {
			startup();

			if( graphDb == null ) {
				throw new IllegalStateException("Couldn't create GraphDatabaseService");
			}
		}

		return graphDb;
	}

	public static ExecutionResult execute(String query, Map<String, Object> params) {
		if( engine == null ) {
			engine = new ExecutionEngine( GraphDatabase.get() );
		}
		return engine.execute(query, params);
	}

	public static void startup() {
		//make sure this directory exists
		scratch.mkdir();

		//open embedded database
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_NAME );
        registerShutdownHook( graphDb );

		//build indices, should be an observer pattern(?) eventually
		//Email.BuildIndices();

		//Wait for indices to be built or fail after 30 seconds
		try (Transaction tx = graphDb.beginTx()){
			graphDb.schema().awaitIndexesOnline( 30, java.util.concurrent.TimeUnit.SECONDS );
			tx.success();
		}
	}

	public static void shutdown() {
		if( shutdownHook != null ) {
			Runtime.getRuntime().removeShutdownHook( shutdownHook );
			shutdownHook = null;
		}

		if( graphDb != null ) {
			graphDb.shutdown();
			graphDb = null;
		}

		engine = null;
	}

	//for unit testing mostly
    public static void clearDb() {
		//shutdown db first
		shutdown();
		IOException rt = null;
		for(int i=0; i<20; i++) {
		    try {
		       	FileUtils.deleteRecursively( new File( DB_NAME ) );
		       	return;
		    } catch ( IOException e ) {
				rt = e;		    
			}
        }
        throw new RuntimeException( rt );
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb ) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
		shutdownHook = new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        };

        Runtime.getRuntime().addShutdownHook( shutdownHook );
    }
}
