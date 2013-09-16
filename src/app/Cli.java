package app;

import com.martiansoftware.jsap.*;

public class Cli {
	public static void main(String[] args) throws Exception {
		System.out.println("Hello from cli");
/*
		SimpleJSAP jsap = new SimpleJSAP(
			"MyProgram",
			"Repeats \"Hello, world!\" multiple times",
			new Parameter[] {
				new FlaggedOption( "count", JSAP.INTEGER_PARSER, "1", JSAP.REQUIRED, 'n', JSAP.NO_LONGFLAG,
					"The number of times to say hello." ),
				new QualifiedSwitch( "verbose", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.NOT_REQUIRED, 'v', "verbose",
					"Requests verbose output." ).setList( true ).setListSeparator( ',' ),
				new UnflaggedOption( "name", JSAP.STRING_PARSER, "World", JSAP.REQUIRED, JSAP.GREEDY,
					"One or more names of people you would like to greet." )
			}
		);

		JSAPResult config = jsap.parse(args);
		if( jsap.messagePrinted() ) System.exit( 1 );

		String[] names = config.getStringArray("name");
		String[] languages = config.getStringArray("verbose");
		for( int i = 0; i < languages.length; i++ ) {
			System.out.println("language=" + languages[i]);
		}
		for( int i = 0; i < config.getInt("count"); i++ ) {
			for( int j = 0; j < names.length; j++ ) {
				System.out.println((config.getBoolean("verbose") ? "Hello" : "Hi")
						+ ", "
						+ names[j]
						+ "!");
			}
		}
*/
	}
}
