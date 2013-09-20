package test.integrate;

import static org.junit.Assert.*;
import org.junit.*;
import org.neo4j.graphdb.Transaction;
import app.*;
import asg.cliche.Command;
import asg.cliche.ShellFactory;
import asg.cliche.InputConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Iterator;
import org.neo4j.kernel.Traversal;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.*;

/**
 * Unit test for {@link app.viewTrustNetwork}.
 */
public class ViewTrustNetwork {

	@Before
	@After
	public void clearDb() {
		app.GraphDatabase.clearDb();
	}	

	/*
	 * checks the simple case of two users
	 * A->B
	 */

	@Test
	public void check_Simple1(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
        try(Transaction tx=gdb.beginTx()){	
			Email e1=new Email("a@a.com");
			Email e2=new Email("b@b.com");
		
			User a=new User();
			User b=new User();

			a.addEmail(e1);
			a.addEmail(e2);

			new TrustEdge(a,b,new Subject("testing"));

			//	viewTrustNetwork(e1);
		}
	}
	/*
	 *chcks the case of 3 user
	 *A->B->C
	 */

	@Test
	public void check_Simple2(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
        try(Transaction tx=gdb.beginTx()){	
			Email e1=new Email("a@a.com");
			Email e2=new Email("b@b.com");
			Email e3=new Email("c@c.com");

			User a=new User();
			User b=new User();
			User c=new User();

			a.addEmail(e1);
			b.addEmail(e2);
			c.addEmail(e3);

			new TrustEdge(a,b,new Subject("testing"));
			new TrustEdge(b,c,new Subject("testing"));
	
			//viewTrustNetwork(e1);
		}
	}
	
	/*
	 * checks the case of 2 connected users and
	 * one loner
	 * A->B
	 * C
	 */

	@Test
	public void check_simple3(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
        try(Transaction tx=gdb.beginTx()){	
			Email e1=new Email("a@a.com");
			Email e2=new Email("b@b.com");
			Email e3=new Email("c@c.com");

			User a=new User();
			User b=new User();
			User c=new User();

			a.addEmail(e1);
			b.addEmail(e2);
			c.addEmail(e3);

			new TrustEdge(a,b,new Subject("testing"));;
	
			//viewTrustNetwork(e1);
			//viewTrustNetwork(e2);
		}
	}

	/*
	 * checks the case of trust not going backwards
	 * A->B
	 */

	@Test
	public void check_simple4(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
        try(Transaction tx=gdb.beginTx()){	
			Email e1=new Email("a@a.com");
			Email e2=new Email("b@b.com");

			User a=new User();
			User b=new User();

			a.addEmail(e1);
			b.addEmail(e2);

			new TrustEdge(a,b,new Subject("testing"));;
	
			//viewTrustNetwork(e2);
		}
	}

	/*
	 * checks the case of somone trusting
	 * the email that we are viewing
	 * A->B->C
	 */

	@Test
	public void check_simple5(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
        try(Transaction tx=gdb.beginTx()){	
			Email e1=new Email("a@a.com");
			Email e2=new Email("b@b.com");
			Email e3=new Email("c@c.com");

			User a=new User();
			User b=new User();
			User c=new User();

			a.addEmail(e1);
			b.addEmail(e2);
			c.addEmail(e3);

			new TrustEdge(a,b,new Subject("testing"));
			new TrustEdge(b,c,new Subject("testing"));
		
			//viewTrustNetwork(e2);
		}
	}
	
	/*
	 * checks the case of multiple users trusting a mutual user
	 * A->C
	 * B->C
	 */
	
	@Test
	public void check_simple6(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
        try(Transaction tx=gdb.beginTx()){	
			Email e1=new Email("a@a.com");
			Email e2=new Email("b@b.com");
			Email e3=new Email("c@c.com");

			User a=new User();
			User b=new User();
			User c=new User();

			a.addEmail(e1);
			b.addEmail(e2);
			c.addEmail(e3);

			new TrustEdge(a,c,new Subject("testing"));
			new TrustEdge(b,c,new Subject("testing"));
		
			//viewTrustNetwork(e2);
		}
	}
}
