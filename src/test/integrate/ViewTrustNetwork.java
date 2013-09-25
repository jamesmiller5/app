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
import java.util.LinkedList;
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
			b.addEmail(e2);

			new TrustEdge(a,b,new Subject("testing"));

			assertTrue(testFunc(e1.getAddress()).equals(new String("testing\nb@b.com\n")));
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

			assertTrue(testFunc(e1.getAddress()).equals("testing\nb@b.com\ntesting\nc@c.com\n"));
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

			assertTrue(testFunc(e1.getAddress()).equals("testing\nb@b.com\n"));
			assertTrue(testFunc(e2.getAddress()).equals(""));
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

			assertTrue(testFunc(e2.getAddress()).equals(""));
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

			assertTrue(testFunc(e2.getAddress()).equals("testing\nc@c.com\n"));
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

			assertTrue(testFunc(e2.getAddress()).equals("testing\nc@c.com\n"));
		}
	}


	public String testFunc( String email ) {
		String out=new String("");
		GraphDatabaseService gdb=app.GraphDatabase.get();
		try(Transaction tx=gdb.beginTx()){
			Email e2=new Email(email);
			User me=e2.getUser();
			if(me==null){
				me=new User();
				me.addEmail(e2);
			}
			Node start=me.getInternalNode();
			LinkedList<Node> q=new LinkedList<Node>();
			LinkedList<String> mark=new LinkedList<String>();

			//BFS
			q.addFirst(start);
			for(Email e3:new User(start).viewEmails()){
				mark.add(e3.getAddress());
				break;
			}
			while(!q.isEmpty()){
				Node temp;
				temp=(Node)q.removeLast();

				// r is relationship from User to TE
				for(Relationship r: temp.getRelationships(RelType.FROM)){
					for(Relationship r2:r.getEndNode().getRelationships(RelType.TO)){
						out=out+r2.getStartNode().getProperty("subject")+"\n";
					}
					//r2 is relationship from TE to next User
					for(Relationship r2:r.getEndNode().getRelationships(RelType.TO)){
						//accessing Email for identification to print
						for(Email e:new User(r2.getEndNode()).viewEmails()){
							//if email has hasnt been added
							if(!mark.contains(e.getAddress())){
								mark.add(e.getAddress());
								q.addFirst(r2.getEndNode());
								for(Email e1:new User(r2.getEndNode()).viewEmails()){
									out=out+e1.getAddress()+"\n";
									break;
								}
							}
						}
						break;
					}
				}
			}

		}
		System.out.println(out);
		return out;
	}
}
