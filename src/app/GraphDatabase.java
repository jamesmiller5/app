package app;

import java.io.File;
import java.io.IOException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;

public abstract class GraphDatabase {
	//make a scratch dir for database & other data stores at the app root
	public static final File scratch = new File(Hello.class.getClassLoader().getResource("").getPath() + File.separator + ".." + File.separator + "scratch");
    public static final String DB_NAME = scratch.getPath() + File.separator + "neo4j-hello-db";

    private static GraphDatabaseService graphDb = null;
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

	public static void startup() {
		//make sure this directory exists
		scratch.mkdir();

		//open embedded database
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_NAME );
        registerShutdownHook( graphDb );
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
	}

	//for unit testing mostly
    public static void clearDb() {
		//shutdown db first
		shutdown();

        try {
            FileUtils.deleteRecursively( new File( DB_NAME ) );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
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
