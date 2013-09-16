package app;

import asg.cliche.Command;
import asg.cliche.ShellFactory;
import java.io.IOException;

public class Cli {
	@Command // One,
	public String hello() {
			return "Hello, World!";
	}

	@Command // two,
	public int add(int a, int b) {
			return a + b;
	}

	public static void main(String[] args) throws IOException {
		ShellFactory.createConsoleShell("hello", "", new Cli())
			.commandLoop(); // and three.
	}
}
